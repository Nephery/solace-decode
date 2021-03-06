package com.example.solace.decode.function;

import com.example.solace.decode.model.User;
import com.example.solace.decode.model.es.ESMessage;
import com.example.solace.decode.repository.MessageJPARepository;
import com.example.solace.decode.repository.UserRepository;
import com.example.solace.decode.repository.es.ESMessageRepository;
import com.solace.spring.cloud.stream.binder.messaging.SolaceHeaders;
import com.solacesystems.jcsmp.Destination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class SaveMessage implements Consumer<Message<com.example.solace.decode.model.Message>> {
	private final MessageJPARepository messageRepository;
	private final ESMessageRepository ESMessageRepository;
	private final UserRepository userRepository;
	private final Pattern channelIdMatcher = Pattern.compile("^channels/(.*?)/");

	@Autowired
	public SaveMessage(MessageJPARepository messageRepository, ESMessageRepository ESMessageRepository, UserRepository userRepository) {
		this.messageRepository = messageRepository;
		this.ESMessageRepository = ESMessageRepository;
		this.userRepository = userRepository;
	}

	@Override
	public void accept(Message<com.example.solace.decode.model.Message> message) {
		Destination destination = message.getHeaders().get(SolaceHeaders.DESTINATION, Destination.class);
		if (destination != null) {
			Matcher match = channelIdMatcher.matcher(destination.getName());
			if (match.find()) {
				Integer channelId = Integer.valueOf(match.group(1));
				com.example.solace.decode.model.Message payload = message.getPayload();
				payload.setChannelId(channelId);
				messageRepository.save(payload);


				               String[] mentionedUserNames = Pattern.compile("@(\\w+)")
						                       .matcher(payload.getText())
						                       .results()
						                       .unordered()
						                       .map(MatchResult::group)
						                       .map(s -> s.substring(1))
						                       .toArray(String[]::new);

						               Set<Integer> mentionedUserIds = new HashSet<>();
				               for (String mentionedUserName : mentionedUserNames) {
					                   mentionedUserIds.addAll(userRepository.findAllByName(mentionedUserName).stream().map(User::getId).collect(Collectors.toSet()));
					               }


				ESMessageRepository.save(new ESMessage(payload, mentionedUserIds.toArray(Integer[]::new)));
			}
		}
	}
}
