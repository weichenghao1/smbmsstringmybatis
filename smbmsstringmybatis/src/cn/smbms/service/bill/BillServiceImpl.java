package cn.smbms.service.bill;
import cn.smbms.dao.bill.BillMapper;
import cn.smbms.pojo.Bill;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("billService")
public class BillServiceImpl implements BillService {


	@Resource
	private BillMapper billDao;

	@Override
	public boolean add(Bill bill) {
		// TODO Auto-generated method stub
		boolean flag = false;
		int add = billDao.add(bill);
		if(add>0){
			flag=true;
		}
		return flag;
	}

	@Override
	public List<Bill> getBillList(Bill bill) {
		// TODO Auto-generated method stub
		List<Bill> billList = billDao.getBillList(bill);
		return billList;
	}

	@Override
	public boolean deleteBillById(String delId) {
		// TODO Auto-generated method stub
		boolean flag=false;
		int i = billDao.deleteBillById(delId);
		if(i>0){
			flag=true;
		}
		return flag;
	}

	@Override
	public Bill getBillById(String id) {
		// TODO Auto-generated method stub
		Bill billById = billDao.getBillById(id);
		return billById;
	}

	@Override
	public boolean modify(Bill bill) {
		boolean flag=false;
		int modify = billDao.modify(bill);
		if(modify>0){
			flag=true;
		}
		return flag;
	}

}
