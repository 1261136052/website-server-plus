package com.xxforest.baseweb.core;

import com.sun.mail.imap.IMAPStore;
import com.sun.mail.imap.IdleManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

//@Service
public class EmailService {

    @Value("${email.host}")
    private String host; // 邮箱服务器地址

    @Value("${email.port}")
    private int port; // 邮箱服务器端口号

    @Value("${email.username}")
    private String username; // 邮箱地址

    @Value("${email.password}")
    private String password; // 邮箱密码


    @PostConstruct
    public void initialize()  {
        try {
            receive("Inbox");
            Logger.getLogger(this.getClass().getSimpleName()).info("EmailService开始工作");
        } catch (javax.mail.MessagingException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    /**
     * 接收指定目录下的所有电子邮件
     *
     * @param folderName 目录名称，例如"Inbox"
     * @throws MessagingException
     */
    public void receive(String folderName) throws MessagingException, javax.mail.MessagingException, IOException, InterruptedException {


        // 创建一个IMAP协议的邮件会话
        Properties props = System.getProperties();
        props.setProperty("mail.imap.ssl.enable", "true"); // 启用SSL加密
        Session session = Session.getDefaultInstance(props, null);
        Store store = session.getStore("imap");
        store.connect(host, port, username, password);
        // 获取指定目录的邮件夹
        Folder folder = store.getFolder(folderName);
        folder.open(Folder.READ_ONLY);

        // 注册邮件数量变化监听器
        folder.addMessageCountListener(new MessageCountAdapter() {
            @Override
            public void messagesAdded(MessageCountEvent event) {
                Message[] messages = event.getMessages();
                for (Message message : messages) {
                    try {
                        System.out.println("New message: " + message.getSubject());
                    } catch (MessagingException | javax.mail.MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // 启动 IDLE 监听
//        ExecutorService es = Executors.newCachedThreadPool();
//        IdleManager idleManager = new IdleManager(session, es);
//        idleManager.watch(folder);

//        // 保持监听状态
//        while (true) {
//            Thread.sleep(5000);
//        }
        // 遍历邮件夹中的所有邮件并打印出来
        Message[] messages = folder.getMessages();
        for (Message message : messages) {
            System.out.println("Subject: " + message.getSubject());
            System.out.println("From: " + message.getFrom()[0]);
            System.out.println("Text: " + getText(message));
        }
        // 启动 IDLE 命令
//        ExecutorService es = Executors.newCachedThreadPool();
//        IdleManager idleManager = new IdleManager(session, es);
//        idleManager.watch(folder);
//        // 保持程序运行，等待新邮件的到达
//        while (true) {
//            try {
//                Thread.sleep(1000); // 可以根据需要调整等待时间
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

//         关闭邮件夹和连接
//         folder.close(false);
//         store.close();
    }



    /**
     * 获取邮件的正文内容
     *
     * @param message 邮件消息对象
     * @return 邮件的正文内容
     * @throws MessagingException
     * @throws IOException
     */
    private String getText(Message message) throws MessagingException, IOException, javax.mail.MessagingException {
        if (message.isMimeType("text/plain")) {
            return message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart multipart = (MimeMultipart) message.getContent();
            int count = multipart.getCount();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < count; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (bodyPart.isMimeType("text/plain")) {
                    sb.append(bodyPart.getContent().toString());
                }
            }
            return sb.toString();
        } else {
            return "";
        }
    }
}
