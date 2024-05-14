package com.ddl.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ddl.reggie.pojo.User;
import com.ddl.reggie.service.UserService;
import com.ddl.reggie.utils.BaseContext;
import com.ddl.reggie.utils.R;
import com.ddl.reggie.utils.SMSUtils;
import com.ddl.reggie.utils.ValidateCodeUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: UserController
 * Package: com.ddl.reggie.controller
 * Description:
 * 移动端用户登录开发
 *
 * @Author 豆豆龙
 * @Create 3/5/2024 下午9:47
 */

@Slf4j
@Tag(name = "移动端用户接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 发送验证码短信
     *
     * @param user
     * @param session
     * @return
     */
    @Operation(summary = "移动端发送验证码接口", description = "发送验证码")
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) throws Exception {
        //获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            //生成4位的验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //调用阿里云短信服务api
            //SMSUtils.sendMessage("阿里云短信测试", "SMS_154950909", phone, code);
            log.info("code={}", code);
            //需要将验证码保存到session
            //session.setAttribute(phone, code);

            //将生成的验证码缓存到redis中，并且设置有效期为5分钟
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);

            return R.success("验证码短信发送成功");
        }
        return R.error("验证码短信发送失败");
    }

    /**
     * 移动端用户登录
     *
     * @param map     使用map或者UserDto都行
     * @param session
     * @return
     */
    @Operation(summary = "移动端用户登录接口",description = "用户登录")
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());

        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();

        //从session中获取保存的验证码
        //Object codeInSession = session.getAttribute(phone);

        //从redis中获取缓存的验证码
        Object codeInSession = redisTemplate.opsForValue().get(phone);

        //进行验证码的比对（页面提交的验证码和session中保存的验证码）
        if (codeInSession != null && codeInSession.equals(code)) {
            //如果比对成功就登陆成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                //判断当前的手机号对应的用户是否为新用户，如果是新用户就自动注册
                user = new User();
                user.setPhone(phone);
                //用户状态默认为0，即禁用
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());

            //如果用户登陆成功，就可以删除redis中缓存的验证码
            redisTemplate.delete(phone);

            return R.success(user);
        }

        return R.error("登录失败");
    }

    /**
     * 移动端用户退出登录
     *
     * @param session
     * @return
     */
    @Operation(summary = "移动端用户退出登录接口",description = "用户退出登录")
    @PostMapping("/loginout")
    public R<String> logout(HttpSession session) {
        User user = userService.getById(BaseContext.getCurrentId());
        String phone = user.getPhone();
        session.removeAttribute(phone);
        session.removeAttribute("user");
        return R.success("退出登录成功");
    }

}
