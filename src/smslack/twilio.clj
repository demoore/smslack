(ns smslack.twilio
  (:require [org.httpkit.client :as http]
            [ring.util.codec :as codec]
            [environ.core :refer [env]]))

(def twilio-auth-sid
  (env :twilio-auth-sid))

(def twilio-auth-token
  (env :twilio-auth-token))

(def twilio-number
  (env :twilio-number))

(def twilio-send-endpoint
  (str "https://api.twilio.com/2010-04-01/Accounts/" twilio-auth-sid "/Messages.json"))

(defn split-number-and-message [message]
  (clojure.string/split message #" " 2))

(defn send-text [to message]
  (let [options {:form-params {:To to :From twilio-number :Body message}
                 :basic-auth [twilio-auth-sid twilio-auth-token]}
      {:keys [status body error]} @(http/post twilio-send-endpoint options)]
  (if error
    (println "Failed, exception is " error)
    (println "Async HTTP POST: " body))))
