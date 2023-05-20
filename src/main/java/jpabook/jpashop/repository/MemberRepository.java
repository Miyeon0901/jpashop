package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    @Autowired // spring data jpa를 쓰면 PersistenceContext 대신 Autowired 사용 가능
//    @PersistenceContext // 스프링이 생성한 jpa entity manager를 주입해줌..?
    private final EntityManager em;

    // 아래와 같이 EntityManagerFactory를 직접 주입 받을 수 있음..?
    /*
    @PersistenceUnit
    private EntityManagerFactory emf;
    */

//    public MemberRepository(EntityManager em) { // @RequiredArgsConstructor로 생성됨.
//        this.em = em;
//    }

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id); // 타입, pk
    }

    public List<Member> findAll() {
        // sql은 테이블 대상, jpql은 객체 대상이라고 보면 됨.
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
