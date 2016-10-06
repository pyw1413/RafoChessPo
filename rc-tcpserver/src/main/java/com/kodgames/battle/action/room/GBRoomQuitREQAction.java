package com.kodgames.battle.action.room;

import com.kodgames.battle.common.constants.GlobalConstants;
import com.kodgames.battle.entity.room.BGRoomQuitRES;
import com.kodgames.battle.entity.room.GBRoomQuitREQ;
import com.kodgames.battle.service.server.KodgamesExtension;
import com.rafo.chess.manager.RedisManager;
import com.rafo.chess.model.LoginUser;
import com.rafo.chess.service.LoginService;
import com.rafo.chess.utils.CmdsUtils;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GBRoomQuitREQAction extends BaseClientRequestHandler
{
	final static Logger logger = LoggerFactory.getLogger("room");

	@Override
	public void handleClientRequest(User user, ISFSObject isfsObject) {
		KodgamesExtension gameExt = (KodgamesExtension) getParentExtension();
		GBRoomQuitREQ req = new GBRoomQuitREQ();
		req.setRoomID(Integer.parseInt(gameExt.getParentRoom().getName()));
		req.setAccountID(user.getName());

		try{
			List<BGRoomQuitRES> data = gameExt.getRoomService().roomQuit(req);

			if(data.get(0).getResult()== GlobalConstants.ROOM_QUIT_SUCCESS){
				LoginUser loginUser = new LoginUser(user);
				loginUser.setRoom(0);
				LoginService.storeUser2redis(loginUser);
				getApi().leaveRoom(user,gameExt.getParentRoom());
				user.setProperty("sfsobj",loginUser.toSFSObject());
				for(BGRoomQuitRES one:data){
					User nowUser = getApi().getUserByName(one.getAccountID());
					if(nowUser != null){
						gameExt.send(CmdsUtils.CMD_ROOM_QUIT,one.toSFSObject(),nowUser);
					}

				}

				logger.debug(System.currentTimeMillis()+"\t"+user.getName()+"\t"+ "roomquit" +"\t"+
						user.getIpAddress()+"\t"+ gameExt.getParentRoom().getName() +"\t"+ 0 +"\t" + "success"+"\t"+"success");

			}else{
				for(BGRoomQuitRES one:data){
					gameExt.send(CmdsUtils.CMD_ROOM_QUIT,one.toSFSObject(),getApi().getUserByName(one.getAccountID()));
				}

				logger.debug(System.currentTimeMillis()+"\t"+user.getName()+"\t"+ "roomquit" +"\t"+
						user.getIpAddress()+"\t"+ gameExt.getParentRoom().getName() +"\t"+ 0 +"\t" + "failed"+"\t"+"other");
			}
		}catch (Exception e){
			logger.debug(System.currentTimeMillis()+"\t"+user.getName()+"\t"+ "roomquit" +"\t"+
					user.getIpAddress()+"\t"+ gameExt.getParentRoom().getName() +"\t"+ 0 +"\t" + "failed"+"\t"+"system_err");
		}


	}
}