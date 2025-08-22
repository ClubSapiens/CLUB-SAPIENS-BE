package org.example.cowmatchingbe.repository;

import org.example.cowmatchingbe.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    //DB 접근 로직 (쿼리메서드) 필요시 기입.
    //오직 데이터베이스 조회/저장/수정/삭제만 책임짐.
}

//일반적으로 레포지토리는 서비스에서 접근해야하기때문에 접근 지정자는 public

//JpaRepository 자체가 애초에 <T,ID> 제네릭을 요구함
// T = 엔티티의 클래스 , ID = 엔티티의 PK 타입 (ID)

//따라서 user relation을 레포지토리로 서비스에서 DB 접근의책임을 주입하려면
//JPA REPOSITORY를 상속하고 요구하는 제네릭이 T = relation class(domain)에 존재하는,
// 그리고 그 relation의 Pk class (type)인 Long인 것.

//서비스 계층에선 이 UserRepo의