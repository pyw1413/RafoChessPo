package com.rafo.hall.service;

import com.rafo.hall.utils.HallRdb;
import com.rafo.hall.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/9/23.
 */
public class EmailService
{
    private final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private static EmailService instance = new EmailService();
    private ConcurrentHashMap<Integer, Integer> playNewEmailState = new ConcurrentHashMap<>();        //map<玩家ID,存在新邮件>
    private List<Email> emailData = new ArrayList<>();    //仅初始化时候有用							//总的玩家跟邮件有关信息列表
    private ConcurrentHashMap<Integer, RESEmailList> emailList = new ConcurrentHashMap<>();    //玩家邮件map
    private ConcurrentHashMap<Integer, Integer> emailNum = new ConcurrentHashMap<>();        //玩家ID-玩家邮件数
    private ConcurrentHashMap<Integer, Date> lastVisitTime = new ConcurrentHashMap<>();        //玩家ID-玩家上次访问时间

    public EmailService()
    {

    }

    public static EmailService getInstance()
    {
        return instance;
    }

    public WCHaveNewEmailSynRES sendNewNumber(int num)
    {
        WCHaveNewEmailSynRES res = new WCHaveNewEmailSynRES();
        res.setNewNumber(num);
        return res;
    }

    /**
     * 获取邮件
     *
     * @param accountId
     * @param player_uid
     * @return
     */
    public WCVisitEmailRES getEmail(String accountId, int player_uid)
    {
        Email email = HallRdb.getEmail(player_uid);
        WCVisitEmailRES emailRes = new WCVisitEmailRES();
        boolean isInsert = false;
        logger.debug("GWVisitEmailRES getID debug: platform contains {}", player_uid);
        if (email == null)
        {
            isInsert = true;
            email = new Email();
            email.setPlayer_id(player_uid);
            email.setLast_visit_time(new Date());
            email.setEmail_num(0);
            email.setEmail_new(0);
            RESEmailList resEmailList = new RESEmailList();
            //email.setEmail_list(Arrays.toString(resEmailList.getEmailData().toArray()).getBytes());
            playNewEmailState.put(player_uid, 0);
            emailList.put(player_uid, resEmailList);
            emailNum.put(player_uid, 0);
            lastVisitTime.put(player_uid, new Date());
            logger.info("add new player to EmailService! playerId = {}", player_uid);
        }
        if (isInsert)
        {
            HallRdb.putEmail(email);
        } else
        {
            email.setEmail_new(0);//新消息提示
            email.setLast_visit_time(new Date());
            HallRdb.putEmail(email);
        }
        //在获取邮件之前先清理一下过期的邮件
        return null;
    }

}
