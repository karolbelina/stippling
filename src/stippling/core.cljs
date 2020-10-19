(ns stippling.core
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

; -4 -3 -2 -1  0  1  2  3  4
; 0   3  2  1  0  3  2  1  0
(defn transform-offset [offset size]
  (mod (- size (mod offset size)) size))

;     |....----....----....----|.... +4
;    .|...----....----....----.|...  +3
;   ..|..----....----....----..|..   +2
;  ...|.----....----....----...|.    +1
;     |----....----....----....|---- 0
;    -|---....----....----....-|---  -1
;   --|--....----....----....--|--   -2
;  ---|-....----....----....---|-    -3
;     |....----....----....----|     -4
(defn generate-line-lattice [len size offset]
  (let [generated-range (range (/ len size))
        transformed-offset (transform-offset offset size)
        half-size (/ size 2)]
    (map (fn [x]
      (+ (- (* x size) transformed-offset) half-size)) generated-range)))

(defn generate-square-lattice [w h size x y]
  (let [xs (generate-line-lattice w size x)
        ys (generate-line-lattice h size y)]
    (for [x xs, y ys] {:x x, :y y})))

(defn setup-state [data w h]
  {:points (generate-square-lattice w h 5 0 0)
   :image (q/load-image data)})

(defn update-state [state]
  {:points (:points state)
   :image (:image state)})
  
(defn calculate-radius [image point]
  (let [pixel (q/get-pixel image (:x point) (:y point))
        r (aget pixel 0)
        g (aget pixel 1)
        b (aget pixel 2)
        average-rgb (/ (+ r g b) 3)]
    (/ average-rgb 255)))

(defn draw-state [state]
  (q/background 0)
  (q/no-stroke)
  (q/fill 255 255 255)
  ; (q/image (:image state) 0 0)
  (doseq [point (:points state)]
    (let [value (calculate-radius (:image state) point)
          r (* value 7)]
      (q/ellipse (:x point) (:y point) r r))))

(defn ^:export run-sketch [data w h]
  (q/defsketch stippling
    :host "stippling"
    :size [w h]
    :setup (fn []
      (q/frame-rate 30)
      (q/color-mode :rgb)
      (q/image-mode :corner)
      (setup-state data w h))
    :update update-state
    :draw draw-state
    :middleware [m/fun-mode]))
