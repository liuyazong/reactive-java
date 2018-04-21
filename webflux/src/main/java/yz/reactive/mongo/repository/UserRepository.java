package yz.reactive.mongo.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import yz.reactive.entity.User;

/**
 * author: liuyazong
 * datetime: 2018/4/19 下午2:05
 * <p></p>
 */
public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
