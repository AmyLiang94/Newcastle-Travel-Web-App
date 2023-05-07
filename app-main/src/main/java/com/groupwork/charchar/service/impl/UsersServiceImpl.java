package com.groupwork.charchar.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.TemplateEngine;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.groupwork.charchar.dao.UsersDao;
import com.groupwork.charchar.entity.UsersEntity;
import com.groupwork.charchar.service.UsersService;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/*
 * @author Eastman
 * @email 931654949@qq.com
 * @date 2023-05-02 15:33:03
 */

/**

 * The implementation of the UsersService interface.

 * Uses UsersDao for data access.
 */
@Service("usersService")
public class UsersServiceImpl extends ServiceImpl<UsersDao, UsersEntity> implements UsersService {
    @Autowired
    private UsersDao usersDao;

    /**

     * Attempts to log in the user and returns a Map with a response code and message.
     * @param user The user to log in.
     * @return A Map containing the response code and message.
     */
    @Override
    public Map<String, Object> loginAccount(UsersEntity user) {
        //Create map record to output user entered account password registered or unregistered, incorrect password, etc.
        Map<String, Object> resultMap = new ConcurrentHashMap<>();

        //Determine if the input is an email address
        if (!isEmail(user.getEmail())) {
            resultMap.put("code", 400);
            resultMap.put("message", "Please enter the correct email address");
            return resultMap;
        }
        //Determine if a user exists in the database
        List<UsersEntity> usersEntityList = usersDao.selectEmail(user.getEmail());
        //该用户不存在或未注册
        if (usersEntityList == null || usersEntityList.isEmpty()) {
            resultMap.put("code", 400);
            resultMap.put("message", "This user does not exist or is not registered");
            return resultMap;
        }
        //Multiple accounts with the same name exist for users, determine account anomalies
        if (usersEntityList.size() > 1) {
            resultMap.put("code", 400);
            resultMap.put("message", "Account anomalies");
            return resultMap;
        }
        //Query for a user and do a password comparison (an email has only one user so it's get (0))
        UsersEntity usersEntity2 = usersEntityList.get(0);
        //Snowflake number encryption by adding salt to the password entered by the user
        String md5Pwd = SecureUtil.md5(user.getPassword() + usersEntity2.getSalt());//查询到的salt和密码编写的雪花数应该与database对应
        //Determine if the password entered is correct
        if (!usersEntity2.getPassword().equals(md5Pwd)) {
            resultMap.put("code", 400);
            resultMap.put("message", "The password entered is incorrect");
            return resultMap;
        }
        resultMap.put("code", 200);
        resultMap.put("message", "Login successful");
        resultMap.put("data", user.getEmail());
        resultMap.put("userId",usersEntityList.get(0).getUserId());
        return resultMap;

    }

    /**

     * Changes the user's password and returns a Map with a response code and message.
     * @param user The user whose password is to be changed.
     * @return A Map containing the response code and message.
     */
    @Override
    public Map<String, Object> updatePassword(UsersEntity user) {
        Map<String, Object> resultMap = new ConcurrentHashMap<>();
        //Get this user by email
        List<UsersEntity> usersEntityList = usersDao.selectEmail(user.getEmail());
        List<UsersEntity> usersEntityList2 = usersDao.findVerifiCode(user.getEmail());

        if (usersEntityList == null || usersEntityList.isEmpty()) {
            resultMap.put("code", 400);
            resultMap.put("message", "The account does not exist");
            return resultMap;
        }

        if (usersEntityList.size() > 1) {
            resultMap.put("code", 400);
            resultMap.put("message", "The account is abnormal");
            return resultMap;
        }
        UsersEntity usersEntity2 = usersEntityList2.get(0);
        if (!((usersEntity2.getVerificationCode()).equals(user.getVerificationCode()))){
            resultMap.put("code", 400);
            resultMap.put("message", "The verification code you entered is incorrect");
            return resultMap;
        }
        String salt = RandomUtil.randomString(6);//用为加密，生成随机数位6位的雪花数(Used as an encryption to generate a snowflake number with 6 random digits)
        String md5Pwd = SecureUtil.md5(user.getPassword() + salt);
        //Generate a new salt and save it to the database along with the new encrypted password
        usersDao.updatePwd(user.getEmail(), md5Pwd, salt);
        resultMap.put("code", 200);
        resultMap.put("message", "Password change successful");
        return resultMap;
    }

    /**

     * Changes the user's information and returns a Map with a response code and message.
     * @param user The user whose information is to be changed.
     * @return A Map containing the response code and message.
     */

    @Override
    public Map<String, Object> updateOneUserInformation(UsersEntity user) {
        Map<String, Object> resultMap = new ConcurrentHashMap<>();
        List<UsersEntity> usersEntityList = usersDao.selectEmail(user.getEmail());

        if (usersEntityList == null || usersEntityList.isEmpty()) {
            resultMap.put("code", 400);
            resultMap.put("message", "This user does not exist or is not registered");
            return resultMap;
        }

        if (usersEntityList.size() > 1||usersEntityList.get(0).getUsername().equals(user.getUsername())) {
            resultMap.put("code", 400);
            resultMap.put("message", "The account is renamed or abnormal");
            return resultMap;
        }
        usersDao.updateUserInformation(user.getUsername(), user.getEmail());
        resultMap.put("code", 200);
        resultMap.put("message", "Change personal information successfully");
        return resultMap;
    }

    /**
     * Register user method
     * @param user the user to register
     * @return a map containing the result of the operation
     */
    @Override
    public Map<String, Object> register(UsersEntity user) {
        Map<String, Object> resultMap = new ConcurrentHashMap<>();

        if (!isEmail(user.getEmail())) {
            resultMap.put("code", 400);
            resultMap.put("message", "Please enter the correct email address");
            return resultMap;
        }
        List<UsersEntity> usersEntityList = usersDao.selectEmail(user.getEmail());

        if (!(usersEntityList == null || usersEntityList.isEmpty())) {
            resultMap.put("code", 400);
            resultMap.put("message", "This username is already registered");
            return resultMap;
        }

        if (usersEntityList.size() > 1) {
            resultMap.put("code", 400);
            resultMap.put("message", "The account is abnormal");
            return resultMap;
        }
        // 雪花算法生成确认码(Snowflake algorithm to generate confirmation codes)
        String confirmCode = IdUtil.getSnowflake(1, 1).nextIdStr();
        //盐(Salt)
        String salt = RandomUtil.randomString(6);//用为加密，生成随机数位6位的雪花数
        //加密密码，原始密码+盐(Encrypted password, original password + salt)
        String md5Pwd = SecureUtil.md5(user.getPassword() + salt);
        // 激活失效时间：24小时(Activation expiry time: 24 hours)
        LocalDateTime ldt = LocalDateTime.now().plusDays(1);
        //初始化账号信息(Initialising account information)
        user.setSalt(salt);
        user.setPassword(md5Pwd);
        user.setConfirmCode(confirmCode);
        user.setActivationTime(ldt);
        user.setIsValid((byte) 0);
        // Add an account
        int result = usersDao.save(user);
        if (result != 0) {
            String activationUrl = "http://1.12.235.241:9090/charchar/users/activation?confirmCode=" + confirmCode;
            sendMail(activationUrl, user.getEmail());
            resultMap.put("code", 200);
            resultMap.put("message", "Register successfully, please go to your mailbox to activate");
        } else {
            resultMap.put("code", 400);
            resultMap.put("message", "Registration failed");
        }
        return resultMap;
    }

    /**
     * Forget password method
     * @param user the user to reset the password for
     * @return a map containing the result of the operation
     */
    @Override
    public Map<String, Object> forgetPassword(UsersEntity user) {
        //List of run result records
        Map<String, Object> resultMap = new ConcurrentHashMap<>();
        //Get this user by email
        List<UsersEntity> usersEntityList = usersDao.selectEmail(user.getEmail());
        List<UsersEntity> usersEntityList2 = usersDao.findVerifiCode(user.getEmail());
        //Confirm that the user exists
        if (usersEntityList == null || usersEntityList.isEmpty()) {
            resultMap.put("code", 400);
            resultMap.put("message", "The account does not exist");
            return resultMap;
        }
        //用户存在多个相同名字账号，账号异常
        if (usersEntityList.size() > 1) {
            resultMap.put("code", 400);
            resultMap.put("message", "The account is abnormal");
            return resultMap;
        }
        UsersEntity usersEntity2 = usersEntityList2.get(0);

        if (!((usersEntity2.getVerificationCode()).equals(user.getVerificationCode()))){
            resultMap.put("code", 400);
            resultMap.put("message", "The verification code you entered is incorrect");
            return resultMap;
        }
        // After passing the above determination, create a temporary password
        int num = (int) ((Math.random() * 9 + 1) * 100000);
        String salt = RandomUtil.randomString(6);//用为加密，生成随机数位6位的雪花数(Used as an encryption to generate a snowflake number with 6 random digits)
        String md5Pwd = SecureUtil.md5(num + salt);
        //Generate a new salt and save it to the database along with the new encrypted password
        usersDao.updatePwd(user.getEmail(), md5Pwd, salt);
        // 发送邮件得到临时密码(Send an email to get a temporary password)
        String activationUrl = "A new password has been created, please change it as soon as possible" + num;
        sendMail(activationUrl, user.getEmail());
        resultMap.put("code", 200);
        resultMap.put("message", "Please go to your email address for a temporary password");
        return resultMap;
    }

    /**
     * This method deletes a user account.
     * @param user The user account to be deleted.
     * @return A map containing the result of the operation.
     */
    //执行该方法前应该先执行loginAccount再调用
    @Override
    public Map<String, Object> deleteUser(UsersEntity user) {//还可以通过逻辑删除
        Map<String, Object> resultMap = new ConcurrentHashMap<>();
        //获取该用户名相应的用户名，加密后的密码 和 盐(Get the corresponding user name, encrypted password and salt)
        List<UsersEntity> usersEntityList = usersDao.selectEmail(user.getEmail());

        if (usersEntityList == null || usersEntityList.isEmpty()) {
            resultMap.put("code", 400);
            resultMap.put("message", "This user does not exist or is not registered");
            return resultMap;
        }

        if (usersEntityList.size() > 1) {
            resultMap.put("code", 400);
            resultMap.put("message", "The account is abnormal");
            return resultMap;
        }
        //查询到一个用户，进行密码对比(一个email只有一个用户所以是get（0）)(Query for a user and do a password comparison (an email has only one user so it's get (0)))
        UsersEntity usersEntity = usersEntityList.get(0);

        String md5Pwd = SecureUtil.md5(user.getPassword() + usersEntity.getSalt());//查询到的salt和密码编写的雪花数应该与database对应
        if (!usersEntity.getPassword().equals(md5Pwd)) {
            resultMap.put("code", 400);
            resultMap.put("message", "The password entered is incorrect");
            return resultMap;
        }
        //注销账户
        usersDao.deleteUser(user.getEmail());
        resultMap.put("code", 200);
        resultMap.put("message", "The account has been cancelled");
        return resultMap;
    }

    /**
     * This field holds the email address of the mail sender.
     */
    @Value("${spring.mail.username}")
    private String mailUsername;
    /**
     * This field holds an instance of the JavaMailSender class for sending emails.
     */
    @Resource
    private JavaMailSender javaMailSender;
    /**
     * This field holds an instance of the TemplateEngine class for creating email templates.
     */
    @Resource
    private TemplateEngine templateEngine;

    /**
     * This method sends an email to the specified email address.
     * @param Url The URL to be sent in the email.
     * @param email The email address to send the email to.
     */
    @Override
    public void sendMail(String Url, String email) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
            // 设置邮件主题(Set email subject)
            message.setSubject("Welcome to CharChar Services");
            // 设置邮件发送者(Setting up email senders)
            message.setFrom(mailUsername);
            // 设置邮件接受者，可以多个(Set up email recipients)
            message.setTo(email);
//             设置邮件发送日期(Set email delivery date)
            message.setSentDate(new Date());
            // 创建上下文环境(Creating a Contextual Environment)
            Context context = new Context();
            context.setVariable("Url", Url);
            String text = templateEngine.process("activation-account.html", context);
            // 设置邮件正文(Setting the body of the email)
            message.setText(text, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        // 邮件发送
        javaMailSender.send(mimeMessage);
    }

    /**
     * This method activates a user account based on the confirmation code.
     * @param confirmCode The confirmation code used to activate the account.
     * @return A map containing the result of the operation.
     */
    @Override
    public Map<String, Object> activationAccount(String confirmCode) {
        Map<String, Object> resultMap = new ConcurrentHashMap<>();
        // 根据确认码查询用户(User search by confirmation code)
        UsersEntity user = usersDao.selectUserByConfirmCode(confirmCode);
        // 判断激活时间是否超时(Determine if the activation time has expired)
        if (user.getActivationTime() != null) {
            boolean after = LocalDateTime.now().isAfter(user.getActivationTime());

            if (after) {
                resultMap.put("code", 400);
                resultMap.put("message", "The link is no longer available, please re-register");
                return resultMap;
            }
        } else {
            resultMap.put("code", 400);
            resultMap.put("message", "error,ActivationTime is null");
            return resultMap;
        }
        // 根据确认码查询用户并修改状态值为 1（可用）(Look up the user according to the confirmation code and change the status value to 1 (available))
        int result = usersDao.updateUserByConfirmCode(confirmCode);
        if (result > 0) {
            resultMap.put("code", 200);
            resultMap.put("message", "Successful activation");
        } else {
            resultMap.put("code", 400);
            resultMap.put("message", "Activation failed");
        }
        return resultMap;
    }

    /**
     * Determines if the given email address is valid.
     * @param email The email address to be validated
     * Returns true if the email address is valid, otherwise returns false.
     */
    @Override
    public boolean isEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    /**
     * Updates the verification code for the specified user and sends an email containing the code.
     * @param user the user to update the verification code for
     * @return a map containing the result of the operation, including a code and message
     */
    @Override
    public Map<String, Object> updateVerificationCode(UsersEntity user) {
        Map<String, Object> resultMap = new ConcurrentHashMap<>();

        List<UsersEntity> usersEntityList = usersDao.selectEmail(user.getEmail());


        if (usersEntityList == null || usersEntityList.isEmpty()) {
            resultMap.put("code", 400);
            resultMap.put("message", "This user does not exist or is not registered");
            return resultMap;
        }

        if (usersEntityList.size() > 1) {
            resultMap.put("code", 400);
            resultMap.put("message", "The account is abnormal");
            return resultMap;
        }
        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000;
        String vertifi=String.valueOf(randomNumber);
        usersDao.updateVertificationCode(user.getEmail(),vertifi);
        String activationUrl = "This is your verification code, please keep it safe" + vertifi;
        sendMail(activationUrl, user.getEmail());
        resultMap.put("code", 200);
        resultMap.put("message", "Verification code sent successfully");
        return resultMap;
    }
}