(ns lead-indicators.core
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [incanter.core :as incanter]
            [incanter.charts :as charts]
            [incanter.stats :as stats]
            [clj-time.format :as time-format]))


(defn load-file [filename]
  (io/file (io/resource filename)))


(defn load-raw-data [timeseries-file]
  (edn/read-string (slurp timeseries-file)))


(defn parse-date [date-str]
  (.getMillis (time-format/parse (time-format/formatter "yyyyMMdd") date-str)))


(defn transform-date-string-col [dataset]
  (incanter/transform-col dataset :date parse-date))


(defn add-cumulative-column [dataset]
  (let [cumulative-column (incanter/cumulative-sum (incanter/$ :value dataset))]
    (incanter/add-column :cumulative cumulative-column dataset)))


(defn add-cumulative-mean-column [dataset]
  (let [c-mean-col (stats/cumulative-mean (incanter/$ :value dataset))]
    (incanter/add-column :c-mean c-mean-col dataset)))


(defn time-series-plot [time-series]
  (let [time-series-plot (charts/time-series-plot
                           :date    :value            ;; must specify x & y column names of dataset
                           :x-label "Date"
                           :y-label "Value"
                           :points  true
                           :title   "Timeseries of values (red) and cumulative mean (blue)"
                           :data    time-series)
        c-mean-plot      (incanter.charts/add-lines time-series-plot
                                                   (incanter/$ :date time-series)
                                                   (incanter/$ :c-mean time-series)
                                                   :points true)]
    c-mean-plot))


(defn cumulative-plot [time-series]
  (charts/time-series-plot
    :date :cumulative
    :x-label "Date"
    :y-label "Cumulative value"
    :points  true
    :title   "Cumulative value over time"
    :data    time-series))


(defn -main []
  (let [timeseries-file      (load-file "clojure")
        raw-data             (load-raw-data timeseries-file)
        raw-dataset          (incanter/dataset [:date :value] raw-data)
        clean-dataset        (transform-date-string-col raw-dataset)
        ordered-dataset      (incanter/$order :date :asc clean-dataset)
        time-series-dataset  (add-cumulative-column ordered-dataset)
        complete-dataset     (add-cumulative-mean-column time-series-dataset)
        c-mean-chart         (time-series-plot complete-dataset)
        cumulative-chart     (cumulative-plot complete-dataset)]
    (incanter/view c-mean-chart)
    (incanter/view cumulative-chart)))
