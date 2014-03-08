(ns Reversey.ui
  (:require [Reversey.core :as r]))

(def circle_constant 6.283185307179586)

(def now_to_move (atom :black))

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
        unit (/ width 8)
        context (.getContext canvas "2d")]
    (set! (.-fillStyle context) "green")
    (.fillRect context 0 0 width height)
    (doseq [row (range 1 8)]
      (draw_line context width height row 0 row 8))
    (doseq [col (range 1 8)]
      (draw_line context width height 0 col 8 col))
    (doseq [row (range 8)]
      (doseq [col (range 8)]
        (cond (deref (r/lookup board [row col]))
                  (do
                    (render_disc canvas board [row col])
                    (.log js/console row col "occupied"))
              (r/legal_move? board [row col] @now_to_move)
                  (do
                    (set! (.-fillStyle context) "#90EE90")
                    (.fillRect context (* col unit) (* row unit) unit unit)
                    (.log js/console [@now_to_move [row col]])
                    (set! (.-fillStyle context) "green")))))))

(defn click_handler [event]
  (let [canvas (.getElementById js/document "canvas")
        unit (/ (.-width canvas) 8)
        row (int (/ (.-pageY event) unit))
        col (int (/ (.-pageX event) unit))
        position [row col]]
    (r/move! the_board position @now_to_move)
    (swap! now_to_move r/opposing)
    (.log js/console (name @now_to_move))
    (draw_board the_board)))

(defn set_click_handler []
  (let [canvas (.getElementById js/document "canvas")]
    (set! (.-onclick canvas) click_handler)))

(draw_board the_board)
(set_click_handler)