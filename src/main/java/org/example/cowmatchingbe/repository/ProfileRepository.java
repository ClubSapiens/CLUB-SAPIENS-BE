package org.example.cowmatchingbe.repository;

import org.example.cowmatchingbe.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection; //List, Set, Queue 같은 자료구조의 최상위 클래스
import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findByUserIdInOrderByUserIdAsc(Collection<Long> ids);
}