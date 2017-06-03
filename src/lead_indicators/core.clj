(ns lead-indicators.core
  (:require [incanter.core :as incanter]
            [incanter.stats :as stats]
            [incanter.charts :as charts]
            [incanter.optimize :as optimize]
            [clj-time.format :as time-format]))

(defn parse-date [date-str]
  (time-format/parse (time-format/formatter "yyyyMMdd") date-str))

(def raw-data [["20170531" 3] ["20170530" 1] ["20170529" 4]])

(def clean-data (map #(vector (.getMillis (parse-date (first %))) (second %)) raw-data))

(prn clean-data)

(def time-series (incanter/dataset [:date :hours] clean-data))

(def dataset
  (incanter/dataset [:date :hours] [[7 3][8 2][9 3]]))

(def chart
  (charts/time-series-plot
    :date    :hours                             ;; must specify x & y column names of dataset
    :x-label "Date"
    :y-label "Hours working with Clojure"
    :title   "Clojure learning - lead indicator"
    :data    time-series))


