package cn.smbms.service.provider;

import cn.smbms.dao.bill.BillMapper;
import cn.smbms.dao.provider.ProviderMapper;
import cn.smbms.pojo.Provider;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("providerService")
public class ProviderServiceImpl implements ProviderService {

    @Resource
    private ProviderMapper providerMapper;

    @Resource
    private BillMapper billMapper;

    @Override
    public boolean add(Provider provider) {
        // TODO Auto-generated method stub
        boolean flag = false;
        int i = providerMapper.addProvider(provider);

        if (i > 0) {
            flag = true;
            System.out.println("成功");
        } else {
            System.out.println("失败");
        }


        return flag;
    }

    @Override
    public List<Provider> getProviderList(String proName, String proCode) {
        List<Provider> providerList = providerMapper.getProviderList(proName, proCode);
        return providerList;
    }

    /**
     * 业务：根据ID删除供应商表的数据之前，需要先去订单表里进行查询操作
     * 若订单表中无该供应商的订单数据，则可以删除
     * 若有该供应商的订单数据，则不可以删除
     * 返回值billCount
     * 1> billCount == 0  删除---1 成功 （0） 2 不成功 （-1）
     * 2> billCount > 0    不能删除 查询成功（0）查询不成功（-1）
     * <p>
     * ---判断
     * 如果billCount = -1 失败
     * 若billCount >= 0 成功
     */
    @Override
    public int deleteProviderById(String delId) {
        int billCount = -1;
        billCount = billMapper.getBillCountByProviderId(delId);
        if (billCount == 0) {
            providerMapper.deleteProviderById(delId);
        }
        return billCount;
    }


    @Override
    public Provider getProviderById(String id) {
        // TODO Auto-generated method stub
        Provider providerById = providerMapper.getProviderById(id);
        return providerById;
    }

    @Override
    public boolean modify(Provider provider) {
        // TODO Auto-generated method stub
        boolean flag = false;
        int modify = providerMapper.modify(provider);
        if (modify > 0) {
            flag = true;
        }
        return flag;
    }


}
