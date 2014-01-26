(ns Reversey.ui
  (:require [Reversey.core :as r]))

(def board (r/make_board 8))
(r/place_disc! board [3 3] :white)
(r/place_disc! board [3 4] :black)
(r/place_disc! board [4 3] :black)
(r/place_disc! board [4 4] :white)

(defn draw_line [context width height start_row start_col end_row end_col]
  (let [unit (/ width 8)]
    (.beginPath context)
    (.moveTo context (* start_col unit) (* start_row unit))
    (.lineTo context (* end_col unit) (* end_row unit))
    (.stroke context)))

(defn draw_board []
  (let [canvas (.getElementById js/document "canvas")
        width (.getAttribute canvas "width")
        height (.getAttribute canvas "height")
        context (.getContext canvas "2d")]
    (set! (.-fillStyle context) "green")
    (.fillRect context 0 0 width height)
    (doseq [row (range 1 8)]
      (draw_line context width height row 0 row 8))
    (doseq [col (range 1 8)]
      (draw_line context width height 0 col 8 col))))

(draw_board)
