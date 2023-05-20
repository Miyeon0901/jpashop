package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

// 아래 두가지가 있어야 완전하게 spring으로 integration해서 테스트함
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // test에 있으면 테스트 끝나고 바로 rollback. insert 쿼리가 나가지 않음. 영속성 컨텍스트 flush 안함.
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
//    @Rollback(false) // 이렇게 하면 rollback 안됨.
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("miyeon");

        // when
        Long savedId = memberService.join(member);

        // then
        em.flush(); // 영속성 컨텍스트 flush. rollback 안됨.
        // jpa에서 같은 트랜잭션 안에서 같은 pk 값을 가지면 같은 영속성 컨텍스트로 관리.
        assertEquals(member, memberRepository.findOne(savedId));
    }
    
    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("hoon");

        Member member2 = new Member();
        member2.setName("hoon");

        // when
        memberService.join(member1);
//        try { // expected = IllegalStateException.class로 해결 가능!
            memberService.join(member2); // 예외가 발생해야 한다!!
//        } catch (IllegalStateException e) {
//            return;
//        }
     
        // then
        fail("예외가 발생해야 한다.");
    }
}