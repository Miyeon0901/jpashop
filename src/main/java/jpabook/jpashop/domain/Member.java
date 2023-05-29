package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded // @Embeddable 과 @Embedded 둘 중 하나만 있으면 되지만 보통 둘 다 써줌.
    private Address address;

//    @JsonIgnore // api에서 조회할때 제외됨. 다양한 case가 있으므로 entity 안에서 이런거(presentation 계층에 대한 로직) 사용하지 말자.
    @OneToMany(mappedBy = "member") // 나는 거울일 뿐이야
    private List<Order> orders = new ArrayList<>(); // 바로 초기화 하는 것이 안전.

}
