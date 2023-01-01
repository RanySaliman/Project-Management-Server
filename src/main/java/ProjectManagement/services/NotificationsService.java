package ProjectManagement.services;

import ProjectManagement.entities.Board;
import ProjectManagement.entities.User;
import ProjectManagement.entities.UserInBoard;
import ProjectManagement.entities.enums.Events;
import ProjectManagement.entities.enums.NotificationMethod;
import ProjectManagement.repositories.UserRepository;
import ProjectManagement.utils.EmailSender;
import ProjectManagement.utils.SocketsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationsService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private SocketsUtil socketsUtil;

    public void notificationHappened(Object o, Events event) {
        emailSender.sendEmail(userRepo.findAll().stream().map(User::getEmail).collect(Collectors.toList()), "Notification", event.name() + "\n" + o.toString());
    }

    public void notificationHappenedOnBoard(Object o, Board board, Events event) {
        List<String> emailRecipients = board.getUsers().stream().
                filter(userInBoard -> userInBoard.getNotificationMethods().contains(NotificationMethod.EMAIL)).
                map(UserInBoard::getUser).map(User::getEmail).collect(Collectors.toList());
        if (emailRecipients.size() > 0) {
            emailSender.sendEmail(emailRecipients, "Notification", event.name() + "\n" + o.toString());
        }
    }

    public void notifyAllUsers(List<User> user, Events event, Object o) {
        List<String> emailRecipients = user.stream().map(User::getEmail).collect(Collectors.toList());
        if (emailRecipients.size() > 0) {
            emailSender.sendEmail(emailRecipients, "Notification", event.name() + "\n" + o.toString());
        }
    }

    public void popNotificationHappenedOnBoard(Object o, Board board, Events event) {
        List<Integer> popUpRecipients = board.getUsers().stream().
                filter(userInBoard -> userInBoard.getNotificationMethods().contains(NotificationMethod.POPUP)).
                map(UserInBoard::getUser).map(User::getId).collect(Collectors.toList());
        if (popUpRecipients.size() > 0) {
            for (Integer id : popUpRecipients) {
                socketsUtil.popupNotification(id, event.name());
            }
        }
    }
}
