package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberForm {
    /*
    폼 객체와 엔티티 객체를 별도로 쓰는 이유는?
    요구사항이 단순하면 엔티티만 써도 상관 없음.
    엔티티에 화면 종속적인 기능이 많이 생기게 되면 지저분해짐.
    JPA를 사용할 때 Entity를 최대한 순수하게 유지해야함.
    오직 핵심 비즈니스 로직에만 dependency 가 있도록. 화면을 위한 로직은 없어야 함.
     */

    @NotEmpty(message = "회원 이름은 필수입니다.")
    private String name;

    private String city;
    private String street;
    private String zipcode;
}
