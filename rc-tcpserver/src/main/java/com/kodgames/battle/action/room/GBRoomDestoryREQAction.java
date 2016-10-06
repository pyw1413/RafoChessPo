package com.kodgames.battle.action.room;

import com.kodgames.battle.common.constants.GlobalConstants;
import com.kodgames.battle.entity.room.BGVoteDestroyRES;
import com.kodgames.battle.entity.room.GBVoteDestroyREQ;
import com.kodgames.battle.service.room.RoomHelper;
import com.kodgames.battle.service.room.RoomService;
import com.kodgames.battle.service.server.KodgamesExtension;
import com.rafo.chess.model.LoginUser;
import com.rafo.chess.service.LoginService;
import com.rafo.chess.utils.CmdsUtils;
import com.smartfoxserver.v2.annotations.MultiHandler;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.RoomVariable;
import com.smartfoxserver.v2.entities.variables.SFSRoomVariable;
import com.smartfoxserver.v2.entities.variables.SFSUserVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.exceptions.SFSVariableException;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.extensions.ExtensionLevel;
import com.smartfoxserver.v2.extensions.SFSExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

@MultiHandler
public class GBRoomDestoryREQAction extends BaseClientRequestHandler
{
	final static Logger logger = LoggerFactory.getLogger("room");

	@Override
	public void handleClientRequest(User user, ISFSObject isfsObject) {
		KodgamesExtension gameExt = (KodgamesExtension) getParentExtension();
		RoomService roomService = gameExt.getRoomService();
		String command = isfsObject.getUtfString(SFSExtension.MULTIHANDLER_REQUEST_ID);

		Room room = gameExt.getParentRoom();
		GBVoteDestroyREQ destoryREQ = new GBVoteDestroyREQ();
		destoryREQ.setAccountID(user.getName());
		destoryREQ.setRoomID(Integer.parseInt(room.getName()));

		ISFSObject resp = new SFSObject();

		if(command.equals(CmdsUtils.CMD_ROOM_DESTROY)){
			destoryREQ.setVoteResult(2);
		}else {
			int voteResult = isfsObject.getInt("voteResult");
			destoryREQ.setVoteResult(voteResult);
		}

		if(room == null){
			resp.putInt("result", GlobalConstants.ROOM_DESTORY_FAILED_ROOM_NUM_ERROR);
			gameExt.send(CmdsUtils.CMD_ROOM_DESTROY_VOTE_RESP,resp,user);
			toLog(logger,user,room.getName(),"destroyroom","failed","room_num_err");
			return;
		}else if(!isContainUser(room,user)){
			resp.putInt("result", GlobalConstants.ROOM_DESTORY_FAILED_NOT_IN_ROOM);
			gameExt.send(CmdsUtils.CMD_ROOM_DESTROY_VOTE_RESP,resp,user);
			toLog(logger,user,room.getName(),"destroyroom","failed","user_not_in_room");
			return;
		}else if(!room.getOwner().getName().equals(user.getName()) && CmdsUtils.CMD_ROOM_DESTROY.equals(command)){
			resp.putInt("result", GlobalConstants.ROOM_DESTORY_FAILED_NOT_OWNER);
			gameExt.send(CmdsUtils.CMD_ROOM_DESTROY_VOTE_RESP,resp,user);
			toLog(logger,user,room.getName(),"destroyroom","failed","user_not_room_owner");
			return;
		}else if(CmdsUtils.CMD_ROOM_DESTROY.equals(command)) {
			if(room.getUserList().size()==1 && room.getOwner().getName().equals(user.getName()) && destoryREQ.getVoteResult()==2){
				try {
					RoomHelper.destroyRoom(room);
					resp.putInt("result", GlobalConstants.ROOM_DESTORY_SUCCESS);
					gameExt.send(CmdsUtils.CMD_ROOM_DESTROY_VOTE_RESP,resp,room.getUserList());
					toLog(logger,user,room.getName(),"destroyroom","success","success");
				} catch (Exception e) {
					resp.putInt("result", GlobalConstants.SYSTEM_ERROR);
					gameExt.send(CmdsUtils.CMD_ROOM_DESTROY_VOTE_RESP,resp,user);
					toLog(logger,user,room.getName(),"destroyroom","failed","system_err");
				}
			}
			RoomVariable nowVote = room.getVariable("isVote");
			if(nowVote.getBoolValue()){
				BGVoteDestroyRES builder = new BGVoteDestroyRES();
				builder.setRoomID(Integer.parseInt(room.getName()));
				builder.setAccountID(user.getName());
				builder.setResult(GlobalConstants.WC_VOTE_DESTROY_FAILED_HAS_VOTED);
				gameExt.send(CmdsUtils.CMD_ROOM_DESTROY_VOTE_RESP,builder.toSFSObject(),user);
			}else {
				RoomVariable isVote = new SFSRoomVariable("isVote", true);
				isVote.setHidden(true);
				RoomVariable voteTime = new SFSRoomVariable("voteTime", (new Long(System.currentTimeMillis()).intValue())/1000);
				voteTime.setHidden(true);
				getApi().setRoomVariables(null, gameExt.getParentRoom(), Arrays.asList(isVote,voteTime));

				UserVariable voteResult = new SFSUserVariable("voteResult", room.getName()+":"+destoryREQ.getVoteResult());
				voteResult.setHidden(true);
				getApi().setUserVariables(user, Arrays.asList(voteResult));

				List<BGVoteDestroyRES> datas = roomService.voteDestoryRoom(destoryREQ);
				for(BGVoteDestroyRES one:datas){
					gameExt.send(CmdsUtils.CMD_ROOM_DESTROY_VOTE_RESP,one.toSFSObject(),getApi().getUserByName(one.getAccountID()));
				}
			}
		}else if(CmdsUtils.CMD_ROOM_DESTROY_VOTE_REQ.equals(command)){
			UserVariable voteResult = new SFSUserVariable("voteResult", room.getName()+":"+destoryREQ.getVoteResult());
			voteResult.setHidden(true);
			getApi().setUserVariables(user, Arrays.asList(voteResult));
			if(RoomHelper.couldDestroy(room)){
				try{
					RoomHelper.destroy(room,gameExt,resp);
					toLog(logger,user,room.getName(),"destroyroom","success","success");
				}catch (Exception e){
					resp.putInt("result", GlobalConstants.SYSTEM_ERROR);
					gameExt.send(CmdsUtils.CMD_ROOM_DESTROY_VOTE_RESP,resp,user);
					toLog(logger,user,room.getName(),"destroyroom","failed","system_err");
				}

			}else {
				List<BGVoteDestroyRES> datas = roomService.voteDestoryRoom(destoryREQ);
				for(BGVoteDestroyRES one:datas){
					gameExt.send(CmdsUtils.CMD_ROOM_DESTROY_VOTE_RESP,one.toSFSObject(),getApi().getUserByName(one.getAccountID()));
				}
			}
		}

	}

	private boolean isContainUser(Room room,User user){
		boolean result = false;
		for(User u:room.getUserList()){
			if(u.getName()==user.getName()){
				return true;
			}
		}
		return result;
	}

	private void  toLog(Logger logger,User user,String roomId,String cmd,String isErr,String msg){
		logger.debug(System.currentTimeMillis()+"\t"+user.getName()+"\t"+ cmd +"\t"+
				user.getIpAddress()+"\t"+ roomId +"\t"+ 0 +"\t" + isErr+"\t"+msg);
	}



}
