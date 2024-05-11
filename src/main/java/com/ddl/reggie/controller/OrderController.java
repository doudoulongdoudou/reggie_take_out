package com.ddl.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ddl.reggie.dto.OrdersDto;
import com.ddl.reggie.pojo.OrderDetail;
import com.ddl.reggie.pojo.Orders;
import com.ddl.reggie.pojo.ShoppingCart;
import com.ddl.reggie.pojo.User;
import com.ddl.reggie.service.OrderDetailService;
import com.ddl.reggie.service.OrdersService;
import com.ddl.reggie.service.ShoppingCartService;
import com.ddl.reggie.service.UserService;
import com.ddl.reggie.utils.BaseContext;
import com.ddl.reggie.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ClassName: OrderController
 * Package: com.ddl.reggie.controller
 * Description:
 *
 * @Author 豆豆龙
 * @Create 3/5/2024 上午10:36
 */

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 后台订单明细管理
     *
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, Long number, String beginTime, String endTime) {
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        Page<OrdersDto> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(number != null, Orders::getNumber, number);
        queryWrapper.gt(StringUtils.isNotEmpty(beginTime), Orders::getOrderTime, beginTime);
        queryWrapper.lt(StringUtils.isNotEmpty(endTime), Orders::getOrderTime, endTime);
        queryWrapper.orderByDesc(Orders::getOrderTime);
        ordersService.page(ordersPage, queryWrapper);

        BeanUtils.copyProperties(ordersPage, pageInfo, "records");
        List<Orders> records = ordersPage.getRecords();
        List<OrdersDto> list = records.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);

            Long userId = item.getUserId();
            User user = userService.getById(userId);
            String name = user.getName();
            ordersDto.setUserName(name);

            return ordersDto;
        }).collect(Collectors.toList());

        pageInfo.setRecords(list);
        return R.success(pageInfo);
    }

    /**
     * 前台订单操作   派送/完成
     * @param orders
     * @return
     */
    @PutMapping
    public R<String> updateStatus(@RequestBody Orders orders) {
        Long id = orders.getId();
        Integer status = orders.getStatus();
        LambdaUpdateWrapper<Orders> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Orders::getId, id);
        if (status == 3) {
            updateWrapper.set(Orders::getStatus, 3);
            ordersService.update(updateWrapper);
        } else if (status == 4) {
            updateWrapper.set(Orders::getStatus, 4);
            ordersService.update(updateWrapper);
        }
        return R.success("派送成功");
    }

    /**
     * 移动端查看订单页面
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> userPage(Integer page, Integer pageSize) {
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        Page<OrdersDto> pageInfo = new Page<>();

        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        ordersService.page(ordersPage, lambdaQueryWrapper);
        BeanUtils.copyProperties(ordersPage, pageInfo, "records");

        List<Orders> list = ordersPage.getRecords();
        List<OrdersDto> ordersDtoList = list.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            Long orderId = item.getId();

            LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(OrderDetail::getOrderId, orderId);
            List<OrderDetail> orderDetailList = orderDetailService.list(queryWrapper);

            ordersDto.setOrderDetails(orderDetailList);
            return ordersDto;
        }).collect(Collectors.toList());

        pageInfo.setRecords(ordersDtoList);
        return R.success(pageInfo);
    }

    /**
     * 用户下单
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        ordersService.submit(orders);
        return R.success("下单成功");
    }


    /**
     * 移动端 再来一单
     * @param map
     * @return
     */
    @PostMapping("/again")
    public R<String> againSubmit(@RequestBody Map<String,String> map){
        String ids = map.get("id");

        long id = Long.parseLong(ids);

        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId,id);
        //获取该订单对应的所有的订单明细表
        List<OrderDetail> orderDetailList = orderDetailService.list(queryWrapper);

        //通过用户id把原来的购物车给清空，这里的clean方法是视频中讲过的,建议抽取到service中,那么这里就可以直接调用了
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> cartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        cartLambdaQueryWrapper.eq(ShoppingCart::getUserId, currentId);
        shoppingCartService.remove(cartLambdaQueryWrapper);

        //获取用户id
        Long userId = BaseContext.getCurrentId();
        List<ShoppingCart> shoppingCartList = orderDetailList.stream().map((item) -> {
            //把从order表中和order_details表中获取到的数据赋值给这个购物车对象
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUserId(userId);
            shoppingCart.setImage(item.getImage());
            Long dishId = item.getDishId();
            Long setmealId = item.getSetmealId();
            if (dishId != null) {
                //如果是菜品那就添加菜品的查询条件
                shoppingCart.setDishId(dishId);
            } else {
                //添加到购物车的是套餐
                shoppingCart.setSetmealId(setmealId);
            }
            shoppingCart.setName(item.getName());
            shoppingCart.setDishFlavor(item.getDishFlavor());
            shoppingCart.setNumber(item.getNumber());
            shoppingCart.setAmount(item.getAmount());
            shoppingCart.setCreateTime(new Date());
            return shoppingCart;
        }).collect(Collectors.toList());

        //把携带数据的购物车批量插入购物车表  这个批量保存的方法要使用熟练！！！
        shoppingCartService.saveBatch(shoppingCartList);

        return R.success("操作成功");
    }

}
