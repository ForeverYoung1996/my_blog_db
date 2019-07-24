package com.site.blog.my.core.service;

public interface MailService {

    //发送普通邮件
    void sendSimpleMail(String to,String subject,String content);
}
