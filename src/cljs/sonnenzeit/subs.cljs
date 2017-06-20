(ns sonnenzeit.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]))

;; query: what is your name?
(re-frame/reg-sub
 :name-subscription
 (fn [db]
   (:name db)))

(re-frame/reg-sub
  :time-subscription
  (fn [db _]        ;; db is current app state. 2nd unused param is query vector
    (-> db
      :time)))

(re-frame/reg-sub
  :sunset-time-subscription
  (fn [db _]
    (-> db
      :sunset-time)))

(re-frame/reg-sub
  :sunrise-time-subscription
  (fn [db _]
    (-> db
      :sunrise-time)))

(re-frame/reg-sub
  :status-subscription
  (fn [db _]
    (-> db
      :status)))
