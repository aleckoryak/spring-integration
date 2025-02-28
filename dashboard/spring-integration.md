


## DirectChannel one-to-one synchronously
In Spring Integration, the DirectChannel is a type of message channel that synchronously invokes the subscribed handler within the sender's thread. It is commonly used in scenarios where you have simple message processing requirements and don't need to offload the workload to a separate thread.
+ Immediate Processing: Use a DirectChannel when you want the message handling to occur immediately and within the same thread that is publishing the message. This is typically suitable for lightweight, non-blocking operations.
+ Simple Workflow: It's particularly useful in simple integration flows where you don’t need complex routing or asynchronous processing. DirectChannel keeps the architecture straightforward.
+ Transactional Contexts: When you need the message consumption to be part of the same transactional context as the message production. Using a DirectChannel ensures that if a transaction is present, the subscriber will participate in that transaction.
1. xml configuration
```xml
<!--...-->
<int:channel id="techSupportDirectChannel"/>
```
2. sender
```java
@DependsOn({"viewService", "techSupportDirectChannel"})
...
@Autowired
private final AbstractSubscribableChannel techSupportDirectChannel;
@Autowired
private ViewService viewService;
...
techSupportDirectChannel.send(message);
```
2. subscriber
```java
class implements MessageHandler

@Autowired
private final AbstractSubscribableChannel techSupportDirectChannel;

@PostConstruct
public void init() {
    techSupportDirectChannel.subscribe(this);
}
@Override
public void handleMessage(Message<?> message) throws MessagingException {
    ...
}

```