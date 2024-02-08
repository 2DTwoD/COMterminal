package com.goznak.types;

import com.goznak.visualization.MessagePartPanel;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.*;

@Component
public class MessageStructure implements Iterable<MessagePart>{
    List<MessagePart> partsList = new ArrayList<>();
    public void addPart(MessagePart messagePart){
        partsList.add(messagePart);
    }
    public void addPartBefore(MessagePart currentPart, MessagePart newPart){
        partsList.add(partsList.indexOf(currentPart), newPart);
    }
    public void removePart(MessagePart part){
        partsList.remove(part);
    }
    @Override
    public Iterator<MessagePart> iterator() {
        return partsList.iterator();
    }
}
