(ns sonnenzeit.views
  (:require [re-frame.core :as re-frame]))


(defn status
  []
  [:div.info @(re-frame/subscribe [:status-subscription])])

(defn geolocation
  []
  [:div.info (-> @(re-frame/subscribe [:geolocation-subscription])
            .toString)])

(defn clock
  []
  [:div
    [:em "currently "]
    @(re-frame/subscribe [:time-subscription])])

(defn sunrise
 []
 [:div
   [:em "sunrise "]
   @(re-frame/subscribe [:sunrise-time-subscription])])

(defn sunset
  []
  [:div
    [:em "sunset "]
    @(re-frame/subscribe [:sunset-time-subscription])])

(defn get-theta
  [hour]
  (* hour 2 (/ Math/PI 24)))

(defn get-clock-point-now
  [hour]
  {:cx (* 200 (Math/cos (get-theta hour))) :cy (* 200 (Math/sin (get-theta hour))) :r 10 :stroke "none" :fill "#602BA6"})

(defn get-clock-point
  [hour]
  {:cx (* 200 (Math/cos (get-theta hour))) :cy (* 200 (Math/sin (get-theta hour))) :r 10 :stroke "none" :fill "white"})

(defn get-clock-tick
  [hour]
  {:cx (* 200 (Math/cos (get-theta hour))) :cy (* 200 (Math/sin (get-theta hour))) :r 3 :stroke "none" :fill "white"})

(defn get-hour-from-time
  [time]
  (-> time
      (clojure.string/split ":")
      first))

(defn shape
  []
  [:svg.clock {:viewBox "-300 -300 600 600" :preserveAspectRatio "xMidYMid meet"}
    [:circle (get-clock-tick 0)]
    [:circle (get-clock-tick 1)]
    [:circle (get-clock-tick 2)]
    [:circle (get-clock-tick 3)]
    [:circle (get-clock-tick 4)]
    [:circle (get-clock-tick 5)]
    [:circle (get-clock-tick 6)]
    [:circle (get-clock-tick 7)]
    [:circle (get-clock-tick 8)]
    [:circle (get-clock-tick 9)]
    [:circle (get-clock-tick 10)]   ;; halp
    [:circle (get-clock-tick 11)]
    [:circle (get-clock-tick 12)]
    [:circle (get-clock-tick 13)]
    [:circle (get-clock-tick 14)]
    [:circle (get-clock-tick 15)]
    [:circle (get-clock-tick 16)]
    [:circle (get-clock-tick 17)]
    [:circle (get-clock-tick 18)]
    [:circle (get-clock-tick 19)]
    [:circle (get-clock-tick 20)]
    [:circle (get-clock-tick 21)]
    [:circle (get-clock-tick 22)]
    [:circle (get-clock-tick 23)]
    [:circle (get-clock-point
               (get-hour-from-time @(re-frame/subscribe [:sunrise-time-subscription])))]
    [:circle (get-clock-point
               (get-hour-from-time @(re-frame/subscribe [:sunset-time-subscription])))]
    [:circle (get-clock-point-now
               (get-hour-from-time @(re-frame/subscribe [:time-subscription])))]
  ]
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

(defn sparkle-scape []
  [:div
    [sparkle 20 10]
    [sparkle 0 "40%"]
    [sparkle -10 "30%"]
    [sparkle 40 "33%"]
    [sparkle 90 "70%"]
    [sparkle 10 "80%"]
    [sparkle 130 "84%"]])

(defn main-panel
  []
  (let [name (re-frame/subscribe [:name-subscription])]
    (fn []
      [:div.page
        [sparkle-scape]
        [:div.content
          [:h4 @name]
          [clock]
          [sunrise]
          [sunset]
          "_"
          [:div
            [:a.button {:on-click #(re-frame/dispatch [:request-geolocation])} "request geoloc?"]]
          [status]
          [geolocation]
        ]
        [:div.sundial
          [shape]]
      ]
    )))
