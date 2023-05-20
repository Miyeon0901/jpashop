package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "member") // 나는 거울일 뿐이야
    private List<Order> orders = new ArrayList<>();
}
