package bap.jp.mvcbap.repository;

import bap.jp.mvcbap.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    @Query(value="SELECT * FROM order_item WHERE order_id = :orderId", nativeQuery=true)
    List<OrderItem> findByOrderId(@Param("orderId") Integer orderId);
}