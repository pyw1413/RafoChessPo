package com.rafo.hall.vo;

import java.util.Date;

/**
 * Created by YL.
 * Date: 16-9-27
 */
public class Email
{
    private int id;
    private int player_id;
    private int email_new;
    private int email_num;
    private Date last_visit_time;
    private byte[] email_list;

    public Email()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getPlayer_id()
    {
        return player_id;
    }

    public void setPlayer_id(int player_id)
    {
        this.player_id = player_id;
    }

    public int getEmail_new()
    {
        return email_new;
    }

    public void setEmail_new(int email_new)
    {
        this.email_new = email_new;
    }

    public int getEmail_num()
    {
        return email_num;
    }

    public void setEmail_num(int email_num)
    {
        this.email_num = email_num;
    }

    public Date getLast_visit_time()
    {
        return last_visit_time;
    }

    public void setLast_visit_time(Date last_visit_time)
    {
        this.last_visit_time = last_visit_time;
    }

    public byte[] getEmail_list()
    {
        return email_list;
    }

    public void setEmail_list(byte[] email_list)
    {
        this.email_list = email_list;
    }
}
