package com.example.solace.decode.repository;

import com.example.solace.decode.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageJPARepository extends JpaRepository<Message, String> {
    List<Message> findByChannelId(Integer channelId); //will need to sort here unless frontend wants to do it
}
