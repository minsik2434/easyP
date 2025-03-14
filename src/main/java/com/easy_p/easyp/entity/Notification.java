package com.easy_p.easyp.entity;

import com.easy_p.easyp.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
//TODO 알림 별로 상속 객체 사용 고려
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private String type;
    private String content;
    @Setter
    private boolean isRead;

    public Notification(Project project, Member member, String type, String content, boolean isRead){
        this.project = project;
        this.member = member;
        this.type = type;
        this.content = content;
        this.isRead = isRead;
    }
}
