(ns reframe-websocket.core
  (:require [re-frame.core :as reframe]
            [cljs.reader :as reader]
            [transit-websocket-client.core :as websocket]
            [cljs.core.async :refer [<! >!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defn async-websocket [ws-url]
  (websocket/async-websocket ws-url))

(reframe/reg-event-db
 :set
 (fn [db [_ keys value]]
   (assoc-in db keys value)))

(reframe/reg-sub
 :get
 (fn [db [_ keys]]
   (get-in db keys)))

(defn send-msg [msg store-path aws]
  "Send msg to server, storing the response in store-path."
  (go (>! aws msg)
      (reframe/dispatch [:set store-path (reader/read-string (<! aws))])))

(comment
  (let [my-message {:my-message "blah" :some-param 12345}
        my-store-location [:store :path]
        my-aws (async-websocket "ws://localhost:7890")]
    (send-msg my-message my-store-location my-aws)))
