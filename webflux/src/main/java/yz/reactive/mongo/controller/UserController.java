package yz.reactive.mongo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import yz.reactive.entity.User;

/**
 * author: liuyazong
 * datetime: 2018/4/19 下午2:09
 * <p></p>
 */
@RestController
@RequestMapping("/mongo/user")
public class UserController {

    @Autowired
    private ReactiveMongoRepository<User, String> userRepository;

    @PostMapping
    public Flux<User> insert(@RequestBody Flux<User> user) {
        return userRepository.insert(user);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Mono<User>>> findById(@PathVariable("id") String id) {
        return Mono.just(ResponseEntity.ok(userRepository.findById(id)));
    }
}
