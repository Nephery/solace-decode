package com.example.solace.decode.rest;

import com.example.solace.decode.model.Message;
import com.example.solace.decode.model.SearchRequest;
import com.example.solace.decode.model.es.ESMessage;
import com.example.solace.decode.repository.MessageJPARepository;
import com.example.solace.decode.repository.es.ESMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class SearchController {
    @Autowired
    MessageJPARepository messageRepository;

    @Autowired
    ESMessageRepository ESRepo;

    @CrossOrigin
    @PostMapping("search/messages")
    List<Message> searchMessages(@RequestBody SearchRequest request) {
        Iterable<String> ids = ESRepo.customFind(request.getContent(), request.getUserId())
                .stream()
                .map(ESMessage::getId)
                .collect(Collectors.toList());
        Map<String, Message> savedMessages = new HashMap<>();
        messageRepository.findAllById(ids).forEach(m -> savedMessages.put(m.getId(), m));
        List<Message> actualList = new ArrayList<>(savedMessages.size());
        ids.forEach(id -> actualList.add(savedMessages.get(id)));
        return actualList;
    }
}
