(ns sonnenzeit.events
  (:require [re-frame.core :as re-frame :refer [reg-event-fx]]
            [sonnenzeit.db :as db]
            [ajax.core :as ajax]          ;; https://github.com/JulianBirch/cljs-ajax
            [day8.re-frame.http-fx]))     ;; https://github.com/Day8/re-frame-http-fx

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-db                 ;; usage:  (dispatch [:timer a-js-Date])
  :timer                         ;; every second an event of this kind will be dispatched
  (fn [db [_ new-time]]          ;; note how the 2nd parameter is destructured to obtain the data value
    (def clock-time (-> new-time
       .toTimeString
       (clojure.string/split " ")
       first))
    (assoc db :time clock-time)))  ;; compute and return the new application state

(re-frame/reg-event-db
  :process-response
  (fn
    [db [_ response]]           ;; destructure the response from the event vector
    (def results (:results (js->clj response)))
    (def sunrise (-> results :sunrise
                     (clojure.string/split "T")
                     last
                     (clojure.string/split "+")
                     first))
    (def sunset (-> results :sunset
                    (clojure.string/split "T")
                    last
                    (clojure.string/split "+")
                    first))

    (.log js/console "suntimes success" response sunrise sunset)

    (-> db
      (assoc :data response)
      (assoc :sunrise-time sunrise)
      (assoc :sunset-time sunset)
      )))

(re-frame/reg-event-db
  :bad-response
  (fn
    [db [_ response]]
    (js/console.log "bad response!!" response)
    (assoc db :status "fail")))    ;; note: it needs an assoc to work. why?


(defn process-geolocation [position]
  ; (def longitude (.-longitude js/position.coords))
  ; (def latitude (.-latitude js/position.coords))
  (.log js/console "geolocation success" position)
)

(re-frame/reg-event-db
  :request-geolocation
  (fn
    [db [_response]]
    (.getCurrentPosition js/navigator.geolocation.
      ; #(assoc db :geolocation %)
      ; #(.log js/console "geolocation success" %)
      ; #(.log js/console (clj->js db))
      #(re-frame/dispatch [:geolocation-success (js->clj %)])
      #(re-frame/dispatch [:geolocation-fail (js->clj %)])
    )
    (.log js/console "requesting geolocation")
    (assoc db :status "requesting geolocation")
  ))

(re-frame/reg-event-db
  :status-update
  (fn
    [db [_ new-status]]
    (.log js/console "updating status to " new-status)
    (assoc db :status new-status)
  ))

(re-frame/reg-event-db
  :geolocation-fail
  (fn
    [db [_]]
    (re-frame/dispatch [:status-update "geolocation failed"])
    (re-frame/dispatch [:request-suntimes])
    (assoc db :geolocation {:coords {:latitude 52.503745 :longitude 13.4229089} :status "failed"})
  ))

(re-frame/reg-event-db
  :geolocation-success
  (fn
    [db [_ loc]]
    (.log js/console "updating geolocation to " loc)
    (re-frame/dispatch [:status-update "geolocation success!!"])
    (re-frame/dispatch [:request-suntimes])
    (assoc db :geolocation loc)
  ))

;; -- Domino 1 - Event Dispatch -----------------------------------------------
;; note: does this belong here??

(defn dispatch-timer-event
 []
 (let [now (js/Date.)]
   (re-frame/dispatch [:timer now])))  ;; <-- dispatch used

;; Call the dispatching function every second.
;; `defonce` is like `def` but it ensures only instance is ever
;; created in the face of figwheel hot-reloading of this file.
(defonce do-timer (js/setInterval dispatch-timer-event 1000))



;; Talking to servers! ----------------------------------

(reg-event-fx        ;; from day8.re-frame.http-fx
  :request-suntimes        ;; <-- the event id
  (fn                ;; <-- the handler function
    [{db :db} _]     ;; <-- 1st argument is coeffect, from which we extract db

    (def lat (-> db :geolocation :coords :latitude))
    (def lng (-> db :geolocation :coords :longitude))
    ; (def lng (.-longitude js/position.coords))

    ;; we return a map of (side) effects
    {:http-xhrio {:method          :get
                  :uri             "https://api.sunrise-sunset.org/json"
                  :format          (ajax/json-request-format)
                  :params          {:lat lat :lng lng :formatted "0"}
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success      [:process-response]
                  :on-failure      [:bad-response]}
     :db  (assoc db :loading? true)}))
