package org.example.cowmatchingbe.repository;

import org.example.cowmatchingbe.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findByUserIdInOrderByUserIdAsc(List<Long> ids);
}
//find BY User Id in ORDER BY user ID ASCEND!