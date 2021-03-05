package com.example.solace.decode.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.lang.String;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Channel {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String type = "channel";

    private String channelType;

    private String time;

    @Column(columnDefinition="TEXT")
    private String description;
}
