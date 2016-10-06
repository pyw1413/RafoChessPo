package com.kodgames.battle.action.room;

import com.kodgames.battle.common.constants.GlobalConstants;
import com.kodgames.battle.entity.room.GBRoomCreateREQ;
import com.kodgames.battle.service.room.RoomHelper;
import com.rafo.chess.core.ROCHExtension;
import com.rafo.chess.manager.RedisManager;
import com.rafo.chess.model.LoginUser;
import com.rafo.chess.service.LoginService;
import com.rafo.chess.utils.CmdsUtils;
import com.rafo.chess.utils.Constants;
import com.smartfoxserver.v2.api.CreateRoomSettings;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.SFSRoomRemoveMode;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.RoomVariable;
import com.smartfoxserver.v2.entities.variables.SFSRoomVariable;
import com.smartfoxserver.v2.entities.variables.SFSUserVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.exceptions.SFSCreateRoomException;
import com.smartfoxserver.v2.exceptions.SFSJoinRoomException;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.game.CreateSFSGameSettings;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class GBRoomCreateREQAction extends BaseClientRequestHandler
{
	final static Logger logger = LoggerFactory.getLogger("room");

	@Override
	public void handleClientRequest(User user, ISFSObject isfsObject) {

		ROCHExtension gameExt = (ROCHExtension) getParentExtension();
		Properties props = gameExt.getConfigProperties();
		int serverId = Integer.parseInt(props.getProperty("server.id").trim());
		int roomId = RandomUtils.nextInt(999999);

		String roomIdStr = String.format("%06d", roomId);

		GBRoomCreateREQ message = new GBRoomCreateREQ();
		assembleMessage(message,isfsObject,user,serverId,roomId);
		LoginUser loginUser = new LoginUser(user);
		int card = loginUser.getCard();
		boolean flag = true;
		ISFSObject resp = new SFSObject();
		int round = 0;

		while(flag){
			round++;
			if(!RoomHelper.checkCard(card,message.getCount())){
				flag = false;
				resp.putInt("result",GlobalConstants.ROOM_CREATE_FAILED_ROOMCARD_NOT_ENOUGTH);
				resp.putUtfString("msg","left card not enough");
				gameExt.send(CmdsUtils.CMD_CREATROOM,resp,user);
				logger.debug(System.currentTimeMillis()+"\t"+user.getName()+"\t"+ "creatroom" +"\t"+
						user.getIpAddress()+"\t"+ roomId +"\t"+ round +"\t" + "failed"+"\t"+"card_not_enough");

			} else if(RoomHelper.checkRoomId(roomId, user, gameExt)){
				flag = false;
				CreateSFSGameSettings settings = new CreateSFSGameSettings();
				settings.setExtension(new CreateRoomSettings.RoomExtensionSettings(Constants.EXTENSION_ID,Constants.EXTENSIONS_CLASS));
				settings.setName(roomIdStr);
				settings.setGame(true);
				settings.setMaxUsers(4);
				Map<Object,Object> map = new HashMap<Object,Object>();
				map.put("message",message.toSFSObject());
				settings.setRoomProperties(map);
				settings.setAutoRemoveMode(SFSRoomRemoveMode.NEVER_REMOVE);
				settings.setGame(true);

				try {

					Room new_room = getApi().createRoom(gameExt.getParentZone(), settings, user,true,user.getLastJoinedRoom());
					new_room.setName(roomIdStr);
					RoomVariable isVote = new SFSRoomVariable("isVote", false);
					RoomVariable isSubCard = new SFSRoomVariable("isSubcard", false);
					isVote.setHidden(true);
					isSubCard.setHidden(true);
					getApi().setRoomVariables(null, new_room, Arrays.asList(isVote, isSubCard));

					logger.debug(System.currentTimeMillis()+"\t"+user.getName()+"\t"+ "creatroom" +"\t"+
							user.getIpAddress()+"\t"+ roomId +"\t"+ round +"\t" + "success"+"\t"+"success");

				} catch (SFSCreateRoomException e) {
					gameExt.trace("room create faild!!!"+ e.getMessage());
					resp.putInt("result",GlobalConstants.ROOM_CREATE_FAILED_INNER_ERROR);
					resp.putUtfString("msg","system error");
					gameExt.send(CmdsUtils.CMD_CREATROOM,resp,user);
					logger.debug(System.currentTimeMillis()+"\t"+user.getName()+"\t"+ "creatroom" +"\t"+
							user.getIpAddress()+"\t"+ roomId +"\t"+ round +"\t" + "failed"+"\t"+"system_err");
				}

				resp.putInt("result",GlobalConstants.ROOM_CREATE_SUCCESS);
				resp.putInt("room",roomId);
				resp.putUtfString("msg","create room success");
				gameExt.send(CmdsUtils.CMD_CREATROOM,resp,user);
			}else {
				roomId = RandomUtils.nextInt() % 999999;
				roomIdStr = String.format("%06d", roomId);
			}
			if(round>1000){
				flag = false;
				gameExt.trace("room create faild,no available roomId");
				resp.putInt("result",GlobalConstants.ROOM_CREATE_FAILED_INNER_ERROR);
				resp.putUtfString("msg","no available roomId");
				gameExt.send(CmdsUtils.CMD_CREATROOM,resp,user);
				logger.debug(System.currentTimeMillis()+"\t"+user.getName()+"\t"+ "creatroom" +"\t"+
						user.getIpAddress()+"\t"+ roomId +"\t"+ round +"\t" + "failed"+"\t"+"no_available_room");
			}
		}
		trace("user ["+user.getName()+"] create room ["+ roomIdStr+"] with round"+round);
	}

	private void assembleMessage(GBRoomCreateREQ message,ISFSObject param,User user,int serverId,int roomId){
		message.setIp(user.getIpAddress());
		message.setAccountID(user.getName());
		message.setServerID(serverId);
		message.setID(Integer.parseInt(user.getName()));
		message.setRoomID(roomId);
		int count = param.getInt("count");
		if(!(count == 0 || count ==1)){
			count = 0;
		}
		message.setCount(count);
		message.setType(param.getInt("type"));

	}
}
