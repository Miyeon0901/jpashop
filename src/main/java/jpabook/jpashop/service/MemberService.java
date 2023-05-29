package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // spring이 제공

import java.util.List;

@Service
//@Transactional // 데이터 변경하는 곳엔 항상 transaction이 있어야함. 클래스에 주면 메서드에도 적용됨.
@Transactional(readOnly = true) // 읽기가 더 많으니까 readOnly 기본으로
@RequiredArgsConstructor // final이 붙은 필드만 가지고 생성자를 만들어줌.
public class MemberService {

//    @Autowired // field injection. 스프링이 스프링 빈에 등록되어 있는 memberRepository를 주입해줌. private이라 변경 불가.
    private final MemberRepository memberRepository; // 변경할 일 없으므로 final. 컴파일 시점에 체크도 해줌.

//    @Autowired // setter injection. 테스트 할 때 좋음. 가짜 repository 주입 가능.
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

//    @Autowired // 생성자 injection. 권장하는 방법. 생략해도 자동으로 주입해줌.
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
    /*
    public static void main(String[] args) {
        MemberService memberService = new MemberService();
    }
    */

    /**
     * 회원 가입
     */
    @Transactional // 읽기 전용 아니니까 따로 줘야함.
    public Long join(Member member) {

        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName()); // 보다 안전하게 db의 name에 unique 제약조건
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
//    @Transactional(readOnly = true) // readOnly로 하면 성능 최적화
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

//    @Transactional(readOnly = true)
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }


    @Transactional
    public void update(Long id, String name) { // 변경감지를 이용
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
