package com.rafo.chess.handlers.room;

import com.kodgames.battle.common.constants.GlobalConstants;
import com.kodgames.battle.entity.room.BGRoomEnterRES;
import com.kodgames.battle.entity.room.GBRoomCreateREQ;
import com.kodgames.battle.entity.room.GBRoomEnterREQ;
import com.kodgames.battle.service.room.RoomHelper;
import com.kodgames.battle.service.room.RoomService;
import com.kodgames.battle.service.server.KodgamesExtension;
import com.rafo.chess.core.GameExtension;
import com.rafo.chess.manager.RedisManager;
import com.rafo.chess.model.LoginRoom;
import com.rafo.chess.model.LoginUser;
import com.rafo.chess.model.RafoRoom;
import com.rafo.chess.service.GameService;
import com.rafo.chess.service.LoginService;
import com.rafo.chess.utils.CmdsUtils;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


public class RoomJoinEventHandler extends BaseServerEventHandler
{
	final static Logger logger = LoggerFactory.getLogger("room");

	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException {
		GameExtension roomExt = (GameExtension) getParentExtension();
		GBRoomEnterREQ message = new GBRoomEnterREQ();
		User user = (User)event.getParameter(SFSEventParam.USER);
		Room room = (Room)event.getParameter(SFSEventParam.ROOM);

		trace("user ["+user.getName()+"] start join room ["+ room.getName()+"] ,is owner?"+ user.getName().equals(room.getOwner().getName()));

		SFSObject extraObj = (SFSObject)event.getParameter(SFSEventParam.OBJECT);
		LoginUser loginUser = new LoginUser(user);
		if(extraObj == null){
			extraObj = new SFSObject();
			extraObj.putInt("roomid",Integer.parseInt(room.getName()));
		}

		Properties props = roomExt.getConfigProperties();
		int serverId = Integer.parseInt(props.getProperty("server.id").trim());

		assembleMessage(loginUser,extraObj,message,serverId);

		BGRoomEnterRES enterRES = null;
		if(room != null) {
			try{
				GameService gameService = roomExt.getGameService();
				if(gameService.getRoom()==null){

					SFSObject createRoomREQ = (SFSObject)room.getProperty("message");
					GBRoomCreateREQ createReq = GBRoomCreateREQ.fromSFSOBject(createRoomREQ);
					roomExt.initService(createReq, loginUser.getId());

					RoomHelper.storeRoom2Redis(getLoginRoom(gameService.getRoom(), serverId));
				}
				enterRES = roomExt.getRoomService().enterRoom(message.getName(), message.getID(), message.getIp(), message.getSex(), message.getHead());

				loginUser.setRoom(Integer.parseInt(room.getName()));
				LoginService.storeUser2redis(loginUser);
				user.setProperty("sfsobj",loginUser.toSFSObject());

				for(User u:room.getUserList()){
					roomExt.send(CmdsUtils.CMD_JOINROOM, enterRES.toRoomEnterResSFSObj(u), u);
				}
			}catch (Exception e){
				logger.debug(System.currentTimeMillis()+"\t"+user.getName()+"\t"+ "joinroom" +"\t"+
						user.getIpAddress()+"\t"+ room.getName() +"\t"+ 0 +"\t" + "failed"+"\t"+"system_err");
				BGRoomEnterRES res = RoomHelper.enterFailed(message,GlobalConstants.SYSTEM_ERROR);
				roomExt.send(CmdsUtils.CMD_JOINROOM, res.toRoomEnterResSFSObj(user), user);
			}

		}else {
			BGRoomEnterRES res = RoomHelper.enterFailed(message,GlobalConstants.ROOM_ENTER_FAILED_NUMBER_ERROR);
			roomExt.send(CmdsUtils.CMD_JOINROOM, res.toRoomEnterResSFSObj(user), user);
			logger.debug(System.currentTimeMillis()+"\t"+user.getName()+"\t"+ "joinroom" +"\t"+
					user.getIpAddress()+"\t"+ room.getName() +"\t"+ 0 +"\t" + "failed"+"\t"+"room_not_exists");
		}

		trace("user ["+user.getName()+"] joined room ["+ room.getName()+"]");

	}

	private void assembleMessage(LoginUser user,ISFSObject param,GBRoomEnterREQ message,int serverId){

		message.setID(user.getId());
		message.setRoomID(param.getInt("roomid"));
		message.setServerID(serverId);
		message.setAccountID(user.getId()+"");
		message.setHead(user.getHead());
		message.setIp(user.getIp());
		message.setName(user.getName());
		message.setSex(user.getSex());
	}


	private LoginRoom getLoginRoom(RafoRoom room, int serverId){
		LoginRoom loginRoom  = new LoginRoom();
		loginRoom.setPlayType(room.getPlayType());
		loginRoom.setBattleTime(room.getCurrRounds());
		loginRoom.setCreateTime(room.getCreateTime());
		loginRoom.setInBattle(room.isInBattle());
		loginRoom.setOwnerAccountID(String.valueOf(room.getOwnerId()));
		loginRoom.setRoomID(room.getRoomId());
		loginRoom.setRoomType(room.getGameType());
		loginRoom.setRoundTotal(room.getTotalRounds());
		loginRoom.setServerId(serverId);
		return loginRoom;
	}


}
