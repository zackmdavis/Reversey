(ns Reversey.core)

(defn make_board [n]
  (vec (for [row (range n)]
    (vec (for [col (range n)] (atom nil))))))

(defn row [position]
  (first position))

(defn col [position]
  (second position))

(defn lookup [board position]
  ((board (row position)) (col position)))

(def colors #{:white :black})

(defn opposing [color]
  (cond (= color (first colors)) (second colors)
        (= color (second colors)) (first colors)))

(defn place_disc! [board position color]
  (let [square (lookup board position)]
    (if-not @square
      (reset! square color))))

(defn flip! [board position]
  (let [square (lookup board position)]
    (if (contains? colors @square)
      (swap! square opposing))))

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

(def directions
  (remove #(= [0 0] %)
          (for [i (range -1 2)
                j (range -1 2)]
            [i j])))

(defn in_bounds? [board_size position]
  (every? #(and (>= % 0) (< % board_size)) 
          position))

(defn positions_in_direction [board_size position direction]
  (take-while #(in_bounds? board_size %)
              (for [step (range 1 board_size)]
                (displaced_by_times position direction step))))

(defn to_flip_in_direction [board position direction]
   (let [color (deref (lookup board position))
         ray (positions_in_direction (count board) position direction)
         candidates (take-while #(= (deref (lookup board %))
                                    (opposing color))
                                ray)
         sentinel (if (< (count candidates) (count ray))
                    (nth ray (count candidates)))]
     (if sentinel
       (if (= (deref (lookup board sentinel)) color)
         candidates
         []) ; not the most
       []))) ; elegant thing

(defn move! [board position color] 
  (place_disc! board position color)
  (doseq [direction directions]
    (doseq [to_flip (to_flip_in_direction board position direction)]
      (flip! board to_flip))))
