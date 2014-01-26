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
  (vec (map - position2 position1)))

(defn displaced_by [position displacement]
  (vec (map + position displacement)))

(defn displaced_by_times [position displacement n]
  (vec (map +
            position
            (map #(* n %)
                 displacement))))

(defn abs [x]
  (Math/abs x))

(defn magnitude [displacement]
  (apply max (map abs displacement)))

(defn normalized [displacement]
  (if (or (= (row displacement) 0)
          (= (col displacement) 0)
          (= (abs (row displacement)) (abs (col displacement))))
    (vec (map (fn [component]
                (/ component (magnitude displacement)))
              displacement))
    nil))

(defn positions_between_positions [position1 position2]
  (let [difference (displacement position1 position2)
        direction (normalized difference)
        between (magnitude difference)]
    (if direction
      (for [step (range 1 between)]
        (displaced_by_times position1 direction step))
      '())))

(defn flip_between_positions! [board position1 position2]
  (doseq [p (positions_between_positions position1 position2)]
    (flip! board p)))

(def directions
  (remove #(= [0 0] %)
          (for [i (range -1 2)
                j (range -1 2)]
            [i j])))

(defn in_bounds [board_size position]
  (every? #(and (>= % 0) (< % board_size)) 
          position))

(defn positions_in_direction [board_size position direction]
  (take-while #(in_bounds board_size %)
              (for [step (range 1 board_size)]
                (displaced_by_times position direction step))))

(defn to_flip_in_direction [board position direction]
   (comment "TODO"))

(defn move! [board position color] 
  (comment "TODO"))
