package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter // immutable 해야 하므로 Setter는 생성하지 않는다.
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address() {
        // JPA 구현 라이브러리가 객체를 생성할 때 리플랙션, 프록시? 같은 기술을 사용할 수 있도록 지원해야 한다.
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
