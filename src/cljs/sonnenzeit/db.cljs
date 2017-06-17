(ns sonnenzeit.db
  (:require [re-frame.core :as re-frame]))

(def default-db
  {:name "\\o/"
   :time (js/Date.)
   :sunset-time "2017-06-17T00:00:00+00:00"
   :ajax-status nil})
