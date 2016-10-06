package com.rafo.chess.service;

import com.rafo.chess.core.ROCHExtension;
import com.rafo.chess.exception.PersistException;
import com.rafo.chess.manager.MySQLManager;
import com.rafo.chess.manager.RedisManager;
import com.rafo.chess.model.GameServer;
import com.rafo.chess.model.LoginUser;
import com.smartfoxserver.v2.entities.User;

import java.sql.*;
import java.util.*;

/**
 * Created by Administrator on 2016/9/19.
 */
public class LoginService {
    public static LoginUser getUserFromRedis(String uid) throws PersistException {
        LoginUser loginUser = null;
        String[] fileds = new String[]{"ID", "name", "province", "city", "country",
                "head", "sex","status","card", "room", "ip", "time","account",
                "refreshToken","haveNewEmail","points","total","forbitTime","unionid","time","token","expire"};

        List<String> vList = RedisManager.getInstance().hMGetWithException("uid." + uid, fileds);

        if (vList != null) {
            if(vList.get(0)!= null){
                loginUser = new LoginUser();
                loginUser.setId(Integer.parseInt(vList.get(0)));
                loginUser.setName(vList.get(1)==null?"guest":vList.get(1));
                loginUser.setProvince(vList.get(2)==null?"":vList.get(2));
                loginUser.setCity(vList.get(3)==null?"":vList.get(3));
                loginUser.setCountry(vList.get(4)==null?"":vList.get(4));
                loginUser.setHead(vList.get(5)==null?"":vList.get(5));
                loginUser.setSex(vList.get(6)==null?"1":vList.get(6));
                loginUser.setStatus(Integer.parseInt(vList.get(7)==null?"0":vList.get(7)));
                loginUser.setCard(Integer.parseInt(vList.get(8)==null?"0":vList.get(8)));
                loginUser.setRoom(Integer.parseInt(vList.get(9)==null?"0":vList.get(9)));
                loginUser.setIp(vList.get(10)==null?"":vList.get(10));
                loginUser.setTimestamp(Long.parseLong(vList.get(11)==null?"0":vList.get(11)));
                loginUser.setAccount(vList.get(12));
                loginUser.setRefreshToken(vList.get(13)==null?"":vList.get(13));
                loginUser.setHaveNewEmail(Integer.parseInt(vList.get(14)==null?"0":vList.get(14)));
                loginUser.setPoints(Integer.parseInt(vList.get(15)==null?"0":vList.get(15)));
                loginUser.setTotal(Integer.parseInt(vList.get(16)==null?"0":vList.get(16)));
                loginUser.setForbitTime(vList.get(17)==null?"":vList.get(17));
                loginUser.setUnionid(vList.get(18)==null?"":vList.get(18));
                loginUser.setTimestamp(Long.parseLong(vList.get(19)==null?"0":vList.get(19)));
                loginUser.setToken(vList.get(20)==null?"":vList.get(20));
                loginUser.setExpire(Long.parseLong(vList.get(21)==null?"0":vList.get(21)));

            }
        }
        return loginUser;
    }

    public static void storeUser2redis(LoginUser loginUser) throws PersistException {
        RedisManager.getInstance().hMSetWithException("uid."+loginUser.getId(),loginUser.toStrMap());
    }

    public static Map<Integer,GameServer> getGameServerMap() throws PersistException {
        Map<Integer,GameServer> serverMap = new HashMap<Integer, GameServer>();
        String  sql = "select * from tbl_server";
        Connection conn = null;
        Statement ps = null;
        try{
            conn = MySQLManager.getInstance().getConnection();
            ps = conn.createStatement();
            ResultSet rs = ps.executeQuery(sql);
            while (rs.next()){
                GameServer server = new GameServer();
                int serverId = rs.getInt("id");
                server.setId(serverId);
                server.setIp(rs.getString("ip"));
                server.setPort(rs.getInt("port"));
                server.setMaxUser(rs.getInt("maxuser"));
                serverMap.put(serverId,server);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistException("mysql error"+ sql);
        } finally {
            MySQLManager.close(null, ps, conn);
        }
        return serverMap;
    }

    public static void storeUser2mysql(LoginUser loginUser) throws PersistException {

        String sql = "insert into tbl_player (id,account,name,head,card,room,ip,points,total,forbitTime,sex,haveNewEmail," +
                "unionid,province,city,country,timestamp,status) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE" +
                " name=values(name),head=values(head),card=values(card),room=values(room),ip=values(ip),points=values(points)," +
                "total=values(total),haveNewEmail=values(haveNewEmail),timestamp=values(timestamp),status=values(status)";

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = MySQLManager.getInstance().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1,loginUser.getId());
            ps.setString(2,loginUser.getAccount());
            ps.setString(3,loginUser.getName());
            ps.setString(4,loginUser.getHead());
            ps.setInt(5,loginUser.getCard());
            ps.setInt(6,loginUser.getRoom());
            ps.setString(7,loginUser.getIp());
            ps.setInt(8,loginUser.getPoints());
            ps.setInt(9,loginUser.getTotal());
            ps.setString(10,loginUser.getForbitTime());
            ps.setString(11,loginUser.getSex());
            ps.setInt(12,loginUser.getHaveNewEmail());
            ps.setString(13,loginUser.getUnionid());
            ps.setString(14,loginUser.getProvince());
            ps.setString(15,loginUser.getCity());
            ps.setString(16,loginUser.getCountry());
            ps.setLong(17,loginUser.getTimestamp());
            ps.setInt(18,loginUser.getStatus());
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistException("mysql error"+ sql);
        } finally {
            MySQLManager.close(null, ps, conn);
        }
    }


    public static void updateUserCard(int uid,int sub) throws PersistException {
        String sql = "UPDATE tbl_player SET card=card-"+sub+" ,cardConsume=cardConsume+1 WHERE id="+uid;

        Connection conn = null;
        Statement ps = null;
        try {
            conn = MySQLManager.getInstance().getConnection();
            ps = conn.createStatement();
            ps.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new PersistException("mysql error"+ sql);
        } finally {
            MySQLManager.close(null, ps, conn);
        }

    }

    public static void updateSeverUserCount(int serverId, int userCount) throws PersistException {
        Map<String,String> map = new HashMap<String ,String>();
        map.put("serverId",Integer.toString(serverId));
        map.put("uc",Integer.toString(userCount));
        RedisManager.getInstance().hMSetWithException("server."+serverId,map);
    }

}
