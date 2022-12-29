package ProjectManagement.utils;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class SocketsUtil {
    private final SimpMessagingTemplate template;

    public SocketsUtil(SimpMessagingTemplate template) {
        this.template = template;
    }

    /**
     * Sends message to Main Chat Room.
     *
     * @param message - OutputMessage object, contains: text message to be sent, sender, receiver and timestamp
     * @return OutputMessage object, the message that was sent.
     */
    public <T> T writeMessageToPublicTopic(T message, String topic) {
        template.convertAndSend("/topic/"+topic, message);
        return message;
    }

    /**
     * Sends message to private chat channel.
     *
     * @param message - OutputMessage object, contains: text message to be sent, sender, receiver and timestamp
     * @return OutputMessage object, the message that was sent.
     */
    public <T> T writeMessageToPrivateChannel(T message, String receiver) {
        template.convertAndSendToUser(receiver, "/", message);
        return  message;
    }

    public void popupNotification(int id, String eventName) {
        template.convertAndSend("/topic/notification/" + id, eventName);
    }

}

