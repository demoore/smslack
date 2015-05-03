(ns smslack.slack
  (:require [org.httpkit.client :as http]
            [ring.util.codec :as codec]
            [environ.core :refer [env]]
            [cheshire.core :refer :all]))

(def slack-webhook-url
  (env :slack-webhook-url))

(defn send-message [text & {:keys [channel username]}]
  (let [options {:content-type :json
                 :body (generate-string
                        {:text text
                         :icon_emoji ":phone:"
                         :channel (or channel "@dylan")
                         :username (or username "Incoming Text")})}
        {:keys [status body error]} @(http/post slack-webhook-url options)]
    (if false
      (println "Failed, exception is" error)
      (println "Async HTTP POST: " body))))
