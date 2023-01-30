package jpapractice.practice.service;

import jpapractice.practice.domain.*;
import jpapractice.practice.domain.item.Book;
import jpapractice.practice.domain.item.Item;
import jpapractice.practice.exception.NotEnoughStockException;
import jpapractice.practice.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    
    @Test
    void 상품주문() {
        // given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10, 10000);

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // then
        Order order = orderRepository.findOne(orderId);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(item.getStockQuantity()).isEqualTo(8);
        assertThat(order.getTotalPrice()).isEqualTo(orderCount * item.getPrice());
    }

    @Test
    void 상품취소() {
        // given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10, 10000);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order order = orderRepository.findOne(orderId);

        assertThat(item.getStockQuantity()).isEqualTo(10);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCEL);
    }

    @Test
    void 상품주문_수량초과() {
        // given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10, 10000);

        int orderCount = 11;



        // when
        assertThatThrownBy(() -> {
            orderService.order(member.getId(), item.getId(), orderCount);
        }).isInstanceOf(NotEnoughStockException.class);

        NotEnoughStockException ex = 
                assertThrows(NotEnoughStockException.class, () ->
                    orderService.order(member.getId(), item.getId(), orderCount));

        System.out.println("ex.getMessage() = " + ex.getMessage());
        // then
    }

    private Book createBook(String name, int quantity, int price) {
        Book book = new Book();
        book.setName(name);
        book.setStockQuantity(quantity);
        book.setPrice(price);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("kim");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }
}