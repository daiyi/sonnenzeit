(ns sonnenzeit.db
  (:require [re-frame.core :as re-frame]))

(def default-db
  {:name "sonnenzeit"
   :time (js/Date.)
   :sunset-time "2017-06-17T00:00:00+00:00" ;; todo better default
   :ajax-status nil})
