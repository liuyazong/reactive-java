package yz.reactive;

import io.reactivex.*;
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

        Flowable.range(0,4)
                .subscribe(s -> log.info("Flowable.range: {}",s));

        Flowable.just("0", "1", "2", "3")
                .subscribe(s -> log.info("Flowable.just: {}",s));


        Flowable.fromArray(items)
                .subscribe(s -> log.info("Flowable.fromArray: {}",s));


        Flowable.fromIterable(strings)
                .subscribe(s -> log.info("Flowable.fromIterable: {}",s));

        Observable.range(0,4)
                .subscribe(s -> log.info("Observable.range: {}",s));

        Observable.just("0", "1", "2", "3")
                .subscribe(s -> log.info("Observable.just: {}",s));


        Observable.fromArray(items)
                .subscribe(s -> log.info("Observable.fromArray: {}",s));

        Observable.fromIterable(strings)
                .subscribe(s -> log.info("Observable.fromIterable: {}",s));

        Single.just("a")
                .subscribe(s -> log.info("Single.just: {}",s));

        Maybe.just("a")
                .subscribe(s -> log.info("Maybe.just: {}",s));
    }

    @Test
    public void test1() throws InterruptedException {
        Flowable.range(0,10)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe(s -> log.info("Schedulers.newThread: {}",s));

        Flowable.range(10,10)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe(s -> log.info("Schedulers.newThread: {}",s));

        Flowable.range(10,10)
                .subscribeOn(Schedulers.computation())
                .subscribe(s -> log.info("Schedulers.computation: {}",s));

        Flowable.range(20,10)
                .subscribeOn(Schedulers.io())
                .subscribe(s -> log.info("Schedulers.io: {}",s));

        Flowable.range(30,10)
                .subscribeOn(Schedulers.single())
                .subscribe(s -> log.info("Schedulers.single: {}",s));

        Thread.sleep(5000);
    }
}
