package com.blg.dorm.web.action.controller;


import com.blg.dorm.common.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

public class BaseController {
    protected static final String viewFolder = "/html";
    protected static final long jsVersion = System.currentTimeMillis();
    protected static final String cookie_key = "dorm_JSESSIONID";
    protected static final Integer Time_out = 54000;

    @Autowired
    //private RedisClient redisClient;

    protected ModelAndView mv = new ModelAndView();

    public BaseController() {
        mv = new ModelAndView();
    }

    public void initView(HttpServletRequest request) {
        try {

            SupplierUserProductLineVo session = this.getSessionProductLine(request);
            if (session != null) {
                mv.addObject("supSession", session);
                MenuRoot menu = this.getMyMenu(request.getParameter("m_id"), session);

                mv.addObject("menus", menu.getMenu());
                // 查询当前用户的未读消息数量
                int count = noticeService.findNoReadMessageList(session.getUserName());
                for (MenuItem item : menu.getMenu()) {
                    if (item.getName().equals("我的通知") || item.getName().equals("通知")) {
                        item.setMsg(String.valueOf(count));
                        break;
                    }
                }
            }
            initViewParams(request);

            mv.setViewName("/index");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
