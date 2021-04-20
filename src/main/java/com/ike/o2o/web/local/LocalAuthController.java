package com.ike.o2o.web.local;

import com.ike.o2o.dto.LocalAuthExecution;
import com.ike.o2o.entity.LocalAuth;
import com.ike.o2o.entity.PersonInfo;
import com.ike.o2o.enums.LocalAuthStateEnum;
import com.ike.o2o.service.LocalAuthService;
import com.ike.o2o.until.CodeUtil;
import com.ike.o2o.until.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/local", method = {RequestMethod.GET, RequestMethod.POST})
public class LocalAuthController {
    @Autowired
    private LocalAuthService localAuthService;

    /**
     * 绑定用户信息
     *
     * @return
     */
    @RequestMapping(value = "/bindlocalauth", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> bindLocalAuth(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();

        //验证码验证
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }
        //读取验证数据
        String userName = HttpServletRequestUtil.getString(request, "userName");
        String password = HttpServletRequestUtil.getString(request, "password");
        PersonInfo personInfo = (PersonInfo) request.getSession().getAttribute("user");

        //非空判断
        if (userName != null && password != null && personInfo != null && personInfo.getUserId() != null) {
            //注册一个本地账号与personInfo关联
            LocalAuth localAuth = new LocalAuth();
            localAuth.setUsername(userName);
            localAuth.setPassword(password);
            localAuth.setPersonInfo(personInfo);
            try {
                //绑定 触发异常
                LocalAuthExecution localAuthExecution = localAuthService.bindLocalAuth(localAuth);
                //判断绑定结果
                if (localAuthExecution.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                    return modelMap;
                } else if (localAuthExecution.getState() == LocalAuthStateEnum.IS_BINDING.getState()) {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "已绑定过本地用户");
                    return modelMap;
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "绑定失败");
                    return modelMap;
                }
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                e.printStackTrace();
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户名:"+userName+",密码:"+password+",personInfo"+personInfo);
            return modelMap;
        }
    }

    /**
     * 修改用户密码
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/changelocalpwd", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> changeLocalPwd(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        String userName = HttpServletRequestUtil.getString(request, "userName");
        String password = HttpServletRequestUtil.getString(request, "password");
        String newPassword = HttpServletRequestUtil.getString(request, "newPassword");
        PersonInfo personInfo = (PersonInfo) request.getSession().getAttribute("user");
        //验证码校验
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }
        //判断新旧密码是否一致
        if (userName != null && password != null && newPassword != null && personInfo != null && personInfo.getUserId() != null) {
            if (password.equals(newPassword)) {
                modelMap.put("success", false);
                modelMap.put("errMsg", "新旧密码不能相同");
                return modelMap;
            }
            //查询是否存在该用户
            LocalAuth currentLocalAuth = localAuthService.getLocalAuthByUsernameAndPwd(userName, password);

            if (currentLocalAuth == null) {
                modelMap.put("success", false);
                modelMap.put("errMsg", "用户名或密码错误,请重新输入");
                return modelMap;
            } else {
                try {
                    LocalAuth authenticLocalAuth = localAuthService.getLocalAuthByUserId(personInfo.getUserId());
                    if (!currentLocalAuth.getLocalAuthId().equals(authenticLocalAuth.getLocalAuthId())) {
                        modelMap.put("success", false);
                        modelMap.put("errMsg", "你不能修改其他账号的密码");
                        return modelMap;
                    }
                    //修改密码
                    LocalAuthExecution localAuthExecution = localAuthService.modifyLocalAuth(currentLocalAuth.getPersonInfo().getUserId(), userName, password, newPassword);
                    //判断返回结果
                    if (LocalAuthStateEnum.SUCCESS.getState() == localAuthExecution.getState()) {
                        modelMap.put("success", true);
                        return modelMap;
                    } else {
                        modelMap.put("false", true);
                        modelMap.put("errMsg", "密码修改失败");
                        return modelMap;
                    }
                } catch (Exception e) {
                    modelMap.put("false", true);
                    modelMap.put("errMsg", e.getMessage());
                    return modelMap;
                }
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "param can not be empty");
            return modelMap;
        }
    }

    /**
     * 用户登录
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/logincheck", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> loginCheck(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();

        //验证码验证,前三次输入不需要验证码
        if (HttpServletRequestUtil.getBoolean(request, "needVerify")) {
            if (!CodeUtil.checkVerifyCode(request)) {
                modelMap.put("success", false);
                modelMap.put("errMsg", "验证码错误");
                return modelMap;
            }
        }
        //提取用户名和密码  userName password
        String userName = HttpServletRequestUtil.getString(request, "userName");
        String password = HttpServletRequestUtil.getString(request, "password");

        if (userName == null || password == null) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户名或密码不能为空");
            return modelMap;
        }
        //用户名和密码校验
        LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(userName, password);
        if (localAuth == null) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户名或密码有误");
            return modelMap;
        } else {
            //往session中塞入personInfo
            request.getSession().setAttribute("user", localAuth.getPersonInfo());
            modelMap.put("success", true);
            return modelMap;
        }
    }

    /**
     * 用户登出
     *
     * @return
     */
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> logout(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        request.getSession().setAttribute("user", null);
        modelMap.put("success", true);
        return modelMap;
    }
}
