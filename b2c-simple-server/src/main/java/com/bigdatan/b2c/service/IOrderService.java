package com.bigdatan.b2c.service;

import com.bigdatan.b2c.entity.JoinOrder;
import com.bigdatan.b2c.entity.Order;
import com.bigdatan.b2c.entity.OrderCertify;
import com.bigdatan.b2c.entity.User;

public interface IOrderService extends IBaseService<Order> {
    public Order getOne(String orderNumber);

    public String addBatch(Order order) throws Exception;

    /**
     * @param orderNumber
     */
    public Order getOrderByNumber(String orderNumber);

    public Integer updateAndAddCertify(Order order, OrderCertify orderCertify)
            throws Exception;

    /**
     * 根据支付状态查询总金额
     *
     * @return
     * @throws Exception
     */
    public Integer getTotalAmountByPaystate(Integer payState) throws Exception;

    /**
     * 根据支付状态查询订单数
     */
    public Integer getCountByPaystate(Integer payState) throws Exception;

    public String joinOrder(JoinOrder joinOrder) throws Exception;

    JoinOrder getJoinOrderByNumber(String orderNumber);

    void notifyAll(String orderNumbers, String paymentSeq) throws Exception;

    void buyGoodsAgain(Order order, User user) throws Exception;
}
