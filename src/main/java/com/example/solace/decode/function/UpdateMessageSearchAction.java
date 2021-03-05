package com.example.solace.decode.function;

import com.example.solace.decode.model.es.ESMessage;
import com.example.solace.decode.repository.MessageJPARepository;
import com.example.solace.decode.repository.es.ESMessageRepository;
import com.solace.spring.cloud.stream.binder.messaging.SolaceHeaders;
import com.solacesystems.jcsmp.Destination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UpdateMessageSearchAction implements Consumer<Message<?>> {
	private final MessageJPARepository messageRepository;
	private final com.example.solace.decode.repository.es.ESMessageRepository ESMessageRepository;
	private final Pattern pattern = Pattern.compile("^messages/(.*?)/actions/(.*?)$");

	@Autowired
	public UpdateMessageSearchAction(MessageJPARepository messageRepository, ESMessageRepository ESMessageRepository) {
		this.messageRepository = messageRepository;
		this.ESMessageRepository = ESMessageRepository;
	}

	@Override
	public void accept(Message<?> message) {
		Destination destination = message.getHeaders().get(SolaceHeaders.DESTINATION, Destination.class);
		if (destination == null) {
			return;
		}

		Matcher match = pattern.matcher(destination.getName());
		if (!match.find()) {
			return;
		}

		String msgId = match.group(1);
		String action = match.group(2);

		com.example.solace.decode.model.Message saved = messageRepository.findById(msgId).orElse(null);
		if (saved == null) {
			return;
		}

		ESMessage savedES = ESMessageRepository.findById(msgId).orElse(null);
		if (savedES == null) {
			return;
		}

		switch (action) {
			case "click":
				int searchClicks = saved.getSearch_clicks() + 1;
				saved.setSearch_clicks(searchClicks);
				savedES.setSearch_clicks(searchClicks);
				break;
			default:
				throw new MessagingException(action + " is not a valid action");
		}

		messageRepository.save(saved);
		ESMessageRepository.save(savedES);
	}
}
