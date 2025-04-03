package bap.jp.mvcbap.batch.bai1;

import java.math.BigDecimal;
import java.time.Instant;

public class OrderExport {

    private Integer orderId;
    private Instant orderDate;
    private BigDecimal totalAmount;
    private Integer userId;
    private String userName;

    // Constructors
    public OrderExport() {}

    public OrderExport(Integer orderId, Instant orderDate, BigDecimal totalAmount, Integer userId, String userName) {
	this.orderId = orderId;
	this.orderDate = orderDate;
	this.totalAmount = totalAmount;
	this.userId = userId;
	this.userName = userName;
    }

    // Getters và Setters
    public Integer getOrderId() {
	return orderId;
    }
    public void setOrderId(Integer orderId) {
	this.orderId = orderId;
    }
    public Instant getOrderDate() {
	return orderDate;
    }
    public void setOrderDate(Instant orderDate) {
	this.orderDate = orderDate;
    }
    public BigDecimal getTotalAmount() {
	return totalAmount;
    }
    public void setTotalAmount(BigDecimal totalAmount) {
	this.totalAmount = totalAmount;
    }
    public Integer getUserId() {
	return userId;
    }
    public void setUserId(Integer userId) {
	this.userId = userId;
    }
    public String getUserName() {
	return userName;
    }
    public void setUserName(String userName) {
	this.userName = userName;
    }
}