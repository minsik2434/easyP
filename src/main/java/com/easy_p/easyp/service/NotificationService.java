package com.easy_p.easyp.service;

public interface NotificationService {
    void checkReadNotification(Long notificationId);
    void deleteNotification(Long notificationId, String email);
    void deleteAllNotification(String email);
    void checkReadAllNotification(String email);
}
