package com.rafo.hall.service;

import com.rafo.hall.vo.WCMarqueeSYN;
import com.smartfoxserver.v2.entities.User;

/**
 * Created by Administrator on 2016/9/22.
 */
public class MarqueeService {

    private static  MarqueeService instance =  new MarqueeService();

    private MarqueeService(){

    }

    public static MarqueeService getInstance() {
        return instance;
    }

    public WCMarqueeSYN send(String msg , int rollTimes , String color) {
        WCMarqueeSYN wcMarqueeSYN = new WCMarqueeSYN();
        wcMarqueeSYN.setContent(msg);
        wcMarqueeSYN.setRollTimes(rollTimes);
        wcMarqueeSYN.setColor(color);
        return wcMarqueeSYN;
    }
}
