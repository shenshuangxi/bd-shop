package com.bigdatan.b2c.service.impl;

import com.bigdatan.b2c.entity.OrderCertify;
import com.bigdatan.b2c.mapper.IBaseDao;
import com.bigdatan.b2c.mapper.OrderCertifyMapper;
import com.bigdatan.b2c.service.IOrderCertifyService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

;

/**
 *
 * 订单收款凭证服务实现类
 */
@Transactional
public class OrderCertifyServiceImpl extends BaseServiceImpl<OrderCertify> implements IOrderCertifyService {

    @Resource
    private OrderCertifyMapper orderCertifyMapper;

    @Override
    protected IBaseDao<OrderCertify> getMapper() {
        return orderCertifyMapper;
    }

}
