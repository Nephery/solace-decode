package com.example.solace.decode.model;

import lombok.*;

import javax.persistence.Id;

import javax.persistence.*;

@Data
@Entity
public class Message {
	@Id
	private String id;

 	@Column(nullable=false)
	private Integer channelId;

	@Column(nullable=false)
	private String type;

	@Column(nullable=false)
	private Integer userId;

	@Column(nullable=false)
	private String name;

	@Column(nullable=false, columnDefinition="TEXT")
	private String text;

	@Column(nullable=false)
	private String timestamp;

	private int search_clicks;

	private String sendingUserId;
}
