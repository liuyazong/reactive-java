package yz.reactive;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;

/**
 * author: liuyazong
 * datetime: 2018/4/16 下午5:08
 * <p></p>
 */
@Slf4j
public class AppTest {

    @Test
    public void test0() {

        Flux.range(0, 4)
                .subscribe(i -> log.info("Flux.range: {}", i));

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
    }

    @Test
    public void test1() throws InterruptedException {

        //为每个Flux分配一个线程
        Flux.range(0, 10)
                .publishOn(Schedulers.parallel())
                .subscribe(i -> log.info("Flux.range: {}", i));

        Flux.range(10, 10)
                .subscribeOn(Schedulers.parallel())
                .subscribe(i -> log.info("Flux.range: {}", i));

        Thread.sleep(5000);
    }

    @Test
    public void test2() throws InterruptedException {
        Flux.range(0, 100).
                parallel(4)
                .runOn(Schedulers.parallel())
                .subscribe(i -> log.info("Flux.range: {}", i));

        Thread.sleep(5000);
    }

    @Test
    public void test3() {
        ConnectableFlux<Integer> flux = Flux.range(0, 4)
                .doOnSubscribe(subscription -> log.info("doOnSubscribe: {}", subscription))
                .publish();

        flux.subscribe(i -> log.info("subscribe 1: {}", i));
        flux.subscribe(i -> log.info("subscribe 2: {}", i));

        flux.connect();
    }

    @Test
    public void test4() {
        Flux<Integer> flux = Flux.range(0, 4)
                .doOnSubscribe(subscription -> log.info("doOnSubscribe: {}", subscription))
                .publish().autoConnect(2);

        flux.subscribe(i -> log.info("subscribe 1: {}", i));
        flux.subscribe(i -> log.info("subscribe 2: {}", i));
    }

    @Test
    public void test5(){
        //构建一个排列
        Flux.range(0, 4)
                .subscribe(i -> log.info("Flux.range: {}", i));

        //这个for循环实现了上面代码的功能
        for (int i = 0; i < 4; i++) {
            log.info("for: {}", i);
        }
    }

}
