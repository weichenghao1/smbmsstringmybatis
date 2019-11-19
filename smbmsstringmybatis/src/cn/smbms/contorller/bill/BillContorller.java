package cn.smbms.contorller.bill;

import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.bill.BillService;
import cn.smbms.service.bill.BillServiceImpl;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.service.provider.ProviderServiceImpl;
import cn.smbms.tools.Constants;
import cn.smbms.util.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/jsp/bill")
public class BillContorller {


    @Resource
    private BillService billService;

    @Resource
    private ProviderService providerService;


    @RequestMapping("/getBillById")
    private String getBillById(@RequestParam(required = false, value = "id") String id, Model model) {
        if (!StringUtils.isNullOrEmpty(id)) {
            Bill bill = null;
            bill = billService.getBillById(id);
            model.addAttribute("bill", bill);
            return "billview";
        }
        return "redirect:/jsp/bill/query.do";
    }


    @RequestMapping("/getproviderlist.do")
    public @ResponseBody
    List<Provider> getProviderlist() {
        return providerService.getProviderList("", "");
    }


    @RequestMapping("/modify.doo")
    private String modify1(Model model, @RequestParam(required = false, value = "id") String id) {
        System.out.println("/modify.doo");
        System.out.println(id);
        Bill bill = null;
        bill = billService.getBillById(id);
        model.addAttribute("bill", bill);
        return "billmodify";
    }

    @RequestMapping("/modify.do")
    private String modify(@ModelAttribute("bill") Bill bill, HttpSession session, Model model,
                          @RequestParam(required = false, value = "id") Integer id) {
        System.out.println("/modify.do");
        bill.setId(id);
        boolean flag = false;
        flag = billService.modify(bill);
        if (flag) {
            return "redirect:/jsp/bill/query.do";
        } else {
            return "billmodify";
        }
    }


    @RequestMapping("bill.duo")
    @ResponseBody
    private HashMap<String, String> delBill(@RequestParam(required = false, value = "billid") String id)
            throws ServletException, IOException {
        System.out.println("delBill");
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (!StringUtils.isNullOrEmpty(id)) {
            boolean flag = billService.deleteBillById(id);
            if (flag) {//删除成功
                resultMap.put("delResult", "true");
            } else {//删除失败
                resultMap.put("delResult", "false");
            }
        } else {
            resultMap.put("delResult", "notexit");
        }
        return resultMap;

    }


    @RequestMapping("/billadd.do")
    private String add1() {
        return "billadd";
    }


    @RequestMapping("/billadd.doo")
    private String add(@ModelAttribute("bill") Bill bill, Model model) {
        System.out.println("/billadd.do");
        boolean flag = false;
        flag = billService.add(bill);
        System.out.println("add flag -- > " + flag);
        if (flag) {
            return "redirect:/jsp/bill/query.do";
        } else {
            return "billadd";
        }

    }


    @RequestMapping(value = "/query.do")
    private String query(Model model,
                         @RequestParam(required = false, value = "queryProductName") String queryProductName,
                         @RequestParam(required = false, value = "queryProviderId") String queryProviderId,
                         @RequestParam(required = false, value = "queryIsPayment") String queryIsPayment)
            throws ServletException, IOException {
        List<Provider> providerList = new ArrayList<Provider>();
        providerList = providerService.getProviderList("", "");
        model.addAttribute("providerList", providerList);
        if (StringUtils.isNullOrEmpty(queryProductName)) {
            queryProductName = "";
        }
        List<Bill> billList = new ArrayList<Bill>();
        Bill bill = new Bill();
        if (StringUtils.isNullOrEmpty(queryIsPayment)) {
            bill.setIsPayment(0);
        } else {
            bill.setIsPayment(Integer.parseInt(queryIsPayment));
        }
        if (StringUtils.isNullOrEmpty(queryProviderId)) {
            bill.setProviderId(0);
        } else {
            bill.setProviderId(Integer.parseInt(queryProviderId));
        }
        bill.setProductName(queryProductName);
        billList = billService.getBillList(bill);
        model.addAttribute("billList", billList);
        model.addAttribute("queryProductName", queryProductName);
        model.addAttribute("queryProviderId", queryProviderId);
        model.addAttribute("queryIsPayment", queryIsPayment);
        return "billlist";
    }

}
