(ns lead-indicators.core
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [incanter.core :as incanter]
            [incanter.charts :as charts]
            [clj-time.format :as time-format]))

(defn parse-date [date-str]
  (time-format/parse (time-format/formatter "yyyyMMdd") date-str))

(defn chart [time-series]
  (charts/time-series-plot
    :date    :hours                             ;; must specify x & y column names of dataset
    :x-label "Date"
    :y-label "Hours working with Clojure"
    :title   "Clojure learning - lead indicator"
    :data    time-series))

(defn -main []
  (let [timeseries-file (io/file (io/resource "example"))
        raw-data (edn/read-string (slurp timeseries-file))
        clean-data (map #(vector (.getMillis (parse-date (first %))) (second %)) raw-data)
        time-series (incanter/dataset [:date :hours] clean-data)
        lead-indicator-chart (chart time-series)]
    (incanter/view lead-indicator-chart)))


