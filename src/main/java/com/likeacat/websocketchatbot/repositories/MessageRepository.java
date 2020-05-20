package com.likeacat.websocketchatbot.repositories;

import com.likeacat.websocketchatbot.entities.Message;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByUserName(String username, Sort sort);
}
