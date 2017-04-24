package com.bigdatan.b2c.mapper;


import com.bigdatan.b2c.entity.Order;

public interface OrderMapper extends IBaseDao<Order> {
    public Order getOne(String orderNumber);

    /**
     * @param orderNumber
     * @return
     */
    public Order getOrderByNumber(String orderNumber);

    /**
     * 获取订单支付，未支付金额
     *
     * @return
     */
    public Integer getAmountByPaystate(Integer payState);


    /**
     * 获取订单数
     */
    public Integer getCountByPaystate(Integer payState);
}