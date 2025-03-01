### Spring Integration 
is a framework within the Spring ecosystem that provides an implementation of the Enterprise Integration Patterns (EIP). It facilitates the integration of different systems through standard enterprise messaging systems and protocols, abstracting away the complexity of dealing with different messaging APIs and protocols.

Spring Integration provides support for both synchronous and asynchronous message processing and can handle data transformation, routing, filtering, and more. It allows you to integrate with various messaging systems like JMS, AMQP, MQTT, and even non-messaging protocols like HTTP, FTP, etc.

## Key Concepts in Spring Integration
### Message: The fundamental data construct which contains the data and headers.
### Channel: The medium through which messages are transferred.
+ DirectChannel: A point-to-point channel that dispatches messages to a single subscriber.
+ PublishSubscribeChannel: broadcast a message to multiple subscribers. It delivers a copy of a message to each of its subscribers, supporting various message handling behaviors like broadcasting events to multiple handlers.
+ QueueChannel: point-to-point channel that stores messages in a queue. It’s useful when you want to buffer messages before the next processor can handle them. This type of channel helps in controlling the workload when the message producer is faster than the consumer.
+ ExecutorChannel: Similar to a PublishSubscribeChannel, but allows for asynchronous processing. Messages sent to an ExecutorChannel are handled by a task executor that you configure, enabling concurrent message processing.
+ PriorityChannel: behaves like a QueueChannel but considers the priority of messages when retrieving them from the channel, based on their headers.
+ RendezvousChannel: synchronous QueueChannel makes the sender block until the receiver accepts the message. It’s useful for synchronization purposes.
+ FluxMessageChannel: from the reactive stack supports reactive programming patterns by using Project Reactor's Flux. It’s effectively a bridge to the reactive world, designed to work with streams of messages.
### Endpoint: The components that interact with the channels like adapters, gateways, and service activators. They help in sending or receiving messages.
### Transformer: Used to transform the message from one format to another.
+ Payload Transforming : Object-to-String, Byte Array to String, JsonToObjectTransformer, ObjectToJsonTransformer, MapToObjectTransformer
+ Header Enricher
+ File Transforming: File to String, File To ByteArray
+ Message
+ ...
### Filter: Used to evaluate whether a message should be passed along or discarded.
+ Expression-Based Filter
+ Payload Type Filter
+ Method Invoking Filter
+ Regex Pattern Matching Filter
+ Message Header Filter
+ Composite Filter
+ Custom Filters
### Router: Routes message to different channels based on a condition.
+ Payload Type Router: Routing messages containing different types of objects to specific handlers
+ Header Value Router: This router uses the value of a specific header to determine the routing destination
+ Expression-Based Router: An Expression Router uses a SpEL (Spring Expression Language) expression to evaluate the routing criteria based on the message context.
+ Method Invoking Router: method of an injected Bean to decide the routing
+ Mapping Message Router: This router maps from a message property (header or payload) to channel names based on a mapping strategy
+ Recipient List Router: can send messages to multiple channels based on a list of recipient channels
### Splitter: Splits a message into multiple messages.
+ Default Splitter: Splits payloads that are inherently iterable, such as lists, arrays, or other iterable types
+ Expression Splitter: Uses a SpEL (Spring Expression Language) expression to define how the message should be split.
+ XPath Splitter
+ JsonPath Splitter
+ File Splitter
+ Custom Splitter
### Aggregator: Aggregates multiple messages into a single message.
+ Correlation Strategy: Determines how messages are correlated (i.e., grouped together). For example, messages could be correlated based on session IDs, certain headers, or parts of the payload.
+ Release Strategy: Dictates when a group of correlated messages should be released as a single aggregated message. For example, you might release a group once all expected pieces of a message have arrived or after a timeout period.
+ Aggregation Algorithm: Defines how the message payloads are combined once they are ready for release. By default, a collection of the aggregated messages is provided, but this can be customized.
+ Message Group Store: The mechanism by which message groups are stored while awaiting completion. This can be in-memory, or for more robust needs, persistent.