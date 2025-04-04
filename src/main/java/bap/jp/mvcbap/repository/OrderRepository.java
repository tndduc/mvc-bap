package bap.jp.mvcbap.repository;

import bap.jp.mvcbap.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT FUNCTION('MONTH', o.orderDate), SUM(o.totalAmount) " +
            "FROM Order o " +
            "WHERE FUNCTION('YEAR', o.orderDate) = FUNCTION('YEAR', CURRENT_DATE) " +
            "GROUP BY FUNCTION('MONTH', o.orderDate) " +
            "ORDER BY FUNCTION('MONTH', o.orderDate)")
    List<Object[]> getRevenueByMonthInYear();
    List<Order> findByUser_id(Integer userId);

}

