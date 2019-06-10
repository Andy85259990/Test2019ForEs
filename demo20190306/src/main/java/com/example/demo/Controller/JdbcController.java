package com.example.demo.Controller;

import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
public class JdbcController {

    @Autowired
    private UserDao userDao1;

    @RequestMapping("/getUser")
    public ModelAndView getUserList() {
        final List<User> userList = userDao1.getUserList();
//        return userList;
        ModelAndView mav = new ModelAndView("userTable");
        mav.addObject("userList", userList);

        return mav;
    }


}
