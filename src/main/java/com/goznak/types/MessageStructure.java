package com.goznak.types;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class MessageStructure implements Iterable<MessagePart>{
    List<MessagePart> partsList = new ArrayList<>();
    public void addPart(MessagePart messagePart){
        partsList.add(messagePart);
    }
    public void removePart(MessagePart messagePart){
        partsList.remove(messagePart);
    }
    @Override
    public Iterator<MessagePart> iterator() {
        return partsList.iterator();
    }
}
