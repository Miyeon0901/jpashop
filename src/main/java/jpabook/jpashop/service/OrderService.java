package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        // 엔티티 조회: Controller 말고 Service에서 엔티티 조회를 해야 영속성도 유지가 되서 편리함.
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);
        // cascade 옵션으로 orderItem, delivery 까지 다 persist.
        // private owner 인 경우에만 써야함. order 에서만 유일하게 참조하므로 가능한 것.

        return order.getId();
    }

    /**
     * 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel(); // 별도로 update를 하지 않는데도 저장됨. 변경 감지!
    }

    /**
     * 검색
     */
    public List<Order> findOrders(OrderSearch orderSearch){
        // 단순하게 위임만 함. 이런경우 Controller에서 repository 바로 불러도 상관 없음.
        return orderRepository.findAllByString(orderSearch);
    }
}
