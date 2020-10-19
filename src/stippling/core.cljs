(ns stippling.core
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn setup-state [data w h]
  {:color 0
   :angle 0
   :image (q/load-image data)})

(defn update-state [state]
  {:color (mod (+ (:color state) 0.7) 255)
   :angle (+ (:angle state) 0.1)
   :image (:image state)})

(defn draw-state [state]
  (q/background 0)
  (q/no-stroke)
  (q/fill 255 255 255)
  (q/image (:image state) 0 0))

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
