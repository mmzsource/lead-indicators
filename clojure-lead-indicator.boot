#!/usr/bin/env boot

(set-env! 
  :resource-paths #{"."}
  :dependencies '[[clj-time "0.13.0"] [incanter "1.9.0"]])


(require '[incanter.core :as incanter])
(require '[incanter.stats :as stats])
(require '[incanter.charts :as charts])
(require '[incanter.optimize :as optimize])
(require '[clj-time.format :as time-format])


(def lead-indicator-chart "/Users/mmz/Projects/clojure-lead-indicator/lead-indicator-chart.png")


(def raw-data [["20170531" 3] ["20170530" 1] ["20170529" 1]])


(defn parse-date [date-str] 
  (time-format/parse (time-format/formatter "yyyyMMdd") date-str)) 


;; (map #(parse-date (first %)) raw-data)


(def dataset 
  (incanter/dataset [:date :hours] [[7 3][8 2][9 3]]))


(def chart 
  (charts/time-series-plot 
    :date    :hours                             ;; must specify x & y column names of dataset
    :x-label "Date"
    :y-label "Hours working with Clojure"
    :title   "Clojure learning - lead indicator"
    :data    dataset))


;;  Since this runs as a boot script, and the JVM is stopped when the boot script is done, 
;;  I must first persist and then open the chart if I want to see the results:

(incanter/save chart lead-indicator-chart)
(incanter/view (str "file://" lead-indicator-chart))
