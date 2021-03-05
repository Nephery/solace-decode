package com.example.solace.decode.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.JCSMPStreamingPublishEventHandler;
import com.solacesystems.jcsmp.TextMessage;
import com.solacesystems.jcsmp.Topic;
import com.solacesystems.jcsmp.XMLMessageProducer;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class MessagingService {
    private XMLMessageProducer prod;
    private ObjectMapper objectMapper;


    public MessagingService(JCSMPSession session) throws Exception {
        this.objectMapper = new ObjectMapper();
        this.prod = session.getMessageProducer(new JCSMPStreamingPublishEventHandler() {

            @Override
            public void responseReceived(String messageID) {
                System.out.println("Producer received response for msg: " + messageID);
            }

            @Override
            public void handleError(String messageID, JCSMPException e, long timestamp) {
                System.out.printf("Producer received error for msg: %s@%s - %s%n",
                        messageID,timestamp,e);
            }
        });

    }

    public void publish(String topicName, Object content) throws Exception{
        final Topic topic = JCSMPFactory.onlyInstance().createTopic(topicName);
        BytesMessage msg = JCSMPFactory.onlyInstance().createMessage(BytesMessage.class);
        String text = objectMapper.writeValueAsString(content);
        msg.setData(text.getBytes(StandardCharsets.UTF_8));
        prod.send(msg,topic);
    }

}
