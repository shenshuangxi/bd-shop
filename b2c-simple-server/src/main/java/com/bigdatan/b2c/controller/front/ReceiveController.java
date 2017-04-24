package com.bigdatan.b2c.controller.front;

import com.bigdatan.b2c.controller.AbstractController;
import com.bigdatan.b2c.entity.Receive;
import com.bigdatan.b2c.entity.User;
import com.bigdatan.b2c.service.IReceiveService;
import constant.SystemCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.JsonResponse;
import util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

;

@RestController
@RequestMapping("/front/receive/receive")
public class ReceiveController extends AbstractController {
    @Resource
    private IReceiveService receiveService;


    /**
     * 添加送货地址
     */
    //@GetMapping("/addReceive")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET},value = "/addReceive")
    public JsonResponse<Receive> addReceive(Receive receive,
                                            HttpServletRequest request) {
        JsonResponse<Receive> result = new JsonResponse<Receive>(
                SystemCode.FAILURE);
        User user = SessionUtil.getUser(request);
        receive.setCreateTime(new Date());
        receive.setUpdateTime(new Date());
        receive.setUserId(user.getUserId());
        receive.setIsDefault((byte) 2);
        try {
            receiveService.insertSelective(receive);
            result.setRes(SystemCode.SUCCESS);
        } catch (Exception e) {
            logError(request, "[user:" + user.getNickname() + ",新增送货地址异常]", e);
        }
        return result;
    }

    /**
     * 编辑送货地址
     */
    //@GetMapping("/editReceive")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET},value = "/editReceive")
    public JsonResponse<Receive> editReceive(Receive receive,
                                             HttpServletRequest request) {
        JsonResponse<Receive> result = new JsonResponse<Receive>(
                SystemCode.FAILURE);
        User user = SessionUtil.getUser(request);
        receive.setUpdateTime(new Date());
        receive.setUserId(user.getUserId());
        try {
            receiveService.updateByPrimaryKeySelective(receive);
            result.setRes(SystemCode.SUCCESS);
        } catch (Exception e) {
            logError(request, "[user:" + user.getNickname() + ",编辑送货地址异常]", e);
        }

        return result;
    }

    /**
     * 收货地址列表
     */
    //@GetMapping("/getReceiveByPage")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET},value = "/getReceiveByPage")
    public JsonResponse<List<Receive>> getReceiveByPage(
            HttpServletRequest request) {
        JsonResponse<List<Receive>> result = new JsonResponse<List<Receive>>(
                SystemCode.FAILURE);
        User user = SessionUtil.getUser(request);
        // receive.setOpenid(user.getOpenid());
        List<Receive> receive = receiveService.queryReceive(user);
        result.setRes(SystemCode.SUCCESS);
        result.setObj(receive);
        return result;
    }

    /**
     * 设置默认收货地址
     */
    //@GetMapping("/setDefaultReceive")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET},value = "/setDefaultReceive")
    public JsonResponse<String> setDefaultReceive(Integer receiveId,
                                                  HttpServletRequest request) {
        JsonResponse<String> result = new JsonResponse<String>(
                SystemCode.SET_DEFAULTRECEIVE_FAILURE);
        User user = SessionUtil.getUser(request);
        try {
            Receive receive = receiveService.queryDefaultReceive(user);
            receiveService.setDefaultReceive(receive, receiveId);
            result.setRes(SystemCode.SUCCESS);
        } catch (Exception e) {
            logError(request, "[user:" + user.getNickname() + ",用户修改默认地址异常]", e);
        }
        return result;
    }

    /**
     * 获取默认收货地址
     */
    //@GetMapping("/queryDefaultReceive")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET},value = "/queryDefaultReceive")
    public JsonResponse<Receive> queryDefaultReceive(
            HttpServletRequest request, Integer receiveId) {
        JsonResponse<Receive> result = new JsonResponse<Receive>(
                SystemCode.FAILURE);
        User user = SessionUtil.getUser(request);
        if (null == user) {
            result.setRes(SystemCode.FAILURE);
            return result;
        }
        Receive receive = receiveService.queryDefaultReceive(user);
        result.setRes(SystemCode.SUCCESS);
        result.setObj(receive);
        return result;
    }

    /**
     * 查看收货地址
     */
    //@GetMapping("/getReceiveById")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET},value = "/getReceiveById")
    public JsonResponse<Receive> getReceiveById(HttpServletRequest request,
                                                Integer receiveId) {
        JsonResponse<Receive> result = new JsonResponse<Receive>(
                SystemCode.FAILURE);
        Receive receive = receiveService.selectByPrimaryKey(receiveId);
        result.setRes(SystemCode.SUCCESS);
        result.setObj(receive);
        return result;
    }

    /**
     * 删除收货地址
     */
    //@GetMapping("/delReceiveById")
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET},value = "/delReceiveById")
    public JsonResponse<Receive> delReceiveById(Receive receive,
                                                HttpServletRequest request) {
        JsonResponse<Receive> result = new JsonResponse<Receive>(
                SystemCode.FAILURE);
        User user = SessionUtil.getUser(request);
        // receive.setOpenid(user.getOpenid());
        receive.setDelState((byte) 1);
        receive.setUpdateTime(new Date());
        try {
            receiveService.updateByPrimaryKeySelective(receive);
            // 删除收货地址时，判断用户有没有默认地址，如果有要删除的收货地址，则要关联删除用户表的收货地址
            // if(user.getDefaultReceiveId() == receive.getReceiveId()) {
            // user.setDefaultReceiveId(null);
            // user.setUpdateTime(new Date());
            // System.out.println(user.getOpenid()+":"+user.getUserId());
            // userMapper.updateByPrimaryKeySelective(user);
            // }
            result.setRes(SystemCode.SUCCESS);
        } catch (Exception e) {
            logError(request, "[user:" + user.getNickname() + ",删除收货地址]", e);
        }

        return result;
    }
}
