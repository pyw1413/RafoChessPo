package com.kodgames.battle.service.room;

import com.kodgames.battle.common.constants.GlobalConstants;
import com.kodgames.battle.entity.room.BGRoomEnterRES;
import com.kodgames.battle.entity.room.GBRoomEnterREQ;
import com.rafo.chess.exception.PersistException;
import com.rafo.chess.manager.MySQLManager;
import com.rafo.chess.manager.RedisManager;
import com.rafo.chess.model.LoginRoom;
import com.rafo.chess.model.LoginUser;
import com.rafo.chess.service.LoginService;
import com.rafo.chess.utils.CmdsUtils;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.match.BoolMatch;
import com.smartfoxserver.v2.entities.match.MatchExpression;
import com.smartfoxserver.v2.entities.match.RoomProperties;
import com.smartfoxserver.v2.entities.match.StringMatch;
import com.smartfoxserver.v2.entities.variables.RoomVariable;
import com.smartfoxserver.v2.entities.variables.SFSRoomVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.exceptions.SFSVariableException;
import com.smartfoxserver.v2.extensions.SFSExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class RoomHelper {

    public static void storeRoom2Redis(LoginRoom loginRoom) throws PersistException {
        RedisManager.getInstance().hMSetWithException("roomid."+Integer.toString(loginRoom.getRoomID()),loginRoom.toStrMap());
    }

    public static LoginRoom getLoginRoomFromRedis(int roomid) throws PersistException {
        LoginRoom loginRoom = null;
        String[] fileds = new String[]{"rndt", "serId", "rmId", "isb", "pt", "rmt", "ct","ownId","bt"};

        List<String> vList = RedisManager.getInstance().hMGetWithException("roomid." + roomid, fileds);

        if (vList != null) {
            if(vList.get(0)!= null){
                loginRoom = new LoginRoom();
                loginRoom.setRoundTotal(Integer.parseInt(vList.get(0)));
                loginRoom.setServerId(Integer.parseInt(vList.get(1)));
                loginRoom.setRoomID(roomid);
                loginRoom.setInBattle("F".equals(vList.get(3))?false:true);
                loginRoom.setPlayType(Integer.parseInt(vList.get(4)));
                loginRoom.setRoomType(Integer.parseInt(vList.get(5)));
                loginRoom.setCreateTime(Long.parseLong(vList.get(6)));
                loginRoom.setOwnerAccountID(vList.get(7));
                loginRoom.setBattleTime(Integer.parseInt(vList.get(8)));
            }
        }
        return loginRoom;
    }


    public static void storeRoom2Mysql(LoginRoom loginRoom) throws PersistException {
        String sql = "insert into tbl_room (roomid,ownerAccountID,roundTotal,battleTime,playType," +
                "createTime,roomType,isInBattle,serverId) values (?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE " +
                "battleTime = values(battleTime),isInBattle =VALUES(isInBattle)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {

            conn = MySQLManager.getInstance().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1,loginRoom.getRoomID());
            ps.setString(2,loginRoom.getOwnerAccountID());
            ps.setInt(3,loginRoom.getRoundTotal());
            ps.setInt(4,loginRoom.getBattleTime());
            ps.setInt(5,loginRoom.getPlayType());
            ps.setLong(6,loginRoom.getCreateTime());
            ps.setInt(7,loginRoom.getRoomType());
            ps.setInt(8,loginRoom.isInBattle()?0:1);
            ps.setInt(9,loginRoom.getServerId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistException("mysql error"+ sql);
        } finally {
            MySQLManager.getInstance().close(null, ps, conn);
        }

    }

    public static boolean couldDestroy(Room room){
        int uc = room.getUserList().size();
        int agreeUC = 0;
        for(User u:room.getUserList()){
            UserVariable userVariable = u.getVariable("voteResult");
            if(userVariable!=null){
                String vr = userVariable.getStringValue();
                if(vr != null){
                    String[] kv = vr.split(":");
                    if(room.getName().equals(kv[0])){
                        if(!"1".equals(kv[1])){
                            agreeUC = agreeUC +1;
                        }
                    }
                }
            }

        }
        return agreeUC == uc;
    }

    public static boolean checkRoomId(int roomId, User user, SFSExtension extension){
        MatchExpression exp = new MatchExpression(RoomProperties.IS_GAME, BoolMatch.EQUALS, true).and (RoomProperties.NAME, StringMatch.EQUALS,roomId+"");
        List<Room> rooms = extension.getApi().findRooms(user.getZone().getRoomList(), exp, 1);
        boolean theServer = rooms.size() < 1;
        boolean global = RedisManager.getInstance().exists("roomid."+Integer.toString(roomId));
        return theServer&!global;

    }

    public static boolean subCard(Room room,int currentBattleTime,SFSExtension extension) throws SFSVariableException, PersistException {
        Logger logger = LoggerFactory.getLogger("room");
        boolean result = false;
        RoomVariable rv = room.getVariable("isSubcard");
        SFSObject createRoomREQ = (SFSObject)room.getProperty("message");
        int count = createRoomREQ.getInt("count");
        int willSubCard = 0;
        if(count == 0){
            willSubCard = 1;
        }else if(count == 1){
            willSubCard = 2;
        }

        if(!rv.getBoolValue() && currentBattleTime>=1){
            RoomVariable isSubCard = new SFSRoomVariable("isSubcard", true);
            isSubCard.setHidden(true);
            room.setVariable(isSubCard);
            extension.getApi().setRoomVariables(room.getOwner(),room,Arrays.asList(isSubCard));

            LoginUser loginUser = new LoginUser(room.getOwner());
            int now_card = loginUser.getCard()-willSubCard;

            room.getOwner().setProperty("sfsobj",loginUser.toSFSObject());

            loginUser.setCard(now_card);
            LoginService.updateUserCard(Integer.parseInt(room.getOwner().getName()),willSubCard);
            LoginService.storeUser2redis(loginUser);
        }

        logger.debug(System.currentTimeMillis()+"\t"+room.getOwner().getName()+"\t"+room.getOwner().getIpAddress()+"\t"+
                room.getName()+"\t"+count+"\t"+willSubCard);

        return result;

    }


    public static void destroyRoom(Room room) throws SFSVariableException, PersistException {
        room.destroy();
       //RoomHelper.destroyMysqlRoom(room);
        RoomHelper.destroyRedisRoom(room);
        RoomHelper.clearUserRoomInfo(room);
    }

    public static void clearUserRoomInfo(Room room) throws PersistException {
        List<User> userList = room.getUserList();
        for(User u:userList){
            LoginUser loginUser = new LoginUser(u);
            loginUser.setRoom(0);
            //LoginService.storeUser2mysql(loginUser);
            LoginService.storeUser2redis(loginUser);
            u.setProperty("sfsobj",loginUser.toSFSObject());
        }
    }


    public static void destroy(Room room,SFSExtension extension,ISFSObject resp) throws PersistException, SFSVariableException {
        RoomHelper.destroyRoom(room);
        resp.putInt("result", GlobalConstants.WC_VOTE_DESTROY_SUCCESS);
        extension.send(CmdsUtils.CMD_ROOM_DESTROY_VOTE_RESP,resp,room.getUserList());
    }



    public static boolean checkCard(int card,int count){
        if(count == 0){
            return card >= 1;
        }else if(count == 1) {
            return card >= 2;
        }
        return false;
    }

    public static BGRoomEnterRES enterFailed(GBRoomEnterREQ message, int errorCode)
    {
        BGRoomEnterRES res = new BGRoomEnterRES();
        res.setResult(errorCode);
        res.setRoomID(message.getRoomID());
        res.setApplierAccountID(message.getAccountID());
        res.setApplierID(message.getID());
        return res;
    }

    public static com.smartfoxserver.v2.entities.Room getRoom(String roomId, User user, SFSExtension extension){
        MatchExpression exp = new MatchExpression(RoomProperties.IS_GAME, BoolMatch.EQUALS, true).and (RoomProperties.NAME, StringMatch.EQUALS,roomId);
        List<com.smartfoxserver.v2.entities.Room> rooms = extension.getApi().findRooms(user.getZone().getRoomList(), exp, 1);
        return rooms.size()>=1?rooms.get(0):null;
    }

    public static void queryOther(String roomId,User user,SFSExtension extension) {
        ISFSObject resp = new SFSObject();
        resp.putUtfString("roomId", roomId);
        resp.putUtfString("uid", user.getVariable("uid").getStringValue());
        com.smartfoxserver.v2.entities.Room room = getRoom(roomId, user,extension);
        List<User> users = new ArrayList<User>();
        for (User u : room.getUserList()) {
            if (!u.getVariable("uid").equals(user.getVariable("uid"))) {
                users.add(u);
            }
        }

        extension.send("dissolveroom.ask", resp, users);
    }

    public static void destroyRedisRoom(Room room) {
        RedisManager.getInstance().del("roomid."+Integer.parseInt(room.getName()));
    }

    public static void destroyMysqlRoom(Room room){
        String sql = "delete from tbl_room where roomid="+room.getName();

        Connection conn = null;
        Statement ps = null;
        try {

            conn = MySQLManager.getInstance().getConnection();
            ps = conn.createStatement();
            ps.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MySQLManager.getInstance().close(null, ps, conn);
        }

    }


    public enum ERR{
        SUCESS(0,"success"),NOEXISTS(2001,"room not exists"),FULL(2002,"room is full"),SYSERR(2003,"system error");
        private final int value;
        private final String msg;
        ERR(int value,String msg){
            this.value = value;
            this.msg = msg;
        }
        public int getValue() {
            return value;
        }
        public String getMsg(){
            return msg;
        }
    }


    public enum SIT{
        EAST(1,"eUid"),SOUTH(2,"sUid"),WEST(3,"wUid"),NORTH(4,"nUid");
        private final int value;
        private final String msg;
        SIT(int value,String msg){
            this.value = value;
            this.msg = msg;
        }
        public int getValue() {
            return value;
        }
        public String getMsg(){
            return msg;
        }
    }
}
