package com.bigdatan.b2c.service;

import com.bigdatan.b2c.entity.OrderDetails;
import com.bigdatan.b2c.vo.OrderDetailsAdminSearchVO;
import org.apache.ibatis.annotations.Param;
import util.PageResult;

import java.util.List;

/**
 * 订单详情
 */
public interface IOrderDetailsService extends IBaseService<OrderDetails> {
    /**
     * 依据商品订单查询类，查询商品订单详细列表
     *
     * @param entity
     * @return
     */
    public PageResult<OrderDetails> getPageByOrderDetailsAdminSearchVO(@Param("t") PageResult<OrderDetails> t, @Param("entity") OrderDetailsAdminSearchVO entity);

    /**
     * 依据商品订单查询类，查询商品订单详细报表
     *
     * @param entity
     * @return
     */
    public List<OrderDetails> getDetailReportByOrderDetailsAdminSearchVO(OrderDetailsAdminSearchVO entity);


    /**
     * 依据商品订单查询类，获取商品销售总额
     *
     * @param entity
     * @return
     */
    public long getTotalDetailsAmountByOrderDetailsAdminSearchVO(OrderDetailsAdminSearchVO entity);

    /**
     * 依据商品订单查询类，获取商品销售总数
     *
     * @param entity
     * @return
     */
    public long getTotalNumAmountByOrderDetailsAdminSearchVO(OrderDetailsAdminSearchVO entity);

}


