package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    /*public void updateItem(Book param) { // param: 준영속 상태의 엔티티
        Book findItem = (Book) itemRepository.findOne(param.getId()); // 같은 엔티티를 조회. 영속 상태의 엔티티
        findItem.setPrice(param.getPrice()); // setter를 쓰는 것보다는 별도의 메서드를 만드는 것이 더 좋음.
        findItem.setName(param.getName());
        findItem.setStockQuantity(param.getStockQuantity());
        findItem.setAuthor(param.getAuthor());
        findItem.setIsbn(param.getIsbn());

        // 영속 상태이므로 별도로 저장 필요 없음. 변경 감지!
    } // 위 코드를 JPA에서 merge라는 기능으로 제공. but 모든 필드를 교체하므로 변경감지(Dirty Checking) 써야함.*/
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

}
