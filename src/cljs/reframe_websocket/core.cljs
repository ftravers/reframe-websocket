(ns reframe-websocket.core
  (:require [re-frame.core :as reframe]
            [transit-websocket-client.core :as websocket]))

(defn async-websocket [ws-url]
  (websocket/async-websocket ws-url))

(reframe/reg-event-db
 :set
 (fn [db [_ keys value]]
   (common/log keys value)
   (assoc-in db keys value)))

(reframe/reg-sub
 :get
 (fn [db [_ keys]]
   (get-in db keys)))

(defn send-msg [msg aws]
  (common/log "Calling Send Message with Value: " (str msg))
  (go (>! aws msg)))

(defn get-msg [store-fn aws]
  "Call store-fn with the result of reading a string off the
  websocket"
  (go
    (store-fn (reader/read-string (<! aws)))))

(defn send-server [msg]
  (websocket/send-msg msg)
  (websocket/get-msg
   (fn [resp]
     (common/log "Server Resp: " resp)
     (let [store-path (:path resp)
           store-val (:value resp)]
       (common/log store-val "->" store-path)
       (reframe/dispatch [:set store-path store-val])))))
