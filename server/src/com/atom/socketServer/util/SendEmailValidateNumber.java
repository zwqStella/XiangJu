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
	private static String host = "smtp.163.com"; // smtp������
    private static String from = "slsserver@163.com"; // �����˵�ַ
    private String to = ""; // �ռ��˵�ַ
    private static String username = "slsserver@163.com"; // �û���
    private static String password = "slsserver001"; // ����
    private static String title = "���λ�÷���ϵͳ�����˻�������֤�ʼ�"; // �ʼ�����
    private int validateNumber;
    

    
    public SendEmailValidateNumber() {
	}

    public int getValidateNumber(){
    	return this.validateNumber;
    }


	public void send(String to) {

        Properties props = new Properties();

        props.put("mail.smtp.host", host);
        // ��Ҫ������Ȩ��Ҳ�����л����������У�飬��������ͨ����֤��һ��Ҫ����һ����
        props.put("mail.smtp.auth", "true");

        // �øո����úõ�props���󹹽�һ��session
        Session session = Session.getDefaultInstance(props);

        // ������������ڷ����ʼ��Ĺ�������console����ʾ������Ϣ��������ʹ
        // �ã�������ڿ���̨��console)�Ͽ��������ʼ��Ĺ��̣�
        session.setDebug(true);

        // ��sessionΪ����������Ϣ����
        MimeMessage message = new MimeMessage(session);
        try {
            // ���ط����˵�ַ
            message.setFrom(new InternetAddress(from));
            // �����ռ��˵�ַ
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            // ���ر���
            message.setSubject(title);

            // ��multipart����������ʼ��ĸ����������ݣ������ı����ݺ͸���
            Multipart multipart = new MimeMultipart();

            // �����ʼ����ı�����
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
                

            contentPart.setText("��ӭ��ʹ����ۣ�����������֤�룺\n" + validateNumber + "\n" + "�����ڽ���ע���������֤���֪���˽������˺ű���������й¶��\n");
            multipart.addBodyPart(contentPart);
            // ��Ӹ���
            //BodyPart messageBodyPart = new MimeBodyPart();
            //DataSource source = new FileDataSource(affix);
            // ��Ӹ���������
            //messageBodyPart.setDataHandler(new DataHandler(source));
            // ��Ӹ����ı���
            // �������Ҫ��ͨ�������Base64�����ת�����Ա�֤������ĸ����������ڷ���ʱ����������
            //multipart.addBodyPart(messageBodyPart);

            // ��multipart����ŵ�message��
            message.setContent(multipart);
            // �����ʼ�
            message.saveChanges();
            // �����ʼ�
            Transport transport = session.getTransport("smtp");
            // ���ӷ�����������
            transport.connect(host, username, password);
            // ���ʼ����ͳ�ȥ
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
