package com.hmdp.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.LoginFormDTO;
import com.hmdp.dto.Result;
import com.hmdp.entity.User;
import com.hmdp.mapper.UserMapper;
import com.hmdp.service.IUserService;
import com.hmdp.utils.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

import static com.hmdp.utils.SystemConstants.USER_NICK_NAME_PREFIX;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public Result sendCode(String phone, HttpSession session) {
        if(RegexUtils.isEmailInvalid(phone)) {
            return Result.fail("号码格式错误");
        }
        String code = RandomUtil.randomString(6);
        session.setAttribute("code", code);


        log.info("发送验证码 {} 到 {} ", code, phone);
        return Result.ok(code);
    }

    @Override
    public Result login(LoginFormDTO loginForm, HttpSession session) {
        if(RegexUtils.isEmailInvalid(loginForm.getPhone())) {
            return Result.fail("号码格式错误");
        }
        Object code = session.getAttribute("code");
        String trueCode = loginForm.getCode();
        if(code == null || !code.toString().equals(trueCode)) {
            return Result.fail("验证码错误");
        }
        User user = query().eq("phone", loginForm.getPhone()).one();

        if(user == null) {
            user = createWithPhone(loginForm.getPhone());
        }

        session.setAttribute("user", user);
        return Result.ok();
    }

    private User createWithPhone(String phone) {
        User user = new User();
        user.setPhone(phone);
        user.setNickName(USER_NICK_NAME_PREFIX + RandomUtil.randomString(7));

        save(user);
        return user;
    }
}
