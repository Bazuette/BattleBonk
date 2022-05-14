package libayon.notifications;

import libayon.utils.timers.Timer;

public class Notification {
    private final String msg;
    private final Timer timer = new Timer();

    public Notification(String s) {
        this.msg = s;
        timer.reset();
        if (!NotificationManager.getNotificationList().contains(this))
            NotificationManager.getNotificationList().add(this);
    }

    public String getMsg() {
        return msg;
    }

    public Timer getTimer() {
        return timer;
    }
}
