package com.qihang.tao.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.qihang.common.common.PageQuery;
import com.qihang.common.common.PageResult;
import com.qihang.common.common.ResultVo;
import com.qihang.tao.domain.OmsTaoOrder;
import com.qihang.tao.domain.bo.TaoOrderBo;

/**
* @author TW
* @description 针对表【oms_tao_order(淘宝订单表)】的数据库操作Service
* @createDate 2024-04-30 13:52:20
*/
public interface OmsTaoOrderService extends IService<OmsTaoOrder> {
    /**
     * 保存店铺订单
     * @param shopId
     * @param order
     * @return
     */
    ResultVo<Integer> saveOrder(Long shopId, OmsTaoOrder order);
    ResultVo<Integer> updateOrderStatus( OmsTaoOrder order);
    PageResult<OmsTaoOrder> queryPageList(TaoOrderBo bo, PageQuery pageQuery);

    OmsTaoOrder queryDetailById(Long id);

}
