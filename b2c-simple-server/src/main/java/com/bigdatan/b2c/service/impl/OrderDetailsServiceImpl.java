package com.bigdatan.b2c.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.bigdatan.b2c.mapper.IBaseDao;
import com.bigdatan.b2c.mapper.OrderDetailsMapper;
import org.springframework.stereotype.Service;

import util.PageResult;


import com.bigdatan.b2c.vo.*;
import com.bigdatan.b2c.entity.OrderDetails;

import com.bigdatan.b2c.service.impl.BaseServiceImpl;
import com.bigdatan.b2c.service.IOrderDetailsService;
import com.github.pagehelper.PageHelper;


@Service
public class OrderDetailsServiceImpl extends BaseServiceImpl<OrderDetails> implements IOrderDetailsService{
	@Resource
	private OrderDetailsMapper orderDetailsMapper;
	@Override
	protected IBaseDao<OrderDetails> getMapper() {
		return orderDetailsMapper;
	}
	
	@Override
	public long getTotalDetailsAmountByOrderDetailsAdminSearchVO(
			OrderDetailsAdminSearchVO entity) {
		
		return (long)orderDetailsMapper.getTotalDetailsAmountByOrderDetailsAdminSearchVO(entity);
	}
	@Override
	public long getTotalNumAmountByOrderDetailsAdminSearchVO(
			OrderDetailsAdminSearchVO entity) {
		
		return (long)orderDetailsMapper.getTotalNumAmountByOrderDetailsAdminSearchVO(entity);
	}
	@Override
	public PageResult<OrderDetails> getPageByOrderDetailsAdminSearchVO(
			PageResult<OrderDetails> t, OrderDetailsAdminSearchVO entity) {
		int pageNo=t.getPageNo();
    	int pageSize=t.getPageSize();
		pageNo = pageNo == 0?1:pageNo;
		pageSize = pageSize == 0?10:pageSize;
		PageHelper.startPage(pageNo,pageSize); 
		return PageResult.toPageResult(orderDetailsMapper.getPageByOrderDetailsAdminSearchVO(entity),t);
	}

	@Override
	public List<OrderDetails> getDetailReportByOrderDetailsAdminSearchVO(
			OrderDetailsAdminSearchVO entity) {
		return orderDetailsMapper.getDetailReportByOrderDetailsAdminSearchVO(entity);
	}

}
