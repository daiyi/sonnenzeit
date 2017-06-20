(ns sonnenzeit.views
  (:require [re-frame.core :as re-frame]))


(defn status
  []
  [:div @(re-frame/subscribe [:status-subscription])])

(defn clock
  []
  [:div
    [:em "currently "]
    (-> @(re-frame/subscribe [:time-subscription])
       .toTimeString
       (clojure.string/split " ")
       first)])

(defn sunrise
 []
 [:div
   [:em "sunrise "]
   (-> @(re-frame/subscribe [:sunrise-time-subscription])
     (clojure.string/split "T")
     last
     (clojure.string/split "+")
     first)])

(defn sunset
  []
  [:div
    [:em "sunset "]
    (-> @(re-frame/subscribe [:sunset-time-subscription])
      (clojure.string/split "T")
      last
      (clojure.string/split "+")
      first)])

(defn shape
  []
  [:svg {:viewBox "-300 -300 600 600" :preserveAspectRatio "xMidYMid meet"}
    [:circle {:cx 0 :cy 0 :r 200 :stroke "white" :stroke-width 2 :fill "none"}]]
)

(defn main-panel
  []
  (let [name (re-frame/subscribe [:name-subscription])]
    (fn []
      [:div.page
        [:div.content
          [:h4 @name]
          [status]
          [clock]
          [sunrise]
          [sunset]
          [:div
            [:button {:on-click #(re-frame/dispatch [:request-geolocation])} "loc?"]]]
        [:div.sundial
          [shape]]
      ]
    )))
