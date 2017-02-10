package com.atom.socketServer.util;

import java.util.*;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;

import com.sun.mail.imap.protocol.BASE64MailboxEncoder;

import java.util.*;
import javax.activation.*;

public class SendEmailValidateNumber {
	private static String host = "smtp.163.com"; // smtp服务器
    private static String from = "slsserver@163.com"; // 发件人地址
    private String to = ""; // 收件人地址
    private static String username = "slsserver@163.com"; // 用户名
    private static String password = "slsserver001"; // 密码
    private static String title = "想聚位置分享系统共享账户创建验证邮件"; // 邮件标题
    private int validateNumber;
    

    
    public SendEmailValidateNumber() {
	}

    public int getValidateNumber(){
    	return this.validateNumber;
    }


	public void send(String to) {

        Properties props = new Properties();

        props.put("mail.smtp.host", host);
        // 需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
        props.put("mail.smtp.auth", "true");

        // 用刚刚设置好的props对象构建一个session
        Session session = Session.getDefaultInstance(props);

        // 有了这句便可以在发送邮件的过程中在console处显示过程信息，供调试使
        // 用（你可以在控制台（console)上看到发送邮件的过程）
        session.setDebug(true);

        // 用session为参数定义消息对象
        MimeMessage message = new MimeMessage(session);
        try {
            // 加载发件人地址
            message.setFrom(new InternetAddress(from));
            // 加载收件人地址
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            // 加载标题
            message.setSubject(title);

            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();

            // 设置邮件的文本内容
            BodyPart contentPart = new MimeBodyPart();
            
            int[] array = {0,1,2,3,4,5,6,7,8,9};
            Random rand = new Random();
            for (int i = 10; i > 1; i--) {
                int index = rand.nextInt(i);
                int tmp = array[index];
                array[index] = array[i - 1];
                array[i - 1] = tmp;
            }
            for(int i = 0; i < 6; i++){
            	this.validateNumber = validateNumber * 10 + array[i];
            }
                

            contentPart.setText("欢迎您使用想聚！这是您的验证码：\n" + validateNumber + "\n" + "您正在进行注册操作【验证码告知他人将导致账号被盗，请勿泄露】\n");
            multipart.addBodyPart(contentPart);
            // 添加附件
            //BodyPart messageBodyPart = new MimeBodyPart();
            //DataSource source = new FileDataSource(affix);
            // 添加附件的内容
            //messageBodyPart.setDataHandler(new DataHandler(source));
            // 添加附件的标题
            // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
            //multipart.addBodyPart(messageBodyPart);

            // 将multipart对象放到message中
            message.setContent(multipart);
            // 保存邮件
            message.saveChanges();
            // 发送邮件
            Transport transport = session.getTransport("smtp");
            // 连接服务器的邮箱
            transport.connect(host, username, password);
            // 把邮件发送出去
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
