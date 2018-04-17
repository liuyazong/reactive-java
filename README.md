# reactive programming

响应式编程是一个关注数据流和变化传播的异步编程范式。它使得通过所使用的编程语言来轻松的描述静态(e.g. array)和动态(e.g. event emitters)数据流成为可能。

[Reactive Streams Specification](http://www.reactive-streams.org/)定义了Java 响应式编程的接口规范。这个规范已被整合进Java 9 (Flow类)。

在面向对象的编程语言中，响应式编程经常被做为观察者模式的扩展。

也有人拿响应式流与迭代器模式进行对比，因为在它们的类库中都有一个Iterable-Iterator对。它们最大的不同在于，迭代器采用的是pull模式，而响应式流采用的是push模式。

迭代器是命令式编程模式，由开发者决定何时调用next()方法来获取下一个元素；而响应式编程中国，与Iterable-Iterator对等价的是Publisher-Subscriber对，由Publisher通知Subscriber有新的元素要处理。

这个规范定义了如下接口：

* org.reactivestreams.Publisher: 无界元素序列的提供者，根据Subscriber发布这些元素。一个Publisher可以服务于多个Subscriber

* org.reactivestreams.Subscriber: 无界元素序列的消费者

* org.reactivestreams.Subscription: 一个订阅到Publisher的Subscriber的一对一的生命周期

* org.reactivestreams.Processor: Publisher和Subscriber都要尊从的契约

Publisher/Subscriber: 1/N

Subscriber/Publisher: 1/1

Subscription/Subscriber: 1/1

## Reactor

[Reactor](http://projectreactor.io/)是一个在JVM平台上构建非阻塞应用程序（non-blocking application）的[Reactive Streams Specification](http://www.reactive-streams.org/)的实现。

它是Spring WebFlux的Reactor默认实现，其对外提供的主要类有Flux和Mono：

Flux: public abstract class Flux<T> implements Publisher<T>
Mono: public abstract class Mono<T> implements Publisher<T>


### 构建

最简单的构建方式：

    //从数组构建
    Flux.just("0", "1", "2", "3")
            .subscribe(s -> log.info("Flux.just: {}", s));
    
    Flux.fromArray(new String[]{"0", "1", "2", "3"})
            .subscribe(s -> log.info("Flux.fromArray: {}", s));
    
    List<String> strings = Arrays.asList("0", "1", "2", "3");
    
    //从可迭代对象构建，如ArrayList
    Flux.fromIterable(strings)
            .subscribe(s -> log.info("Flux.fromIterable: {}", s));
    
    //从Java 8 的Stream构建
    Flux.fromStream(strings.stream())
            .subscribe(s -> log.info("Flux.fromStream: {}", s));
    
    //构建至多一个元素的流
    Mono.just("0")
            .subscribe(s -> log.info("Mono.just: {}", s));
    String value = "0";
    
    Mono.justOrEmpty(Optional.ofNullable(value))
            .subscribe(s -> log.info("Mono.justOrEmpty: {}", s));
            
构建一个数字序列：

    //构建一个排列
    Flux.range(0, 4)
            .subscribe(i -> log.info("Flux.range: {}", i));
    
    //这个for循环实现了上面代码的功能
    for (int i = 0; i < 4; i++) {
        log.info("for: {}", i);
    }

### Scheduler

为序列分配线程执行，而不是在调用者线程中执行。

Reactor提供了如下调度器：

* Schedulers.immediate(): 在当前线程执行
* Schedulers.single(): 可重用的单线程
* Schedulers.newSingle(): 每次调用都创建一个新线程
* Schedulers.elastic(): 在需要时创建线程池，重用空闲的线程，适用于I/O等阻塞式工作，类似Executors.newCachedThreadPool()
* Schedulers.parallel(): 固定大小（CPU核心数）的线程池

    
    //为每个Flux分配一个线程
    Flux.range(0, 10)
            .publishOn(Schedulers.parallel())
            .subscribe(i -> log.info("Flux.range: {}", i));
    
    Flux.range(10, 10)
            .subscribeOn(Schedulers.parallel())
            .subscribe(i -> log.info("Flux.range: {}", i));

publishOn/subscribeOn: 都会使你的操作在单独的线程中执行 

### ParallelFlux

parallel(int)和runOn(Scheduler)方法帮助你真正的进行异步处理：
parallel(int)方法返回一个ParallelFlux实例，runOn(Scheduler)方法告诉ParallelFlux实例使用哪个Scheduler来执行任务。

    Flux.range(0, 100).
            parallel(4)
            .runOn(Schedulers.parallel())
            .subscribe(i -> log.info("Flux.range: {}", i));

### ConnectableFlux

通过ConnectableFlux，可以向多个Subscriber发送广播


publish()-->connect():

    ConnectableFlux<Integer> flux = Flux.range(0, 4)
                    .doOnSubscribe(subscription -> log.info("doOnSubscribe: {}", subscription))
                    .publish();
    
    flux.subscribe(i -> log.info("subscribe 1: {}", i));
    flux.subscribe(i -> log.info("subscribe 2: {}", i));
    
    flux.connect();

publish()-->autoConnect():

    Flux<Integer> flux = Flux.range(0, 4)
            .doOnSubscribe(subscription -> log.info("doOnSubscribe: {}", subscription))
            .publish().autoConnect(2);

    flux.subscribe(i -> log.info("subscribe 1: {}", i));
    flux.subscribe(i -> log.info("subscribe 2: {}", i));

## RxJava

[RxJava](https://github.com/ReactiveX/RxJava)是另一个[Reactive Streams Specification](http://www.reactive-streams.org/)的实现。