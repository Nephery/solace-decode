package com.example.solace.decode.model.es;

import com.example.solace.decode.model.Message;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "messages")
@ToString
@NoArgsConstructor
@Getter
@Setter
public class ESMessage {
	@Id
	@Field(type = FieldType.Keyword)
	private String id;

	@Field(type = FieldType.Keyword)
	private Integer channelId;

	@Field(type = FieldType.Text, analyzer = "english")
	private String payload;

	@Field(type = FieldType.Rank_Feature)
	private double search_clicks;

	@Field(type = FieldType.Keyword)
	private String sendingUserId;

	private Integer[] mentionedUserIds;

	public ESMessage(Message message, Integer[] mentionedUserIds) {
		this.id = message.getId();
		this.channelId = message.getChannelId();
		this.payload = message.getText();
		this.search_clicks = 0.00000000000000000001; // Set it to a tiny number because rank features must be > 1
		this.sendingUserId = message.getSendingUserId();
		this.mentionedUserIds = mentionedUserIds;
	}
}
