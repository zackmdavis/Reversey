(ns Reversey.core)

(defn make_board [n]
  (vec (for [row (range n)]
    (vec (for [col (range n)] (atom :empty))))))

(defn row [position]
  (first position))

(defn col [position]
  (second position))

(defn lookup [board position]
  ((board (row position)) (col position)))

(def colors #{:white :black})

(defn opposing_color [color]
  (cond (= color (first colors)) (second colors)
        (= color (second colors)) (first colors)
        :else nil))

(defn place_disc! [board position color]
  (let [square (lookup board position)]
    (if (= @square :empty)
      (reset! square color)
      nil)))

(defn flip! [board position]
  (let [square (lookup board position)]
    (if (contains? colors @square)
      (swap! square opposing_color)
      nil)))

(defn displacement [position1 position2]
  [(- (row position2) (row position1))
   (- (col position2) (col position1))])

(defn displaced_by [position displacement]
  [(+ (row position) (row displacement))
   (+ (col position) (col displacement))])

(defn normalized [displacement]
  (if (or (= (row displacement) 0)
          (= (col displacement) 0)
          (= (Math/abs (row displacement)) (Math/abs (col displacement))))
    (let [magnitude (max (row displacement) (col displacement))]
      [(/ (row displacement) magnitude)
       (/ (col displacement) magnitude)])
    nil))

;; This is not very elegant and should probably be rewritten
(defn positions_between_positions [position1 position2]
  (let [direction
        (normalized (displacement position1 position2))]
    (if direction
      (loop [positions [position1]]
        (if (= (last positions) position2)
          (take (- (count positions) 2) (rest positions)) ;; wtf
          (recur (conj positions
                       (displaced_by (last positions)
                                     direction)))))
      [])))

(defn flip_between_positions! [board position1 position2]
  (doseq [p (positions_between_positions position1 position2)]
    (flip! board p)))