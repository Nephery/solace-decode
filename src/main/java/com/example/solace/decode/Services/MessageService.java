package com.example.solace.decode.Services;

import com.example.solace.decode.model.Message;
import com.example.solace.decode.repository.MessageJPARepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageJPARepository messageJPARepository;

    public MessageService(MessageJPARepository messageJPARepository) {
        this.messageJPARepository = messageJPARepository;
    }

    public List<Message> getChannelMessages(Integer channelId) {
        return this.messageJPARepository.findByChannelId(channelId);
    }


}


