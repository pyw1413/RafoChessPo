package com.kodgames.battle.entity.record;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/17.
 */
// 一个房间历史战绩统计，包含四个玩家的得分，名称等
public class RoomStatisticsPROTO {

    private long startTime ;
    private int roomID ;
    private List<PlayerPointInfoPROTO> playerInfo = new ArrayList<>(); // 四个玩家在房间内的总积分统计

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public List<PlayerPointInfoPROTO> getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(List<PlayerPointInfoPROTO> playerInfo) {
        this.playerInfo = playerInfo;
    }

    public void addPlayerInfo(PlayerPointInfoPROTO playerPointInfoPROTO) {
        this.playerInfo.add(playerPointInfoPROTO);
    }
    
    public byte[] toByteArray() {
    	byte[] bytes = null;  
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();  
            ObjectOutputStream oo = new ObjectOutputStream(bo);  
            oo.writeObject(this);  
            bytes = bo.toByteArray();       
            bo.close();  
            oo.close();  
        } catch (Exception e) {  
            System.out.println("translation" + e.getMessage());  
            e.printStackTrace();  
        }  
        return bytes;    
    }
    
	public static RoomStatisticsPROTO parseFrom(byte[] bytes) {
		Object obj = null;
		try {
			// bytearray to object
			ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
			ObjectInputStream oi = new ObjectInputStream(bi);

			obj = oi.readObject();
			bi.close();
			oi.close();
		} catch (Exception e) {
			System.out.println("translation" + e.getMessage());
			e.printStackTrace();
		}
		return (RoomStatisticsPROTO) obj;
	}
}
