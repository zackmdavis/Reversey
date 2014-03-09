(ns Reversey.ui
  (:require [Reversey.core :as r]))

(def circle_constant 6.283185307179586)

(def the_board (r/make_board 8))
(def the_setup (vector [[3 3] :white] [[3 4] :black]
                       [[4 3] :black] [[4 4] :white]))
(doseq [[initial_position color] the_setup]
  (r/place_disc! the_board initial_position color))

(def now_to_move (atom :black))

(defn render_disc [canvas color position]
  (let [context (.getContext canvas "2d")
        color (name color)
        unit (/ (.-width canvas) 8)
        center (map #(* unit (+ 0.5 %)) position)]
    (.beginPath context)
    (.arc context 
          (second center) (first center)
          (* 0.4 unit)
          0 circle_constant)
    (.stroke context)
    (set! (.-fillStyle context) color)    
    (.fill context)))

(defn render_highlight [canvas position]
  (let [context (.getContext canvas "2d")
        unit (/ (.-width canvas) 8)
        [row col] position]
    (set! (.-fillStyle context) "#80DE80")
    (.fillRect context
               (* col unit) (* row unit)
               unit unit)
    (set! (.-fillStyle context) "green")))

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
        context (.getContext canvas "2d")
        surface (r/surface board @now_to_move)]
    (set! (.-fillStyle context) "green")
    (.fillRect context 0 0 width height)
    (doseq [row (range 1 8)]
      (draw_line context width height row 0 row 8))
    (doseq [col (range 1 8)]
      (draw_line context width height 0 col 8 col))
    (doseq [row (range 8)]
      (doseq [col (range 8)]
        (let [position [row col]
              prophecy (r/lookup surface position)
              disc_maybe (first prophecy)
              vision (second prophecy)]
          (cond disc_maybe (render_disc canvas disc_maybe position)
                (seq vision) (render_highlight canvas position)))))))
    
(defn color_commentary [board to_move]
  (let [scoreboard (r/score board)
        to_move_span (.getElementById js/document "to-move")
        black_score_span (.getElementById js/document "black-score")
        white_score_span (.getElementById js/document "white-score")
        mover (name to_move)]
    (.setAttribute to_move_span "class" mover)
    (set! (.-innerHTML to_move_span) mover)
    (set! (.-innerHTML black_score_span) (str (scoreboard :black)))
    (set! (.-innerHTML white_score_span) (str (scoreboard :white)))))

(defn click_handler [event]
  (let [canvas (.getElementById js/document "canvas")
        unit (/ (.-width canvas) 8)
        row (int (/ (.-pageY event) unit))
        col (int (/ (.-pageX event) unit))
        position [row col]]
    (when (r/legal_move? the_board position @now_to_move)
      (r/move! the_board position @now_to_move)
      (swap! now_to_move r/opposing)
      (color_commentary the_board @now_to_move)
      (draw_board the_board))))

(defn set_click_handler []
  (let [canvas (.getElementById js/document "canvas")]
    (set! (.-onclick canvas) click_handler)))

(draw_board the_board)
(set_click_handler)