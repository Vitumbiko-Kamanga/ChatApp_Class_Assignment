package org.example.app1;

import org.example.chatapp.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageStore <T extends Message> {
    List<T> messageStore;

    public MessageStore() {
        this.messageStore = new ArrayList<>();
    }
    public synchronized List<T> getAllMessages(){
        return this.messageStore.stream().sorted(Message::compareTo).toList();
    }

    public synchronized List<T> getAllMessagesFromStartDate(String datetime){
        LocalDateTime time = LocalDateTime.parse(datetime);
        return messageStore.stream().filter((T t) -> t.getDate().compareTo(time) > 0).sorted(Message::compareTo).toList();
    }

    public synchronized void addMessage(T message){
        this.messageStore.add(message);
    }
}
