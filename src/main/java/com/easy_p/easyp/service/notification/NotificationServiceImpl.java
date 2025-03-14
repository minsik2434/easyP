package com.easy_p.easyp.service.notification;

import com.easy_p.easyp.common.exception.NotFoundException;
import com.easy_p.easyp.common.exception.PermissionException;
import com.easy_p.easyp.entity.Notification;
import com.easy_p.easyp.repository.NotificationRepository;
import com.easy_p.easyp.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public void checkReadNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new NotFoundException("NotFound"));
        notification.setRead(true);
    }

    @Override
    @Transactional
    public void deleteNotification(Long notificationId, String email) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new NotFoundException("Not Found"));
        if(!notification.getMember().getEmail().equals(email)){
            throw new PermissionException("Permission Denied");
        }
        notificationRepository.delete(notification);
    }

    @Override
    @Transactional
    public void deleteAllNotification(String email) {
        notificationRepository.deleteAllByMemberEmail(email);
    }

    @Override
    @Transactional
    public void checkReadAllNotification(String email) {
        notificationRepository.updateAllIsReadTrueByMemberEmail(email);
    }

}
