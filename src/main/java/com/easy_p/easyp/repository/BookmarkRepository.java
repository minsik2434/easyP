package com.easy_p.easyp.repository;

import com.easy_p.easyp.entity.Bookmark;
import com.easy_p.easyp.entity.Member;
import com.easy_p.easyp.entity.Project;
import com.easy_p.easyp.repository.querydsl.BookmarkQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> , BookmarkQueryRepository {
    @Query("SELECT COUNT(bm) FROM Bookmark bm JOIN bm.member m WHERE m.email = :email")
    Long countMemberBookmarkProject(@Param("email") String email);

    Optional<Bookmark> findByMemberAndProject(Member member, Project project);

    @Modifying
    @Query("UPDATE Bookmark bm " +
            "SET bm.sequence = bm.sequence - 1 " +
            "WHERE bm.sequence > :currentSequence " +
            "AND bm.sequence <= :changeSequence " +
            "AND bm.member.email = :email")
    void decreaseBookmarkSequence(@Param("email") String email ,
                                  @Param("currentSequence") Integer currentSequence,
                                  @Param("changeSequence") Integer changeSequence);

    @Modifying
    @Query("UPDATE Bookmark bm " +
            "SET bm.sequence = bm.sequence + 1 " +
            "WHERE bm.sequence < :currentSequence " +
            "AND bm.sequence >= :changeSequence " +
            "AND bm.member.email = :email")
    void increaseBookmarkSequence(@Param("email") String email,
                                  @Param("currentSequence") Integer currentSequence,
                                  @Param("changeSequence") Integer changeSequence);
}
