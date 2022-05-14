package libayon.notifications;

import libayon.event.EventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationManager extends EventListener {
    public static List<Notification> notificationList = new ArrayList<>();

    public static void createNotification(String msg) {
        Notification s = new Notification(msg);
    }

    public static List<Notification> getNotificationList() {
        return notificationList;
    }

}
