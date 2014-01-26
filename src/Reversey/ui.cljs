(ns Reversey.ui
  (:require [Reversey.core :as r]))

(def circle_constant 6.283185307179586)

(def the_board (r/make_board 8))
(r/place_disc! the_board [3 3] :white)
(r/place_disc! the_board [3 4] :black)
(r/place_disc! the_board [4 3] :black)
(r/place_disc! the_board [4 4] :white)

(defn render_disc [canvas board position]
  (let [context (.getContext canvas "2d")
        unit (/ (.-width canvas) 8)
        center (map #(* unit (+ 0.5 %)) position)
        color (apply str
                     (rest
                      (str (deref (r/lookup board position)))))]
    (.beginPath context)
    (.arc context 
          (second center) (first center)
          (* 0.4 unit)
          0 circle_constant)
    (.stroke context)
    (set! (.-fillStyle context) color)    
    (.fill context)))

(defn draw_line [context width height start_row start_col end_row end_col]
  (let [unit (/ width 8)]
    (.beginPath context)
    (.moveTo context (* start_col unit) (* start_row unit))
    (.lineTo context (* end_col unit) (* end_row unit))
    (.stroke context)))

(defn draw_board [board]
  (let [canvas (.getElementById js/document "canvas")
        width (.getAttribute canvas "width")
        height (.getAttribute canvas "height")
        context (.getContext canvas "2d")]
    (set! (.-fillStyle context) "green")
    (.fillRect context 0 0 width height)
    (doseq [row (range 1 8)]
      (draw_line context width height row 0 row 8))
    (doseq [col (range 1 8)]
      (draw_line context width height 0 col 8 col))
    (doseq [row (range 8)]
      (doseq [col (range 8)]
        (if (deref (r/lookup board [row col]))
          (render_disc canvas board [row col]))))))

(draw_board the_board)
