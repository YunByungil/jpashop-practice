package jpapractice.practice.service;

import jpapractice.practice.domain.item.Book;
import jpapractice.practice.domain.item.Item;
import jpapractice.practice.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Test
    void 상품등록() {
        // given
        Book book = new Book();
        book.setName("hi");
        
        // when
        itemService.saveItem(book);

        // then
        Item one = itemService.findOne(book.getId());
        System.out.println("one.getName() = " + one.getName());
    }

}