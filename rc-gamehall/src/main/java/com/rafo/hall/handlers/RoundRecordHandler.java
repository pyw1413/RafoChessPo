package com.rafo.hall.handlers;

import com.rafo.hall.utils.CmdsUtils;
import com.rafo.hall.vo.PlayerPointInfoPROTO;
import com.rafo.hall.vo.RoundDataPROTO;
import com.rafo.hall.vo.WCRoundRecordRES;
import com.smartfoxserver.v2.controllers.SystemRequest;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

/**
 * Created by Administrator on 2016/9/23.
 */
public class RoundRecordHandler extends BaseClientRequestHandler {
    @Override
    public void handleClientRequest(User user, ISFSObject isfsObject) {
        Integer recordID = isfsObject.getInt("recordID");
        Integer roomID = isfsObject.getInt("roomID");

        WCRoundRecordRES res = new WCRoundRecordRES();
        res.setResult(0);
        RoundDataPROTO data = new RoundDataPROTO();
        data.setStartTime(System.currentTimeMillis());
        data.setId(1);
        PlayerPointInfoPROTO vo = new PlayerPointInfoPROTO();
        vo.setPlayerID(1);vo.setNickName("test");vo.setChair(1);vo.setHead("head");vo.setPoint(10);
        data.getPlayerInfo().add(vo);
        vo = new PlayerPointInfoPROTO();
        vo.setPlayerID(2);vo.setNickName("test2");vo.setChair(1);vo.setHead("head");vo.setPoint(-10);
        data.getPlayerInfo().add(vo);
        vo = new PlayerPointInfoPROTO();
        vo.setPlayerID(3);vo.setNickName("test3");vo.setChair(1);vo.setHead("head");vo.setPoint(156);
        data.getPlayerInfo().add(vo);
        vo = new PlayerPointInfoPROTO();
        vo.setPlayerID(4);vo.setNickName("test4");vo.setChair(1);vo.setHead("head");vo.setPoint(110);
        data.getPlayerInfo().add(vo);
        res.getRoundData().add(data);

        data = new RoundDataPROTO();
        data.setStartTime(System.currentTimeMillis());
        data.setId(1);
        vo = new PlayerPointInfoPROTO();
        vo.setPlayerID(1);vo.setNickName("test");vo.setChair(1);vo.setHead("head");vo.setPoint(10);
        data.getPlayerInfo().add(vo);
        vo = new PlayerPointInfoPROTO();
        vo.setPlayerID(2);vo.setNickName("test2");vo.setChair(1);vo.setHead("head");vo.setPoint(-10);
        data.getPlayerInfo().add(vo);
        vo = new PlayerPointInfoPROTO();
        vo.setPlayerID(3);vo.setNickName("test3");vo.setChair(1);vo.setHead("head");vo.setPoint(156);
        data.getPlayerInfo().add(vo);
        vo = new PlayerPointInfoPROTO();
        vo.setPlayerID(4);vo.setNickName("test4");vo.setChair(1);vo.setHead("head");vo.setPoint(110);
        data.getPlayerInfo().add(vo);
        res.getRoundData().add(data);

        send(CmdsUtils.CMD_ROUNDRECORD , res.toObject() , user);
    }
}
