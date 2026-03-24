package com.hospital.data;

import com.google.gson.reflect.TypeToken;
import com.hospital.model.Notification;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public class NotificationRepository extends JsonRepository<Notification> {
    private static final String FILE_PATH = "data/notifications.json";
    private static final Type LIST_TYPE = new TypeToken<List<Notification>>(){}.getType();

    public NotificationRepository() {
        super(FILE_PATH, LIST_TYPE);
    }

    @Override
    protected String getId(Notification entity) {
        return entity.getNotificationId();
    }

    public List<Notification> findByRecipientId(String recipientUserId) {
        return findAll().stream()
                // Use Objects.equals to avoid NPE if recipientUserId is null
                .filter(notif -> Objects.equals(notif.getRecipientUserId(), recipientUserId))
                .toList();
    }

    public List<Notification> findUnreadByRecipientId(String recipientUserId) {
        return findAll().stream()
                .filter(notif -> Objects.equals(notif.getRecipientUserId(), recipientUserId)
                        && !notif.isRead())
                .toList();
    }
}

