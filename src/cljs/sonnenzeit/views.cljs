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

(defn proto-sparkle []
  [:svg.sparkle {:stroke-linecap "square" :viewBox "0.0 0.0 50.0 50.0"}
    [:clip-path#p.0
      [:path {:d "m0 0l50.0 0l0 50.0l-50.0 0l0 -50.0z" :clip-rule "nonzero"}]]
    [:g {:clip-path "url(#p.0)"}
      [:path {:fill "none" :d "m0 0l50.0 0l0 50.0l-50.0 0z" :fill-rule "nonezero"}]
      [:path {:fill "none" :d "m0.62204725 25.0l20.068499 -4.323374l4.309454 -20.13332l4.309454 20.13332l20.068499 4.323374l-20.068499 4.323374l-4.309454 20.133318l-4.309454 -20.133318z" :fill-rule "nonezero"}]
      [:path {:fill "#fff" :stroke-width "1.0" :stroke-linejoin "butt" :d "m0.62204725 25.0l20.068499 -4.323374l4.309454 -20.13332l4.309454 20.13332l20.068499 4.323374l-20.068499 4.323374l-4.309454 20.133318l-4.309454 -20.133318z" :fill-rule "nonezero"}]]])

(defn sparkle [bottom left]
  [:div.sparkle-wrapper {:style {:width "12px" :height "10px" :bottom bottom :left left}}
    [proto-sparkle]])

(defn main-panel
  []
  (let [name (re-frame/subscribe [:name-subscription])]
    (fn []
      [:div.page
        [sparkle 20 10]
        [sparkle 0 "40%"]
        [sparkle -10 "30%"]
        [sparkle 40 "33%"]
        [sparkle 90 "70%"]
        [sparkle 10 "80%"]
        [sparkle 130 "84%"]
        [:div.content
          [:h4 @name]
          [status]
          [clock]
          [sunrise]
          [sunset]
          ; [:div
          ;   [:button {:on-click #(re-frame/dispatch [:request-geolocation])} "loc?"]]
          ]
        [:div.sundial
          [shape]]
      ]
    )))
