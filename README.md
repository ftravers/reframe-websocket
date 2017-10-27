<div id="table-of-contents">
<h2>Table of Contents</h2>
<div id="text-table-of-contents">
<ul>
<li><a href="#sec-1">1. Setup</a></li>
<li><a href="#sec-2">2. Reframe :set and :get event/subscription registration</a></li>
<li><a href="#sec-3">3. Send/Recv to Server</a>
<ul>
<li><a href="#sec-3-1">3.1. Define your endpoint</a></li>
<li><a href="#sec-3-2">3.2. Send to Server</a></li>
</ul>
</li>
</ul>
</div>
</div>

# Setup<a id="sec-1" name="sec-1"></a>

Add to project:

![](https://clojars.org/fentontravers/reframe-websocket/latest-version.svg)

```clojure
    (ns ...
      (:require [reframe-websocket.core :as reframe-websocket]))

```

# Reframe :set and :get event/subscription registration<a id="sec-2" name="sec-2"></a>

This will create an event handler called `:get` and a subscription
handler called `:set` to be used like:

```clojure
    (reframe/dispatch-sync [:set [:some :path] "abc123"])
    ;; sets the path [:some :path] to value "abc123" in the app-db
    @(reframe/subscribe [:get [:some :path]])
    ;; => "abc123"

```

# Send/Recv to Server<a id="sec-3" name="sec-3"></a>

## Define your endpoint<a id="sec-3-1" name="sec-3-1"></a>

```clojure
    (def my-aws (reframe-websocket/async-websocket "ws://localhost:7890"))

```

## Send to Server<a id="sec-3-2" name="sec-3-2"></a>

```clojure
    ;; Send a message, specify where to store the response
    (reframe-websocket/send-msg "your message" [:store :path] my-aws)        
    
    ;; retrieve the response
    @(reframe/subscribe [:get [:store :path]])

```
