package com.rafo.hall.vo;

/**
 * Created by YL.
 * Date: 16-9-28
 */
public class EmailContent
{
    private int id;
    private int email_id;
    private String email_content;
    private String email_desc;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getEmail_id()
    {
        return email_id;
    }

    public void setEmail_id(int email_id)
    {
        this.email_id = email_id;
    }

    public String getEmail_content()
    {
        return email_content;
    }

    public void setEmail_content(String email_content)
    {
        this.email_content = email_content;
    }

    public String getEmail_desc()
    {
        return email_desc;
    }

    public void setEmail_desc(String email_desc)
    {
        this.email_desc = email_desc;
    }
}
