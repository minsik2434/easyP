package com.easy_p.easyp.controller;

import com.easy_p.easyp.dto.MemberContext;
import com.easy_p.easyp.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    @DeleteMapping("/notification/{notificationId}")
    public ResponseEntity<Void> delete(@PathVariable("notificationId") Long notificationId,
                                       @AuthenticationPrincipal MemberContext memberContext){
        notificationService.deleteNotification(notificationId, memberContext.getUsername());
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/notification/all")
    public ResponseEntity<Void> deleteAll(@AuthenticationPrincipal MemberContext memberContext){
        notificationService.deleteAllNotification(memberContext.getUsername());
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/notification/isRead/all")
    public ResponseEntity<Void> checkAll(@AuthenticationPrincipal MemberContext memberContext){
        notificationService.checkReadAllNotification(memberContext.getUsername());
        return ResponseEntity.noContent().build();
    }
}
