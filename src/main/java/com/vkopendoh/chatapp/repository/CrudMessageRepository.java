package com.vkopendoh.chatapp.repository;

import com.vkopendoh.chatapp.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrudMessageRepository extends JpaRepository<Message,Integer> {
}