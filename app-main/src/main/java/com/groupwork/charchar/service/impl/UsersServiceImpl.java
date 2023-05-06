package com.groupwork.charchar.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.groupwork.charchar.dao.UsersDao;
import com.groupwork.charchar.entity.UsersEntity;
import com.groupwork.charchar.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service("usersService")
public class UsersServiceImpl extends ServiceImpl<UsersDao, UsersEntity> implements UsersService {
    @Autowired
    private UsersDao usersDao;

    @Override
    public Map<String, Object> loginAccount(UsersEntity user) {
        //创建map记录输出用户输入的账户密码注册或者未注册，密码不正确等
        Map<String, Object> resultMap = new ConcurrentHashMap<>();
//        ArrayList<Integer> arrayList=new ArrayList<>();
        //判断输入的是否是邮箱
        if (!isEmail(user.getEmail())) {
            resultMap.put("code", 400);
            resultMap.put("message", "请输入正确的邮箱");
            return resultMap;
        }
        List<UsersEntity> usersEntityList = usersDao.selectEmail(user.getEmail());
        //该用户不存在或未注册
        if (usersEntityList == null || usersEntityList.isEmpty()) {
            resultMap.put("code", 400);
            resultMap.put("message", "该用户不存在或未注册");
            return resultMap;
        }
        //用户存在多个相同名字账号，账号异常
        if (usersEntityList.size() > 1) {
            resultMap.put("code", 400);
            resultMap.put("message", "账号异常");
            return resultMap;
        }
        //查询到一个用户，进行密码对比(一个email只有一个用户所以是get（0）)
        UsersEntity usersEntity2 = usersEntityList.get(0);
        //用户输入的密码和盐进行加密
        String md5Pwd = SecureUtil.md5(user.getPassword() + usersEntity2.getSalt());//查询到的salt和密码编写的雪花数应该与database对应
        if (!usersEntity2.getPassword().equals(md5Pwd)) {
//        System.out.println(md5Pwd+"=?"+usersEntity2.getUsername());
            resultMap.put("code", 400);
            resultMap.put("message", "输入的密码不正确");
            return resultMap;
        }
        resultMap.put("code", 200);
        resultMap.put("message", "登陆成功");
        resultMap.put("data", user.getEmail());
        resultMap.put("userId", usersEntityList.get(0).getUserId());
        return resultMap;

    }

    //执行该方法前应该先执行loginAccount
    @Override
    public Map<String, Object> updatePassword(UsersEntity user) {
        Map<String, Object> resultMap = new ConcurrentHashMap<>();
        String salt = RandomUtil.randomString(6);//用为加密，生成随机数位6位的雪花数
        String md5Pwd = SecureUtil.md5(user.getPassword() + salt);
        //生成新的盐和加密后的新密码一并保存到数据库
        usersDao.updatePwd(user.getEmail(), md5Pwd, salt);
        resultMap.put("code", 200);
        resultMap.put("message", "修改密码成功");
        System.out.println("updatePassword");
        return resultMap;
    }

    //调用login方法后再调用
    @Override
    public Map<String, Object> updateOneUserInformation(UsersEntity user) {
        Map<String, Object> resultMap = new ConcurrentHashMap<>();
        List<UsersEntity> usersEntityList = usersDao.selectEmail(user.getEmail());
        //该用户不存在或未注册
        if (usersEntityList == null || usersEntityList.isEmpty()) {
            resultMap.put("code", 400);
            resultMap.put("message", "该用户不存在或未注册");
            return resultMap;
        }
        //用户存在多个相同名字账号，账号异常
        if (usersEntityList.size() > 1 || usersEntityList.get(0).getUsername().equals(user.getUsername())) {
            resultMap.put("code", 400);
            resultMap.put("message", "该账号重名或异常");
            return resultMap;
        }
        // 检查密保问题的答案是否正确
        usersDao.updateUserInformation(user.getUsername(), user.getEmail());
        resultMap.put("code", 200);
        resultMap.put("message", "修改个人信息成功");
        return resultMap;
    }


    @Override
    public Map<String, Object> register(UsersEntity user) {
        Map<String, Object> resultMap = new ConcurrentHashMap<>();
        //判断输入的是否是邮箱
        if (!isEmail(user.getEmail())) {
            resultMap.put("code", 400);
            resultMap.put("message", "请输入正确的邮箱");
            return resultMap;
        }
        List<UsersEntity> usersEntityList = usersDao.selectEmail(user.getEmail());
        //该用户名已经注册
        if (!(usersEntityList == null || usersEntityList.isEmpty())) {
            resultMap.put("code", 400);
            resultMap.put("message", "该用户名已经注册");
            return resultMap;
        }
        //用户存在多个相同名字账号，账号异常
        if (usersEntityList.size() > 1) {
            resultMap.put("code", 400);
            resultMap.put("message", "该账号异常");
            return resultMap;
        }
        // 雪花算法生成确认码
        String confirmCode = IdUtil.getSnowflake(1, 1).nextIdStr();
        //盐
        String salt = RandomUtil.randomString(6);//用为加密，生成随机数位6位的雪花数
        //加密密码，原始密码+盐
        String md5Pwd = SecureUtil.md5(user.getPassword() + salt);
        // 激活失效时间：24小时
        LocalDateTime ldt = LocalDateTime.now().plusDays(1);
        //初始化账号信息
        user.setSalt(salt);
        user.setPassword(md5Pwd);
        user.setConfirmCode(confirmCode);
        user.setActivationTime(ldt);
        user.setIsValid((byte) 0);
        // 新增账号
        int result = usersDao.save(user);
        if (result != 0) {
            // 发送邮件时间很慢（可以使用异步方式发送：多线程、消息队列）
            String activationUrl = "http://1.12.235.241:9090/charchar/users/activation?confirmCode=" + confirmCode;
            sendMail(activationUrl, user.getEmail());
            resultMap.put("code", 200);
            resultMap.put("message", "注册成功，请前往邮箱激活");
        } else {
            resultMap.put("code", 400);
            resultMap.put("message", "注册失败");
        }
        return resultMap;
    }


    @Override
    public Map<String, Object> forgetPassword(UsersEntity user) {
        //运行结果记录列表
        Map<String, Object> resultMap = new ConcurrentHashMap<>();
        //通过邮箱获取该用户
        List<UsersEntity> usersEntityList = usersDao.selectEmail(user.getEmail());
        List<UsersEntity> usersEntityList2 = usersDao.findVerifiCode(user.getEmail());
        //确认该用户是否存在
        if (usersEntityList == null || usersEntityList.isEmpty()) {
            resultMap.put("code", 400);
            resultMap.put("message", "该账号不存在");
            return resultMap;
        }
        //用户存在多个相同名字账号，账号异常
        if (usersEntityList.size() > 1) {
            resultMap.put("code", 400);
            resultMap.put("message", "该账号异常");
            return resultMap;
        }
        UsersEntity usersEntity2 = usersEntityList2.get(0);
        System.out.println(user.getVerificationCode() + "==" + usersEntity2.getVerificationCode());
        if (!((usersEntity2.getVerificationCode()).equals(user.getVerificationCode()))) {
            resultMap.put("code", 400);
            resultMap.put("message", "您输入的验证码不正确");
            return resultMap;
        }
        // 通过上述判定后，创建临时密码
        int num = (int) ((Math.random() * 9 + 1) * 100000);
        String salt = RandomUtil.randomString(6);//用为加密，生成随机数位6位的雪花数
        String md5Pwd = SecureUtil.md5(num + salt);
        //生成新的盐和加密后的新密码一并保存到数据库
        usersDao.updatePwd(user.getEmail(), md5Pwd, salt);
        // 发送邮件得到临时密码（可以使用异步方式发送：多线程、消息队列）
        String activationUrl = "新的密码已生成请尽快更改" + String.valueOf(num);
        sendMail(activationUrl, user.getEmail());
        resultMap.put("code", 200);
        resultMap.put("message", "请前往邮箱获取临时密码");
        return resultMap;
    }


    //执行该方法前应该先执行loginAccount再调用
    @Override
    public Map<String, Object> deleteUser(UsersEntity user) {//还可以通过逻辑删除
        Map<String, Object> resultMap = new ConcurrentHashMap<>();
        //获取该用户名相应的用户名，加密后的密码 和 盐
        List<UsersEntity> usersEntityList = usersDao.selectEmail(user.getEmail());

        //该用户不存在或未注册
        if (usersEntityList == null || usersEntityList.isEmpty()) {
            resultMap.put("code", 400);
            resultMap.put("message", "该用户不存在或未注册");
            return resultMap;
        }
        //用户存在多个相同名字账号，账号异常
        if (usersEntityList.size() > 1) {
            resultMap.put("code", 400);
            resultMap.put("message", "该账号异常");
            return resultMap;
        }
        //查询到一个用户，进行密码对比(一个email只有一个用户所以是get（0）)
        UsersEntity usersEntity = usersEntityList.get(0);
        //用户输入的密码和盐进行加密
        String md5Pwd = SecureUtil.md5(user.getPassword() + usersEntity.getSalt());//查询到的salt和密码编写的雪花数应该与database对应
        if (!usersEntity.getPassword().equals(md5Pwd)) {
            resultMap.put("code", 400);
            resultMap.put("message", "输入的密码不正确");
            return resultMap;
        }
        //注销账户
        usersDao.deleteUser(user.getEmail());
        resultMap.put("code", 200);
        resultMap.put("message", "该账户已被注销");
        return resultMap;
    }

    @Value("${spring.mail.username}")
    private String mailUsername;
    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private TemplateEngine templateEngine;

    @Override
    public void sendMail(String Url, String email) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
            // 设置邮件主题
            message.setSubject("Welcome to CharChar Services");
            // 设置邮件发送者
            message.setFrom(mailUsername);
            // 设置邮件接受者，可以多个
            message.setTo(email);
//             设置邮件发送日期
            message.setSentDate(new Date());
            // 创建上下文环境
            Context context = new Context();
            context.setVariable("Url", Url);
            String text = templateEngine.process("activation-account.html", context);
            // 设置邮件正文
            message.setText(text, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        // 邮件发送
        javaMailSender.send(mimeMessage);
    }

    @Override
    public Map<String, Object> activationAccount(String confirmCode) {
        Map<String, Object> resultMap = new ConcurrentHashMap<>();
        // 根据确认码查询用户
        UsersEntity user = usersDao.selectUserByConfirmCode(confirmCode);
        // 判断激活时间是否超时
        if (user.getActivationTime() != null) {
            boolean after = LocalDateTime.now().isAfter(user.getActivationTime());

            if (after) {
                resultMap.put("code", 400);
                resultMap.put("message", "链接已失效，请重新注册");
                return resultMap;
            }
        } else {
            System.out.println("getActivationTime is null");
        }
        // 根据确认码查询用户并修改状态值为 1（可用）
        int result = usersDao.updateUserByConfirmCode(confirmCode);
        if (result > 0) {
            resultMap.put("code", 200);
            resultMap.put("message", "激活成功");
        } else {
            resultMap.put("code", 400);
            resultMap.put("message", "激活失败");
        }
        return resultMap;
    }

    //判断输入的是否是邮箱
    //以字母或数字开头：^[a-zA-Z0-9._%+-]+
    //邮箱账号部分包括：字母、数字、点号、下划线、加号、减号：[a-zA-Z0-9._%+-]+
    //必须包含一个“@”符号：@
    //域名可以由字母、数字、点号、减号组成：[a-zA-Z0-9.-]+
    //域名后缀至少有两个字母：\.[a-zA-Z]{2,}$
    @Override
    public boolean isEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    @Override
    public Map<String, Object> updateVerificationCode(UsersEntity user) {
        Map<String, Object> resultMap = new ConcurrentHashMap<>();

        List<UsersEntity> usersEntityList = usersDao.selectEmail(user.getEmail());

        //该用户不存在或未注册
        if (usersEntityList == null || usersEntityList.isEmpty()) {
            resultMap.put("code", 400);
            resultMap.put("message", "该用户不存在或未注册");
            return resultMap;
        }
        //用户存在多个相同名字账号，账号异常
        if (usersEntityList.size() > 1) {
            resultMap.put("code", 400);
            resultMap.put("message", "该账号异常");
            return resultMap;
        }
        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000;
        String vertifi = String.valueOf(randomNumber);
        usersDao.updateVertificationCode(user.getEmail(), vertifi);
        String activationUrl = "This is your verification code, please keep it safe" + vertifi;
        sendMail(activationUrl, user.getEmail());
        if (vertifi != null) {
            resultMap.put("code", 200);
            resultMap.put("message", "验证码发送成功");
        } else {
            resultMap.put("code", 400);
            resultMap.put("message", "验证码发送失败");
        }
        return resultMap;
    }


}