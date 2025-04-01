package bap.jp.mvcbap.repository;

import bap.jp.mvcbap.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserid(Integer userId);
    @Query("SELECT o FROM Order o WHERE o.totalAmount >= :minAmount AND o.totalAmount <= :maxAmount")
    List<Order> findOrdersByTotalAmountBetween(@Param("minAmount") BigDecimal minAmount, @Param("maxAmount") BigDecimal maxAmount);

    @Query("SELECT o FROM Order o WHERE o.totalAmount >= :minAmount")
    List<Order> findOrdersByTotalAmountGreaterThan(@Param("minAmount") BigDecimal minAmount);

    @Query("SELECT o FROM Order o WHERE o.totalAmount <= :maxAmount")
    List<Order> findOrdersByTotalAmountLessThan(@Param("maxAmount") BigDecimal maxAmount);

    @Query("SELECT o FROM Order o WHERE FUNCTION('YEAR', o.orderDate) = FUNCTION('YEAR', CURRENT_DATE) AND FUNCTION('MONTH', o.orderDate) = FUNCTION('MONTH', CURRENT_DATE)")
    List<Order> findOrdersForCurrentMonth();
}

