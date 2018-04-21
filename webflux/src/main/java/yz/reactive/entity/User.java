package yz.reactive.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * author: liuyazong
 * datetime: 2018/4/19 上午11:43
 * <p></p>
 */
@Data
@Document
public class User {
    private String id;
    private String mobile;
    private String password;
    private LocalDateTime updateTime = LocalDateTime.now();
}
