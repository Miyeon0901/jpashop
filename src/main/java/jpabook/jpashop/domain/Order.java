package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne // 상대방에는 @OneToMany
    @JoinColumn(name = "member_id") // 외래키. JPA에서 외래키는 연관 관계의 주인만 update
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "delivery_id")// 일대일 관계에서 연관관계 주인(외래키를 가짐)은 access가 많이 이뤄지는 테이블
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문시간. LocalDateTime을 쓰면 hibernate가 알아서 지원을 해줌.

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]
}
