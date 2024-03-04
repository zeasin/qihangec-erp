package com.qihang.tao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qihang.common.common.ResultVoEnum;
import com.qihang.common.common.ResultVo;
import com.qihang.tao.domain.TaoOrder;
import com.qihang.tao.domain.TaoOrderItem;
import com.qihang.tao.mapper.TaoOrderItemMapper;
import com.qihang.tao.service.TaoOrderService;
import com.qihang.tao.mapper.TaoOrderMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.List;

/**
* @author TW
* @description 针对表【tao_order(淘宝订单表)】的数据库操作Service实现
* @createDate 2024-02-29 19:01:11
*/
@AllArgsConstructor
@Service
public class TaoOrderServiceImpl extends ServiceImpl<TaoOrderMapper, TaoOrder>
    implements TaoOrderService{
    private final TaoOrderMapper mapper;
    private final TaoOrderItemMapper itemMapper;
    @Transactional
    @Override
    public ResultVo<Integer> saveOrder(Integer shopId, TaoOrder order) {
        try {
            List<TaoOrder> taoOrders = mapper.selectList(new LambdaQueryWrapper<TaoOrder>().eq(TaoOrder::getTid, order.getTid()));
            if (taoOrders != null && taoOrders.size() > 0) {
                // 存在，修改
                TaoOrder update = new TaoOrder();
                update.setId(taoOrders.get(0).getId());
                update.setSid(order.getSid());
                update.setSellerRate(order.getSellerRate());
                update.setBuyerRate(order.getBuyerRate());
                update.setStatus(order.getStatus());
                update.setModified(order.getModified());
                update.setEndTime(order.getEndTime());
                update.setConsignTime(order.getConsignTime());
                update.setUpdateTime(new Date());
                mapper.updateById(update);
                // 更新item
                for (var item : order.getItems()) {
                    List<TaoOrderItem> taoOrderItems = itemMapper.selectList(new LambdaQueryWrapper<TaoOrderItem>().eq(TaoOrderItem::getOid, item.getOid()));
                    if (taoOrderItems != null && taoOrderItems.size() > 0) {
                        // 更新
                        TaoOrderItem itemUpdate = new TaoOrderItem();
                        itemUpdate.setId(taoOrderItems.get(0).getId());
                        itemUpdate.setRefundId(item.getRefundId());
                        itemUpdate.setRefundStatus(item.getRefundStatus());
                        itemUpdate.setStatus(item.getStatus());
                        itemUpdate.setBuyerRate(item.getBuyerRate());
                        itemUpdate.setSellerRate(item.getSellerRate());
                        itemUpdate.setEndTime(item.getEndTime());
                        itemUpdate.setConsignTime(item.getConsignTime());
                        itemUpdate.setShippingType(item.getShippingType());
                        itemUpdate.setLogisticsCompany(item.getLogisticsCompany());
                        itemUpdate.setInvoiceNo(item.getInvoiceNo());
                        itemMapper.updateById(itemUpdate);
                    } else {
                        // 新增
                        itemMapper.insert(item);
                    }
                }
                return new ResultVo<>(ResultVoEnum.DataExist, "订单已经存在，更新成功");
            } else {
                // 不存在，新增
                order.setShopId(shopId);
                order.setCreateTime(new Date());
                mapper.insert(order);
                // 添加item
                for (var item : order.getItems()) {
                    itemMapper.insert(item);
                }
                return new ResultVo<>(ResultVoEnum.SUCCESS, "SUCCESS");
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new ResultVo<>(ResultVoEnum.SystemException, "系统异常：" + e.getMessage());
        }
    }
}




