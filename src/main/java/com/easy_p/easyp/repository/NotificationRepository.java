package com.easy_p.easyp.repository;

import com.easy_p.easyp.entity.Notification;
import com.easy_p.easyp.repository.querydsl.NotificationQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationQueryRepository {
    @Query("SELECT COUNT(n) FROM Notification n where n.member.email =:email AND isRead = false")
    Long countNoReadNotificationByMemberEmail(@Param("email") String email);

    @Modifying
    @Query("DELETE FROM Notification n WHERE n.member.email =:email")
    void deleteAllByMemberEmail(@Param("email") String email);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.member.email =:email")
    void updateAllIsReadTrueByMemberEmail(@Param("email") String email);
}
