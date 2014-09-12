(ns clj-jmx.core
  (:import (javax.management MBeanInfo ObjectName MBeanServer)
           (java.lang.management ManagementFactory)))

(defn ^MBeanServer platform
  "Gets the platform MBean server"
  []
  (ManagementFactory/getPlatformMBeanServer))

(defn ^ObjectName object-name
  "Turn a string into a javax.management.ObjectName"
  [s]
  (ObjectName. (str s)))

(defn register-mbean
  "Register an mbean with the jmx server - using the given name"
  [^MBeanServer server name bean]
  (.registerMBean server bean (object-name name)))

(defn registered?
  "Checks whether the mbean is registered with the jmx server"
  [^MBeanServer server name]
  (.isRegistered server (object-name name)))

(defn unregister-bean
  "Unregister an mbean with the jmx server at the given name"
  [^MBeanServer server name]
  (.unregisterMBean server (object-name name)))

(defmacro defmbean
  "Defines an mbean interface and its corresponding implementation.
   the opts are defined like deftype operations - so.
   (defmbean Foo []
     (getBar [this a b] (+ a b))
     (invokeX [this x] (println x)))"
  [name fields & opts]
  (let [mbean-name (symbol (str name "MBean"))]
    `(do
      (definterface ~mbean-name
                     ~@(for [[sym [_ & args]] opts]
                         `(~sym [~@args])))
      (deftype ~name ~fields
         ~mbean-name
         ~@(for [[sym args & body] opts]
             `(~sym ~args ~@body))))))


(comment
  (unregister-bean (platform) "clj-jmx:name=foo")
  (register-mbean (platform) "clj-jmx:name=foo" (Foo.)))


