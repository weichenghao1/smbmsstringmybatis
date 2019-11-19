package cn.smbms.dao.bill;

import cn.smbms.pojo.Bill;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BillMapper {
    //添加
    int add(Bill bill);

    //通过查询条件获取供应商列表-模糊查询-getBillList
    List<Bill> getBillList(Bill bill);


    //通过delId删除Bill
    int deleteBillById(@Param("delId") String delId);


    //通过billId获取Bill
    Bill getBillById(@Param("id") String id);

    //修改订单信息
    int modify(Bill bill);

    //根据供应商ID查询订单数量
    int getBillCountByProviderId(@Param("providerId") String providerId);


}
