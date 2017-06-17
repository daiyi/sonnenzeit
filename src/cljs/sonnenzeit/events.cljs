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
    (assoc db :time new-time)))  ;; compute and return the new application state

(re-frame/reg-event-db
  :process-response
  (fn
    [db [_ response]]           ;; destructure the response from the event vector
    (-> db
        (assoc :data (js->clj response))
        (assoc :sunset-time (:sunset (:results (js->clj response)))
          ))))

(re-frame/reg-event-db
  :bad-response
  (fn
    [db [_ response]]
    (js/console.log "bad response!!")
    (assoc db :ajax-status "fail")))    ;; note: it needs an assoc to work. why?


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
  :request-sunset        ;; <-- the event id
  (fn                ;; <-- the handler function
    [{db :db} _]     ;; <-- 1st argument is coeffect, from which we extract db

    ;; we return a map of (side) effects
    {:http-xhrio {:method          :get
                  :uri             "https://api.sunrise-sunset.org/json"
                  :format          (ajax/json-request-format)
                  :params          {:lat "52.499357" :lng "-13.421163" :formatted "0"}
                  :response-format (ajax/json-response-format {:keywords? true})
                  :on-success      [:process-response]
                  :on-failure      [:bad-response]}
     :db  (assoc db :loading? true)}))
