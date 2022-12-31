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
     * Publishes the given message to the specified public topic.
     *
     * @param message the message to publish
     * @param topic   the name of the topic to publish to
     * @param <T>     the type of the message
     * @return the published message
     */
    public <T> T writeMessageToPublicTopic(T message, String topic) {
        template.convertAndSend("/topic/" + topic, message);
        return message;
    }

    /**
     * Sends a message to the specified private channel for the specified user.
     *
     * @param message  The message to be sent.
     * @param receiver The username of the user to receive the message.
     * @return The message that was sent.
     */
    public <T> T writeMessageToPrivateChannel(T message, String receiver) {
        template.convertAndSendToUser(receiver, "/", message);
        return message;
    }

    /**
     * Sends a notification event to the board notification channel.
     *
     * @param id        The ID of the board which his users will receive the notification.
     * @param eventName The name of the notification event.
     */
    public void popupNotification(int id, String eventName) {
        template.convertAndSend("/topic/notification/" + id, eventName);
    }

}

