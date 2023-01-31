package jpapractice.practice.repository;

import jpapractice.practice.domain.Order;
import jpapractice.practice.domain.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long orderId) {
        return em.find(Order.class, orderId);
    }

    public List<Order> findAllByString(OrderSearch orderSearch) {
        String jqpl = "select o from Order o join o.member m";
        boolean isFirstCondtion = true;

        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondtion) {
                jqpl += " where";
                isFirstCondtion = false;
            } else {
                jqpl += " and";
            }
            jqpl += " o.orderStatus = :orderStatus";
        }

        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondtion) {
                jqpl += " where";
                isFirstCondtion = false;
            } else {
                jqpl += " and";
            }
            jqpl += " m.name like :name";
        }
        TypedQuery<Order> query = em.createQuery(jqpl, Order.class)
                .setMaxResults(1000); // 최대 1000건

        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("orderStatus", orderSearch.getOrderStatus());
        }

        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();

//        return em.createQuery("select o from Order o join o.member m" +
//                " where o.orderStatus = :orderStatus" +
//                " and m.name like :name", Order.class)
//                .setParameter("orderStatus", orderSearch.getOrderStatus())
//                .setParameter("name", orderSearch.getMemberName())
//                .getResultList();
    }

}
