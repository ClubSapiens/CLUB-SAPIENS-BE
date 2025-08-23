package org.example.cowmatchingbe.repository;

import org.example.cowmatchingbe.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findByUserIdInOrderByUserIdAsc(List<Long> ids);
    @Query(value = """
            SELECT p.* 
            FROM profiles p
            JOIN users u ON p.user_id = u.id
            WHERE (:gender IS NULL OR u.gender = :gender)
            ORDER BY p.user_id ASC
            LIMIT :limit OFFSET :offset
            """, nativeQuery = true)
    List<Profile> findByGenderWithPaging(
            @Param("gender") String gender,
            @Param("offset") int offset,
            @Param("limit") int limit
    );
}
//find BY User Id in ORDER BY user ID ASCEND!