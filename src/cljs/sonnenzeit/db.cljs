(ns sonnenzeit.db
  (:require [re-frame.core :as re-frame]))

(def default-db
  {:name "sonnenzeit"
   :time "00:00:00"         ;; todo better default
   :sunrise-time "00:00:00" ;; todo better default
   :sunset-time "00:00:00"  ;; todo better default
   :status "waiting..."
   :nothing nil
   :geolocation {:coords {:latitude 52.503745 :longitude 13.4229089}}}) ;; berlin
  ;  :geolocation {:coords {:latitude -51.0001666 :longitude -73.1827937}}}) ;; grey
