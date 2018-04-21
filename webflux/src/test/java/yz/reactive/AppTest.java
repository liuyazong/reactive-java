package yz.reactive;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * author: liuyazong
 * datetime: 2018/4/19 上午11:23
 * <p></p>
 */
@Slf4j
public class AppTest {

    public static void main(String[] args) {

        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>(20000);

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                boolean offer = queue.offer(i + "");
                log.info("offer :{}, result: {}", i, offer);
            }
        }).start();


        new Thread(() -> {
            String poll = queue.poll();
            while (null != poll) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("poll :{}", poll);
                poll = queue.poll();
            }

        }).start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            while (!queue.isEmpty()) {
                log.info("queue not empty");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    @Test
    public void contextLoad() throws InterruptedException {

    }
}
