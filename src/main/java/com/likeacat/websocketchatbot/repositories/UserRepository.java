package com.likeacat.websocketchatbot.repositories;

import com.likeacat.websocketchatbot.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByName(String name);
}
