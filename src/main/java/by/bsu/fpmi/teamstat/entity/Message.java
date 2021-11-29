package by.bsu.fpmi.teamstat.entity;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class Message {
    private final String id;
    private final String advice;

    public Message(String advice) {
        this.advice = advice;
        id = UUID.randomUUID().toString();
    }

    public Message(List<String> advices){
        this(String.join("\n", advices));
    }
}
