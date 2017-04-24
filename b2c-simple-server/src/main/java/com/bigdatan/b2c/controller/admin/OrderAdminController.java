package com.bigdatan.b2c.controller.admin;

import com.bigdatan.b2c.controller.AbstractController;
import com.bigdatan.b2c.entity.Admin;
import com.bigdatan.b2c.entity.Order;
import com.bigdatan.b2c.entity.OrderCertify;
import com.bigdatan.b2c.entity.OrderDetails;
import com.bigdatan.b2c.service.IOrderDetailsService;
import com.bigdatan.b2c.service.IOrderService;
import com.bigdatan.b2c.vo.OrderDetailsAdminSearchVO;
import constant.SystemCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.JsonResponse;
import util.PageResult;
import util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

;

/**
 *  商品订单模块 后台
 */
@RestController
@RequestMapping("/admin/order/orderAdmin")
public class OrderAdminController extends AbstractController {

    @Resource
    private IOrderService orderService;

    @Resource
    private IOrderDetailsService orderDetailsService;

    /**
     * 商品订单列表
     */

    //@GetMapping("/getOrderByPage")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET},value = "/getOrderByPage")
    public JsonResponse<PageResult<Order>> getOrderByPage(
            PageResult<Order> page, Order order, HttpServletRequest request) {
        JsonResponse<PageResult<Order>> result = new JsonResponse<PageResult<Order>>();
        orderService.queryByPage(page, order);
        if (page.getTotal() != 0) {
            result.setRes(SystemCode.SUCCESS);
            result.setObj(page);
        } else {
            result.setRes(SystemCode.FAILURE);
        }
        return result;
    }

    /**
     * 商品订单详情
     */
    //@GetMapping("/getOrderByOrderNumber")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET},value = "/getOrderByOrderNumber")
    public JsonResponse<Order> getOrderByOrderNumber(String orderNumber,
                                                     HttpServletRequest request) {
        JsonResponse<Order> result = new JsonResponse<Order>();
        Order order = orderService.getOne(orderNumber);
        if (order != null) {
            result.setRes(SystemCode.SUCCESS);
            result.setObj(order);
        }
        return result;
    }

    /**
     * 发货 发货时需要传订单id，订单状态，物流单号
     */

    //@GetMapping("/toSend")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET},value = "/toSend")
    public JsonResponse<String> toSend(Order order, HttpServletRequest request) {
        JsonResponse<String> result = new JsonResponse<String>();
        // String logisticsNumber = order.getLogisticsNumber();
        order = orderService.selectByPrimaryKey(order.getOrderId());
        try {

            // 筛选出全额付款和预定付款的数据
            if (order.getLogisticsState() == 1) {
                order.setLogisticsState((byte) 2);
                // order.setLogisticsNumber(logisticsNumber);
                order.setUpdateTime(new Date());
                // 设置其他属性为空
                order.setPayState(null);
                order.setDelState(null);
                order.setOrderNumber(null);

                orderService.updateByPrimaryKeySelective(order);
                result.setRes(SystemCode.SUCCESS);
            }
        } catch (Exception e) {
            logError(request, "[发货异常]", e);
            result.setRes(SystemCode.FAILURE);
        }
        return result;
    }

    /**
     * 确认收货
     *
     * @param order   订单对象
     * @param request
     * @return
     */

    //@GetMapping("/receiveGoods")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET},value = "/receiveGoods")
    public JsonResponse<String> receiveGoods(Order order,
                                             HttpServletRequest request) {
        JsonResponse<String> result = new JsonResponse<String>();
        order = orderService.selectByPrimaryKey(order.getOrderId());
        try {

            // 筛选出全额付款和预定付款的数据
            if (order.getLogisticsState() == 2) {
                order.setLogisticsState((byte) 3);
                order.setUpdateTime(new Date());
                // 设置其他属性为空
                order.setPayState(null);
                order.setDelState(null);
                order.setOrderNumber(null);

                orderService.updateByPrimaryKeySelective(order);
                result.setRes(SystemCode.SUCCESS);
            }
        } catch (Exception e) {
            logError(request, "[确认收货异常]", e);
            result.setRes(SystemCode.FAILURE);
        }
        return result;
    }

    /**
     * 确认收款
     *
     * @param order   订单对象
     * @param request
     * @return
     */
    //@GetMapping("/receiveMoney")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET},value = "/receiveMoney")
    public JsonResponse<String> receiveMoney(Order order,
                                             HttpServletRequest request) {
        JsonResponse<String> result = new JsonResponse<String>();
        String certifyImageUrl = request.getParameter("imageUrl");
        order = orderService.selectByPrimaryKey(order.getOrderId());
        try {

            // 筛选出全额付款和预定付款的数据
            if (order.getLogisticsState() == 3 && order.getPayState() != 2) {
                order.setPayState((byte) 2);
                Date now = new Date();
                order.setUpdateTime(now);
                // 设置其他属性为空
                order.setLogisticsState(null);
                order.setDelState(null);
                order.setOrderNumber(null);
                // orderService.updateByPrimaryKeySelective(order);
                // 添加收款凭证记录
                OrderCertify orderCertify = new OrderCertify();
                orderCertify.setOrderId(order.getOrderId());
                orderCertify.setOrderNumber(order.getOrderNumber());
                orderCertify.setImageUrl(certifyImageUrl);
                orderCertify.setCreateTime(now);
                Admin admin = SessionUtil.getAdminUser(request);
                orderCertify.setAdmin(admin);

                orderService.updateAndAddCertify(order, orderCertify);
                // certifyService.insertSelective(certify);

                result.setRes(SystemCode.SUCCESS);
            }
        } catch (Exception e) {
            logError(request, "[确认收货异常]", e);
            result.setRes(SystemCode.FAILURE);
        }
        return result;
    }

    /**
     * 订单支付情况
     */
    //@GetMapping("/getOrderByPaystate")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET},value = "/getOrderByPaystate")
    public JsonResponse<List<Object>> getOrderByPaystate(
            HttpServletRequest request) {
        JsonResponse<List<Object>> result = new JsonResponse<List<Object>>();
        List<Object> dataList = new ArrayList<Object>();
        List<Object> dataList2 = new ArrayList<Object>();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            // 未付款金额
            Integer paid = orderService.getTotalAmountByPaystate(1);
            map.put("name", "未支付订单");
            map.put("y", paid);
            dataList.add(map);
            // 已付款
            Map<String, Object> map2 = new HashMap<String, Object>();
            Integer unPaid = orderService.getTotalAmountByPaystate(2);
            map2.put("name", "已支付订单");
            map2.put("y", unPaid);
            dataList.add(map2);
            result.setObj(dataList);
            result.setRes(SystemCode.SUCCESS);
        } catch (Exception e) {
            logError(request, "[查询订单数据异常]", e);
            result.setRes(SystemCode.FAILURE);
        }
        return result;
    }

    /**
     * 订单数
     */
    //@GetMapping("/getCountByPaystate")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET},value = "/getCountByPaystate")
    public JsonResponse<List<Object>> getCountByPaystate(
            HttpServletRequest request) {
        JsonResponse<List<Object>> result = new JsonResponse<List<Object>>();
        List<Object> dataList = new ArrayList<Object>();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            // 未付款金额
            Integer paidNum = orderService.getCountByPaystate(1);
            map.put("name", "未支付订单");
            map.put("y", paidNum);
            dataList.add(map);
            // 已付款
            Map<String, Object> map2 = new HashMap<String, Object>();
            Integer unPaidNum = orderService.getCountByPaystate(2);
            map2.put("name", "已支付订单");
            map2.put("y", unPaidNum);
            dataList.add(map2);
            result.setObj(dataList);
            result.setRes(SystemCode.SUCCESS);
        } catch (Exception e) {
            logError(request, "[查询订单数据异常]", e);
            result.setRes(SystemCode.FAILURE);
        }
        return result;
    }

    /**
     * 依据商品订单查询类，查询商品订单详细列表
     */
    //@GetMapping("/getPageByOrderDetailsAdminSearchVO")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET},value = "/getPageByOrderDetailsAdminSearchVO")
    public JsonResponse<PageResult<OrderDetails>> getPageByOrderDetailsAdminSearchVO(
            PageResult<OrderDetails> page, OrderDetailsAdminSearchVO order,
            HttpServletRequest request) {
        JsonResponse<PageResult<OrderDetails>> result = new JsonResponse<PageResult<OrderDetails>>();

        // 处理查询参数
        if (order != null) {
            if (order.getGoodsName() != null && order.getGoodsName().length() > 0) {
                order.setGoodsName("%" + order.getGoodsName() + "%");
            }
            if (order.getDetailsAmount_le() != null
                    && order.getDetailsAmount_le().length() > 0) {
                double detailsAmount_le = Double.valueOf(order
                        .getDetailsAmount_le()) * 100;
                order.setDetailsAmount_le(Double.toString(detailsAmount_le));
            }
            if (order.getDetailsAmount_ge() != null
                    && order.getDetailsAmount_ge().length() > 0) {
                double detailsAmount_ge = Double.valueOf(order
                        .getDetailsAmount_ge()) * 100;
                order.setDetailsAmount_ge(Double.toString(detailsAmount_ge));
            }

        }

        orderDetailsService.getPageByOrderDetailsAdminSearchVO(page, order);
        if (page.getTotal() != 0) {
            result.setRes(SystemCode.SUCCESS);
            result.setObj(page);
        } else {
            result.setRes(SystemCode.FAILURE);
        }
        return result;
    }

    /**
     * 依据商品订单查询类，查询商品订单详细报表,getGoodsName保存的是priceId
     */
    //@GetMapping("/getDetailReportByOrderDetailsAdminSearchVO")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET},value = "/getDetailReportByOrderDetailsAdminSearchVO")
    public JsonResponse<OrderDetails> getDetailReportByOrderDetailsAdminSearchVO(
            OrderDetailsAdminSearchVO order,
            HttpServletRequest request) {
        JsonResponse<OrderDetails> result = new JsonResponse<OrderDetails>();

        // 处理查询参数
        if (order != null) {
            if (order.getGoodsName() == null || order.getGoodsName().trim().length() == 0) {
                order.setGoodsName("0");
            }
            if (order.getDetailsAmount_le() != null
                    && order.getDetailsAmount_le().length() > 0) {
                double detailsAmount_le = Double.valueOf(order
                        .getDetailsAmount_le()) * 100;
                order.setDetailsAmount_le(Double.toString(detailsAmount_le));
            }
            if (order.getDetailsAmount_ge() != null
                    && order.getDetailsAmount_ge().length() > 0) {
                double detailsAmount_ge = Double.valueOf(order
                        .getDetailsAmount_ge()) * 100;
                order.setDetailsAmount_ge(Double.toString(detailsAmount_ge));
            }
            result.setRes(SystemCode.SUCCESS);
            result.setList(orderDetailsService.getDetailReportByOrderDetailsAdminSearchVO(order));
        } else {
            result.setRes(SystemCode.FAILURE);
        }
        return result;
    }


    /**
     * 依据商品订单查询类，获取商品销售总额
     */
    //@GetMapping("/getTotalDetailsAmountByOrderDetailsAdminSearchVO")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET},value = "/getTotalDetailsAmountByOrderDetailsAdminSearchVO")
    public JsonResponse<Long> getTotalDetailsAmountByOrderDetailsAdminSearchVO(
            PageResult<OrderDetails> page, OrderDetailsAdminSearchVO order,
            HttpServletRequest request) {
        JsonResponse<Long> result = new JsonResponse<Long>();
        // 处理查询参数
        if (order != null) {
            if (order.getGoodsName() != null && order.getGoodsName().length() > 0) {
                order.setGoodsName("%" + order.getGoodsName() + "%");
            }
            if (order.getDetailsAmount_le() != null
                    && order.getDetailsAmount_le().length() > 0) {
                double detailsAmount_le = Double.valueOf(order
                        .getDetailsAmount_le()) * 100;
                order.setDetailsAmount_le(Double.toString(detailsAmount_le));
            }
            if (order.getDetailsAmount_ge() != null
                    && order.getDetailsAmount_ge().length() > 0) {
                double detailsAmount_ge = Double.valueOf(order
                        .getDetailsAmount_ge()) * 100;
                order.setDetailsAmount_ge(Double.toString(detailsAmount_ge));
            }

        }
        long total = orderDetailsService
                .getTotalDetailsAmountByOrderDetailsAdminSearchVO(order);

        result.setRes(SystemCode.SUCCESS);
        result.setObj(total);

        return result;
    }

    /**
     * 依据商品订单查询类，获取商品销售总数
     */
    //@GetMapping("/getTotalNumAmountByOrderDetailsAdminSearchVO")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET},value = "/getTotalNumAmountByOrderDetailsAdminSearchVO")
    public JsonResponse<Long> getTotalNumAmountByOrderDetailsAdminSearchVO(
            OrderDetailsAdminSearchVO order, HttpServletRequest request) {
        JsonResponse<Long> result = new JsonResponse<Long>();
        // 处理查询参数
        if (order != null) {
            if (order.getGoodsName() != null && order.getGoodsName().length() > 0) {
                order.setGoodsName("%" + order.getGoodsName() + "%");
            }
            if (order.getDetailsAmount_le() != null
                    && order.getDetailsAmount_le().length() > 0) {
                double detailsAmount_le = Double.valueOf(order
                        .getDetailsAmount_le()) * 100;
                order.setDetailsAmount_le(Double.toString(detailsAmount_le));
            }
            if (order.getDetailsAmount_ge() != null
                    && order.getDetailsAmount_ge().length() > 0) {
                double detailsAmount_ge = Double.valueOf(order
                        .getDetailsAmount_ge()) * 100;
                order.setDetailsAmount_ge(Double.toString(detailsAmount_ge));
            }

        }
        long total = orderDetailsService
                .getTotalNumAmountByOrderDetailsAdminSearchVO(order);

        result.setRes(SystemCode.SUCCESS);
        result.setObj(total);
        return result;
    }

}
