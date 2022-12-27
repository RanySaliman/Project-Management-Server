package ProjectManagement.entities.enums;

public enum NotificationMethod {
    EMAIL_POPUP,
    EMAIL,
    POPUP,
    NONE;
    public static boolean NotifyEmail(NotificationMethod notificationMethod){
        return notificationMethod == EMAIL || notificationMethod == EMAIL_POPUP;
    }
}

