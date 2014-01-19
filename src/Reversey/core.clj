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
