package yz.reactive.redis.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveStringCommands;
import org.springframework.data.redis.core.ReactiveRedisCallback;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import yz.reactive.entity.User;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

/**
 * author: liuyazong
 * datetime: 2018/4/21 上午10:27
 * <p></p>
 */
@Service("redisUserHandler")
public class UserHandler {

    public static final AtomicLong id = new AtomicLong(0);

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ReactiveRedisTemplate redisTemplate;

    public Mono<ServerResponse> insert(Flux<User> users) {
        Flux execute = redisTemplate.execute(connection -> {
            return connection.stringCommands()
                    .set(
                            users.flatMap(user -> {
                                try {
                                    long l = id.incrementAndGet();
                                    user.setId(l + "");
                                    ReactiveStringCommands.SetCommand setCommand =
                                            ReactiveStringCommands.SetCommand
                                                    .set(ByteBuffer.wrap(String.valueOf(l).getBytes()))
                                                    .value(ByteBuffer.wrap(objectMapper.writeValueAsBytes(user)));
                                    return Mono.just(setCommand);
                                } catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                    return null;
                                }

                            }).filter(Objects::nonNull)
                    );
        });
        return ServerResponse.ok().body(execute, User.class);
    }

    @SuppressWarnings("unchecked")
    public Mono<ServerResponse> findById(String id) {

        Flux<User> flux = redisTemplate
                .execute((ReactiveRedisCallback<ByteBuffer>) connection -> {
                    return connection.stringCommands().get(ByteBuffer.wrap(id.getBytes()));
                })
                .flatMap((Function<ByteBuffer, Flux<User>>) byteBuffer -> {
                    try {
                        User user = objectMapper.readValue(byteBuffer.array(), User.class);
                        return Flux.just(user);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).filter(Objects::nonNull);
        return ServerResponse.ok().body(flux, User.class);
    }

}
