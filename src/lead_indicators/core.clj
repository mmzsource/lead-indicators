(ns lead-indicators.core
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [incanter.core :as incanter]
            [incanter.charts :as charts]
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

(defn time-series-plot [time-series]
  (let [time-series-plot (charts/time-series-plot
                           :date    :value            ;; must specify x & y column names of dataset
                           :x-label "Date"
                           :y-label "Value"
                           :points true
                           :title   "Timeseries of values (red) and cumulative (blue)"
                           :data    time-series)
        cumulative-plot (incanter.charts/add-lines time-series-plot
                                                   (incanter/$ :date time-series)
                                                   (incanter/$ :cumulative time-series)
                                                   :points true)]
    cumulative-plot))

(defn -main []
  (let [timeseries-file (load-file "clojure")
        raw-data (load-raw-data timeseries-file)
        raw-dataset (incanter/dataset [:date :value] raw-data)
        clean-dataset (transform-date-string-col raw-dataset)
        ordered-dataset (incanter/$order :date :asc clean-dataset)
        time-series-dataset (add-cumulative-column ordered-dataset)
        lead-indicator-chart (time-series-plot time-series-dataset)]
    (incanter/view lead-indicator-chart)))

