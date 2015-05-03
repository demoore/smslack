(ns smslack.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [smslack.twilio :as twilio]
            [smslack.slack :as slack]))

(defn text [body]
  (let [text-vector (twilio/split-number-and-message body)]
    (twilio/send-text (nth text-vector 0) (nth text-vector 1))))

(defn text-to-slack [request]
  (let [message (:Body (:params request))
        from    (:From (:params request))]
    (slack/send-message message :username from)))

(defn respond-to-slack [request]
  (let [message (:text (:params request))
        from    (str "Sending from " (:user_name (:params request)))]
    (slack/send-message message :username from)))

(defn handle-slack [request]
  (text (:text (:params request)))
  (respond-to-slack request))

(defapi slack-routes
  (ring.swagger.ui/swagger-ui
   "/swagger-ui")
  (swagger-docs
   {:info {:title "Sample api"}})
  (context* "/api" []
            (POST* "/slack/message" request
                   :summary "Sends a text message"
                   (ok (handle-slack request)))))

(defapi twilio-routes
  (ring.swagger.ui/swagger-ui
   "/swagger-ui")
  (swagger-docs
   {:info {:title "Sample api"}})
  (context* "/api" []
            (POST* "/twilio/message" request
                   :summary "This is where text messages get POSTed to"
                   (ok (text-to-slack request)))))
