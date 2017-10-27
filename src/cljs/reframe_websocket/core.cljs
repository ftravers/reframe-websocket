(ns reframe-websocket.core
  (:require [re-frame.core :as reframe]
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

(defn send-msg [msg store-location aws]
  (go (>! aws msg)
      (reframe/dispatch [:set store-path (reader/read-string (<! aws))])))
