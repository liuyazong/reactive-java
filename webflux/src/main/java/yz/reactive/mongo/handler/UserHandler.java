package yz.reactive.mongo.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import yz.reactive.entity.User;


/**
 * author: liuyazong
 * datetime: 2018/4/19 上午11:44
 * <p></p>
 */
@Service
public class UserHandler {

    @Autowired
    private ReactiveMongoRepository<User, String> userRepository;

    public Mono<ServerResponse> insert(Flux<User> users) {
        return ServerResponse.ok().body(userRepository.insert(users), User.class);
    }

    public Mono<ServerResponse> findById(String id) {
        return ServerResponse.ok().body(userRepository.findById(id), User.class);
    }
}
