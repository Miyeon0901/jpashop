package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 상대방에는 @OneToMany.
    @JoinColumn(name = "member_id") // 외래키. JPA에서 외래키는 연관 관계의 주인만 update
    private Member member; // = new ByteBuddyInterceptor(); 지연로딩이므로 proxy 객체를 가져옴. bytebuddy
    // Hibernate5Module을 쓰면 제외하고 order만 가져와줌. orderItems: null, delivery: null
    // Configure FORCE_LAZY_LOADING 으로 강제로 가져올 수 있음. -> 성능상 문제가 생김.

//    @BatchSize(size = 1000) // detail 하게 적용, collection 에 적용.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // cascade는 persist를 전파
    private List<OrderItem> orderItems = new ArrayList<>();

    /*
    persist(orderItemA)
    persist(orderItemB)
    persist(orderItemC)
    persist(order)

    cascade를 쓰면 위 네줄이 아래 한줄로 줄여짐.
    persist(order)
    */


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // order 저장할때 delivery도 함께 persist
    @JoinColumn(name = "delivery_id")// 일대일 관계에서 연관관계 주인(외래키를 가짐)은 access가 많이 이뤄지는 테이블
    private Delivery delivery;

    // 컬럼명이 order_date가 됨. SpringPhysicalNamingStrategy
    private LocalDateTime orderDate; // 주문시간. LocalDateTime을 쓰면 hibernate가 알아서 지원을 해줌.

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    // == 연관관계 (편의) 메서드 == // 양방향일 때 핵심적으로 컨트롤 하는 엔티티 쪽에 넣어주는 것이 좋음.
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    /*
    public static void main(String[] args) {
        Member member = new Member();
        Order order = new Order();

        member.getOrders().add(order);
        order.setMember(member);
        // 위 두줄을 쓸 필요 없게 연관관계 편의 메서드를 만듦.
        // order.setMember(member); 만 작성하면 됨.
    }
    */

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //== 생성 메서드 ==// 앞으로 생성하는 부분 변경이 필요하면 이부분만 보면 된다.
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //== 비즈니스 로직 ==//
    /**
     * 주문취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) { // 강조할 때나 똑같은 이름이 있을 때 this.orderItems 쓰면됨.
            orderItem.cancel();
        }
    }

    // == 조회 로직 == //
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
//        for (OrderItem orderItem : orderItems) {
//            totalPrice += orderItem.getTotalPrice();
//        }
//        collapse loop with stream 'sum()' --> java8
        return totalPrice;
    }
}
