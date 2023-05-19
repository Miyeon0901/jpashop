package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional // 트랜잭션이 있어야 에러가 안남. test에 있으면 테스트 끝나고 바로 rollback
    @Rollback(false) // 이렇게 하면 rollback 안됨.
    public void testMember() throws Exception {
        //given
        Member member = new Member();
        member.setUsername("miyeon");

        //when
        Long savedId = memberRepository.save(member);
        Member findMember = memberRepository.find(savedId);

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
        // 같은 영속성 컨텍스트 안에서는 ID 값이 같으면 같은 엔티티. 1차 캐시에서 끌어옴. select를 하지조차 않는다.
        System.out.println("findMember == member: " + (findMember == member));
    }
}