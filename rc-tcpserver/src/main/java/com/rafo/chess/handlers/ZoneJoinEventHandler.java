package com.rafo.chess.handlers;

import com.kodgames.battle.common.constants.GlobalConstants;
import com.rafo.chess.core.ROCHExtension;
import com.rafo.chess.model.LoginUser;
import com.rafo.chess.service.LoginService;
import com.rafo.chess.utils.CmdsUtils;
import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.SFSUserVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

import java.util.Arrays;

public class ZoneJoinEventHandler extends BaseServerEventHandler
{
	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException
	{
		ROCHExtension loginExt = (ROCHExtension)getParentExtension();
		User theUser = (User) event.getParameter(SFSEventParam.USER);
		ISession session = (ISession) theUser.getSession();
		SFSObject sfsObject = (SFSObject) session.getProperty("sfsobj");

		try{

			theUser.setProperty("sfsobj",sfsObject);

			LoginUser loginUser = new LoginUser(theUser);
			theUser.setName(loginUser.getId()+"");
			theUser.setConnected(false);

			LoginService.storeUser2redis(loginUser);

			SFSObject resp = loginUser.toSFSObject();
			resp.putInt("result", GlobalConstants.LOGIN_SUCCESS);

			loginExt.send(CmdsUtils.CMD_USER_INFO_UPDATE,resp,theUser);
		}catch (Exception e){

			sfsObject.putInt("result", GlobalConstants.LOGIN_FAILED);
			loginExt.send(CmdsUtils.CMD_USER_INFO_UPDATE,sfsObject,theUser);
		}

	}
}
