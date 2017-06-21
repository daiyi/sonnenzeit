(ns sonnenzeit.db
  (:require [re-frame.core :as re-frame]))

(def default-db
  {:name "sonnenzeit"
   :time (js/Date.)
   :sunrise-time "2017-06-17T00:00:00+00:00" ;; todo better default
   :sunset-time "2017-06-17T00:00:00+00:00" ;; todo better default
   :status "waiting..."
   :nothing nil
   :geolocation {:coords {:latitude -51.0001666 :longitude -73.1827937} :timestamp 1498050678956}})
