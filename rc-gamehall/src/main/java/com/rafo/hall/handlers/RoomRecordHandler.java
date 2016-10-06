package com.rafo.hall.handlers;

import com.rafo.hall.utils.CmdsUtils;
import com.rafo.hall.vo.PlayerPointInfoPROTO;
import com.rafo.hall.vo.RoomStatisticsPROTO;
import com.rafo.hall.vo.WCRoomRecordRES;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public class RoomRecordHandler extends BaseClientRequestHandler{
    @Override
    public void handleClientRequest(User user, ISFSObject isfsObject) {
        WCRoomRecordRES res = new WCRoomRecordRES();
        List<RoomStatisticsPROTO> stats = new ArrayList<RoomStatisticsPROTO>();
        res.setResult(0);
        RoomStatisticsPROTO stat = new RoomStatisticsPROTO();
        stat.setRoomID(1);
        stat.setStartTime(System.currentTimeMillis());
        stat.setRecordID(1);
        PlayerPointInfoPROTO point = new PlayerPointInfoPROTO();
        point.setPlayerID(1);
        point.setNickName("test1");
        point.setChair(1);
        point.setPoint(10);
        point.setHead("head");
        stat.addPlayerInfo(point);



        point = new PlayerPointInfoPROTO();
        point.setPlayerID(2);
        point.setNickName("test2");
        point.setChair(2);
        point.setPoint(-10);
        point.setHead("head");
        stat.addPlayerInfo(point);

        point = new PlayerPointInfoPROTO();
        point.setPlayerID(3);
        point.setNickName("test3");
        point.setChair(3);
        point.setPoint(5);
        point.setHead("head");
        stat.addPlayerInfo(point);

        point = new PlayerPointInfoPROTO();
        point.setPlayerID(4);
        point.setNickName("test4");
        point.setChair(4);
        point.setPoint(5);
        point.setHead("head");
        stat.addPlayerInfo(point);

        stats.add(stat);
        stat = new RoomStatisticsPROTO();
        stat.setRoomID(1);
        stat.setStartTime(System.currentTimeMillis());
        stat.setRecordID(2);

        point = new PlayerPointInfoPROTO();
        point.setPlayerID(1);
        point.setNickName("test1");
        point.setChair(1);
        point.setPoint(10);
        point.setHead("head");
        stat.addPlayerInfo(point);



        point = new PlayerPointInfoPROTO();
        point.setPlayerID(2);
        point.setNickName("test2");
        point.setChair(2);
        point.setPoint(-10);
        point.setHead("head");
        stat.addPlayerInfo(point);

        point = new PlayerPointInfoPROTO();
        point.setPlayerID(3);
        point.setNickName("test3");
        point.setChair(3);
        point.setPoint(5);
        point.setHead("head");
        stat.addPlayerInfo(point);

        point = new PlayerPointInfoPROTO();
        point.setPlayerID(4);
        point.setNickName("test4");
        point.setChair(4);
        point.setPoint(5);
        point.setHead("head");
        stat.addPlayerInfo(point);

        stats.add(stat);
        res.setRoomStatistics(stats);



        getParentExtension().send(CmdsUtils.CMD_ROOMRECORD , res.toObject() , user);
    }
}
