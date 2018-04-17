package yz.reactive;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * author: liuyazong
 * datetime: 2018/4/16 下午9:02
 * <p></p>
 */
@Slf4j
public class AppTest {
    @Test
    public void test0() {
        String[] items = {"0", "1", "2", "3"};
        List<String> strings = Arrays.asList("0", "1", "2", "3");

        Flowable.range(0, 4)
                .subscribe(s -> log.info("Flowable.range: {}", s));

        Flowable.just("0", "1", "2", "3")
                .subscribe(s -> log.info("Flowable.just: {}", s));


        Flowable.fromArray(items)
                .subscribe(s -> log.info("Flowable.fromArray: {}", s));


        Flowable.fromIterable(strings)
                .subscribe(s -> log.info("Flowable.fromIterable: {}", s));

        Observable.range(0, 4)
                .subscribe(s -> log.info("Observable.range: {}", s));

        Observable.just("0", "1", "2", "3")
                .subscribe(s -> log.info("Observable.just: {}", s));


        Observable.fromArray(items)
                .subscribe(s -> log.info("Observable.fromArray: {}", s));

        Observable.fromIterable(strings)
                .subscribe(s -> log.info("Observable.fromIterable: {}", s));

        Single.just("a")
                .subscribe(s -> log.info("Single.just: {}", s));

        Maybe.just("a")
                .subscribe(s -> log.info("Maybe.just: {}", s));
    }

    @Test
    public void test1() throws InterruptedException {
        Flowable.range(0, 10)
                .subscribeOn(Schedulers.computation())
                .subscribe(s -> log.info("Schedulers.computation: {}", s));

        Flowable.range(10, 10)
                .observeOn(Schedulers.computation())
                .subscribe(s -> log.info("Schedulers.computation: {}", s));

        Thread.sleep(5000);
    }

    @Test
    public void test2() throws InterruptedException {
        Flowable.range(0, 10)
                .flatMap(v -> Flowable.just(v)
                        .subscribeOn(Schedulers.computation())
                        .map(w -> {
                            Integer r = w * w;
                            log.info("parallel processing: {} * {} = {}", w, w, r);
                            return r;
                        }))
                .subscribe(r -> log.info("parallel processing: {}", r));


        Thread.sleep(5000);
    }


    @Test
    public void test3() throws InterruptedException {
        Flowable.range(0, 10)
                .parallel(4)
                .runOn(Schedulers.computation())
                .map(w -> {
                    Integer r = w * w;
                    log.info("parallel processing: {} * {} = {}", w, w, r);
                    return r;
                })
                .sequential()
                .subscribe(r -> log.info("parallel processing: {}", r));

        Thread.sleep(5000);

    }
}
