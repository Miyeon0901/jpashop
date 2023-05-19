package jpabook.jpashop;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository { // Entity 같은 것을 찾아줌. DAO와 유사

    @PersistenceContext
    private EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId(); // command와 query를 분리해라? member 자체를 반환하기보다는 id만 반환
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
