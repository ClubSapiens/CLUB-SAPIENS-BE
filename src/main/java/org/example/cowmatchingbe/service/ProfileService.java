
package org.example.cowmatchingbe.service;

import lombok.RequiredArgsConstructor;
import org.example.cowmatchingbe.domain.Profile;
import org.example.cowmatchingbe.domain.User;
import org.example.cowmatchingbe.repository.ProfileRepository;
import org.example.cowmatchingbe.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    /** 프로필 저장/수정(UPSERT) */
    @Transactional
    public Profile upsert(Long userId, String nickname, String mbti, String desc, String insta) {
        Profile p = profileRepository.findById(userId)
                .orElse(Profile.builder().userId(userId).build());
        p.setNickname(nickname);
        p.setMbti(mbti != null ? mbti.toUpperCase() : null);
        p.setDescription(desc);
        p.setInstaProfile(insta);
        return profileRepository.save(p);
    }

    /**
     * 페이지 조회(성별 필터 + offset/limit)
     * 간단 구현: users에서 성별 필터 → id 슬라이싱 → 그 id들로 profiles 조회
     */
    public List<Profile> getProfilesPage(String gender, int offset, int limit) {
        var ids = userRepository.findAll().stream()
                .filter(u -> gender == null || gender.isBlank() || gender.equalsIgnoreCase(u.getGender()))
                .map(User::getId)
                .sorted(Comparator.naturalOrder())
                .toList();

        int end = Math.min(ids.size(), offset + limit);
        if (offset >= end) return List.of();

        var pageIds = ids.subList(offset, end);
        return profileRepository.findByUserIdInOrderByUserIdAsc(pageIds);
    }
}