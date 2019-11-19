package cn.smbms.contorller.provider;

import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.service.provider.ProviderServiceImpl;
import cn.smbms.tools.Constants;
import cn.smbms.util.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping(value = "/jsp/provider")
public class ProviderContorller {

    @Resource
    private ProviderService providerService;


    @RequestMapping("/delprovider.do")
    private @ResponseBody
    Map<String, String> delProvider(@RequestParam(value = "proid", required = false) String id) {
        System.out.println(id);
        if (id == null) {
            System.out.println("id是null");
        }
        if (id.trim().equals("")) {
            System.out.println("id是空字符串");
        }
        Map<String, String> resultMap = new HashMap<String, String>();
        if (!StringUtils.isNullOrEmpty(id)) {
            System.out.println("id不是空的，id是" + id);
            int flag = providerService.deleteProviderById(id);
            if (flag == 0) {//删除成功
                resultMap.put("delResult", "true");
            } else if (flag == -1) {//删除失败
                resultMap.put("delResult", "false");
            } else if (flag > 0) {//该供应商下有订单，不能删除，返回订单数
                resultMap.put("delResult", String.valueOf(flag));
            }
        } else {
            resultMap.put("delResult", "notexit");
        }
        //把resultMap转换成json对象输出
        return resultMap;
    }


    @RequestMapping("/modify.do")
    private String modify1(Provider provider, Model model, @RequestParam(required = false, value = "id") Integer id) {
        System.out.println(id);
        provider = providerService.getProviderById(id.toString());
        model.addAttribute("provider", provider);
        return "providermodify";
    }


    @RequestMapping(value = "/provider.moy", method = RequestMethod.POST)
    private String modify(@ModelAttribute("provider") Provider provider, Model model) {
        System.out.println("/modify.do");
        boolean flag = false;
        System.out.println(provider.getId());
        flag = providerService.modify(provider);
        if (flag) {
            return "redirect:/jsp/provider/query.do";
        } else {
            return "providermodify";
        }
    }


    @RequestMapping("/view.do")
    private String getProviderById(Model model, @RequestParam(required = false, value = "id") String id) {
        System.out.println("view.do");
        if (!StringUtils.isNullOrEmpty(id)) {
            Provider provider = null;
            provider = providerService.getProviderById(id);
            model.addAttribute("provider", provider);
            return "providerview";
        }
        return "redirect:/jsp/provider/query.do";
    }


    @RequestMapping("/provideradd")
    private String add1() {
        return "provideradd";
    }


    @RequestMapping("/provideradd.do")
    private String add(@ModelAttribute Provider provider, HttpSession session, Model model) {
        System.out.println("provideradd.do");
        boolean flag = false;
        flag = providerService.add(provider);
        if (flag) {
            return "redirect:/jsp/provider/query.do";
        } else {
            return "provideradd";
        }
    }

    @RequestMapping(value = "/query.do")
    private String query(Model model,
                         @RequestParam(required = false, value = "queryProName") String queryProName,
                         @RequestParam(required = false, value = "queryProCode") String queryProCode)
            throws ServletException, IOException {
        if (StringUtils.isNullOrEmpty(queryProName)) {
            queryProName = "";
        }
        if (StringUtils.isNullOrEmpty(queryProCode)) {
            queryProCode = "";
        }
        List<Provider> providerList = new ArrayList<Provider>();
        providerList = providerService.getProviderList(queryProName, queryProCode);
        model.addAttribute("providerList", providerList);
        model.addAttribute("queryProName", queryProName);
        model.addAttribute("queryProCode", queryProCode);
        return "providerlist";
    }

}
