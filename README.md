# clj-tiny-jmx

A little jmx lib allowing you to create mbeans with a bit convenience

## Usage

```clojure

(defmbean Foo []
  (getFoo [this] (+ 1 2))
  (invokeBar [this ^String x] (println x)))

(register-mbean (mbean-server) "clj-tiny-jmx:name=foo" (Foo.))

```

## License

Copyright © 2014 Dan Stone

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
