(ns sonnenzeit.views
  (:require [re-frame.core :as re-frame]))


(defn clock-stub
  []
  [:div "16:20"])

(defn clock
  []
  [:div
    [:em "currently "]
    (-> @(re-frame/subscribe [:time])
       .toTimeString
       (clojure.string/split " ")
       first)])

(defn sunset
  []
  [:div
    [:em "sunset "]
    (-> @(re-frame/subscribe [:sunset-time])
      (clojure.string/split "T")
      last
      (clojure.string/split "+")
      first)])

(defn main-panel
  []
  (let [name (re-frame/subscribe [:name])]
    (fn []
      [:div
        [:h4 @name]
        [clock]
        [sunset]])))
