package com.rafo.hall.utils;

import com.rafo.hall.manager.RedisManager;
import com.rafo.hall.vo.Email;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YL.
 * Date: 16-9-28
 */
public class HallRdb
{
    public static final String REDIS_EMAIL_KEY = "email.";
    public static final String REDIS_PLAYER_EMAIL_KEY = "player_email.";

    public static void putEmail(Email email)
    {
        String key = REDIS_EMAIL_KEY + email.getPlayer_id();
        Map<String, String> emailMap = new HashMap<>();
        emailMap.put("id", String.valueOf(email.getId()));
        emailMap.put("player_id", String.valueOf(email.getPlayer_id()));
        emailMap.put("email_new", String.valueOf(email.getEmail_new()));
        emailMap.put("email_num", String.valueOf(email.getEmail_num()));
        emailMap.put("email_list", Arrays.toString(email.getEmail_list()));
        RedisManager.getInstance().hMSet(key, emailMap);
    }


    public static Email getEmail(int playerUid)
    {
        String key = REDIS_EMAIL_KEY + playerUid;
        Map<String, String> emailMap = RedisManager.getInstance().hMGetAll(key);
        Email email = null;
        if(emailMap.size() != 0)
        {
            email = new Email();
            email.setId(Integer.parseInt(emailMap.get("id")));
            email.setPlayer_id(Integer.parseInt(emailMap.get("player_id")));
            email.setEmail_new(Integer.parseInt(emailMap.get("email_new")));
            email.setEmail_num(Integer.parseInt(emailMap.get("email_num")));
            email.setEmail_list(emailMap.get("email_list").getBytes());
        }
        return email;
    }
}
