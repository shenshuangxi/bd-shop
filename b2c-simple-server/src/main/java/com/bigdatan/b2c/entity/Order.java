package com.bigdatan.b2c.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {
    private Integer orderId;
    private String orderNumber;
    private User user;
    private Integer totalAmount;
    private Integer paidAmount;
    private Integer discountAmount;
    private Receive receive; // 订单收货地址
    private Date sendTime; // 发货时间
    private Byte logisticsState; // 配送状态
    private Byte invoiceTag; // 开具发票标志
    private String comment; // 备注说明
    private Byte delState;
    private Payment payment; // 付款方式
    private String paymentSeq; // 在线支付流水号
    private Byte payState;
    private Date createTime;
    private Date updateTime;
    private List<OrderDetails> orderDetailsList;

    private OrderCertify orderCertify; // 订单凭证

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Integer paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Integer discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Receive getReceive() {
        return receive;
    }

    public void setReceive(Receive receive) {
        this.receive = receive;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Byte getLogisticsState() {
        return logisticsState;
    }

    public void setLogisticsState(Byte logisticsState) {
        this.logisticsState = logisticsState;
    }

    public Byte getInvoiceTag() {
        return invoiceTag;
    }

    public void setInvoiceTag(Byte invoiceTag) {
        this.invoiceTag = invoiceTag;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Byte getDelState() {
        return delState;
    }

    public void setDelState(Byte delState) {
        this.delState = delState;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getPaymentSeq() {
        return paymentSeq;
    }

    public void setPaymentSeq(String paymentSeq) {
        this.paymentSeq = paymentSeq;
    }

    public Byte getPayState() {
        return payState;
    }

    public void setPayState(Byte payState) {
        this.payState = payState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<OrderDetails> getOrderDetailsList() {
        return orderDetailsList;
    }

    public void setOrderDetailsList(List<OrderDetails> orderDetailsList) {
        this.orderDetailsList = orderDetailsList;
    }

    public OrderCertify getOrderCertify() {
        return orderCertify;
    }

    public void setOrderCertify(OrderCertify orderCertify) {
        this.orderCertify = orderCertify;
    }

}