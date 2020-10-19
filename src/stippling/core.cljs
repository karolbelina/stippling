(ns stippling.core
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn generate-line-lattice [sl l o]
  (let [generated-range (range (+ (/ sl l) 1))
        transformed-offset (mod (- l (mod o l)) l)
        half-length (/ l 2)]
    (map (fn [x] (+ (- (* x l) transformed-offset) half-length)) generated-range)))

(defn generate-rect-lattice [sw sh w h x y]
  (let [xs (generate-line-lattice sw w x)
        ys (generate-line-lattice sh h y)]
    (for [x xs, y ys] {:x x, :y y})))

(defn generate-square-lattice [sw sh a x y]
  (generate-rect-lattice sw sh a a x y))

(defn generate-hexagonal-lattice [sw sh a x y]
  (let [a-sqrt-3 (* a (q/sqrt 3))
        set-a (generate-rect-lattice sw sh a a-sqrt-3 x y)
        set-b (generate-rect-lattice sw sh a a-sqrt-3 (+ x (/ a 2)) (+ y (/ a-sqrt-3 2)))]
    (concat set-a set-b)))

(defn setup-texture [data w h]
  (let [pixel-count (* w h)
        texture (vec (repeat pixel-count 0))]
    (dotimes [i pixel-count]
      (let [r (aget data (+ (* i 4) 0))
            g (aget data (+ (* i 4) 1))
            b (aget data (+ (* i 4) 2))]
        (aset texture i (/ (+ r b g) 3 255))))
    texture))

(defn setup-state [data w h]
  {:points (generate-hexagonal-lattice w h 8 0 0)
   :texture (setup-texture data w h)
   :width w
   :height h})
  
(defn calculate-radius [state point]
  (let [w (:width state)
        h (:height state)
        x (max 0 (min w (int (:x point))))
        y (max 0 (min h (int (:y point))))
        i (+ (* y w) x)
        pixel (aget (:texture state) i)]
    pixel))

(defn draw-state [state]
  (q/background 0)
  (q/no-stroke)
  (q/fill 255 255 255)
  (doseq [point (:points state)]
    (let [value (calculate-radius state point)
          r (* value 10)]
      (q/ellipse (:x point) (:y point) r r)))
  (q/no-loop))

(defn ^:export run-sketch [data w h]
  (q/defsketch stippling
    :host "stippling"
    :size [w h]
    :setup (fn []
      (q/frame-rate 30)
      (q/color-mode :rgb)
      (q/image-mode :corner)
      (setup-state data w h))
    :draw draw-state
    :middleware [m/fun-mode]))
