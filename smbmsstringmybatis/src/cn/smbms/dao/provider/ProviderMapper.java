package cn.smbms.dao.provider;

import cn.smbms.pojo.Provider;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProviderMapper {

    //增加供应商
    int addProvider(Provider provider);

    //通过供应商名称、编码获取供应商列表-模糊查询-providerList
    List<Provider> getProviderList(@Param("proName") String proName, @Param("proCode") String proCode);

    //通过proId删除Provider
    int deleteProviderById(@Param("delId") String delId);

    //通过proId获取Provider
    Provider getProviderById(String id);

    //修改用户信息
    int modify(Provider provider);


}
