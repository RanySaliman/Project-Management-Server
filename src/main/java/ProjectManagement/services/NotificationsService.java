package ProjectManagement.services;

import ProjectManagement.entities.User;
import ProjectManagement.entities.enums.Events;
import ProjectManagement.repositories.UserRepository;
import ProjectManagement.utils.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
@Service
public class NotificationsService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private EmailSender emailSender;
    public void notificationHappened(Object o, Events event) {
        emailSender.sendEmail(userRepo.findAll().stream().map(User::getEmail).collect(Collectors.toList()), "Notification", event.name()+"\n"+o.toString());
    }


}
