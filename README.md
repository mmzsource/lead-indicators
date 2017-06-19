# lead-indicators

A Clojure library designed to visualise time-series in an incanter plot.

Expects a file as input with following structure (also see `/resources/example`):

```clojure
[["20170103" 6]
 ["20170102" 3.5]
 ["20170101" 5]]
```

Basically a vector of vectors where the inner vectors represent a date (key) and a value.

## Usage

`lein run`

or 

`lein run filename`

where `filename` is the name of a file residing in the `resources` folder.
Takes `clojure` as default filename (when no args are passed in).

## License

Copyright © 2017 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
