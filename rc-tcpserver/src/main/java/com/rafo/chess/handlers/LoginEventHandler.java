package com.rafo.chess.handlers;

import com.kodgames.battle.common.constants.GlobalConstants;
import com.rafo.chess.core.ROCHExtension;
import com.rafo.chess.exception.PersistException;
import com.rafo.chess.model.LoginUser;
import com.rafo.chess.service.LoginService;
import com.rafo.chess.utils.CmdsUtils;
import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSErrorCode;
import com.smartfoxserver.v2.exceptions.SFSErrorData;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.exceptions.SFSLoginException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginEventHandler extends BaseServerEventHandler
{
	private final Logger logger = LoggerFactory.getLogger("user");
	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException
	{

		String userName = (String) event.getParameter(SFSEventParam.LOGIN_NAME);
		ROCHExtension loginExt = (ROCHExtension)getParentExtension();
		ISession session = (ISession) event.getParameter(SFSEventParam.SESSION);
        String cryptedPass = (String) event.getParameter(SFSEventParam.LOGIN_PASSWORD);

		LoginUser loginUser = null;
        try{
            loginUser = LoginService.getUserFromRedis(userName);
        }catch (PersistException e){
            processException(userName,loginExt,session);
        }

        User oldUser = loginExt.getParentZone().getUserManager().getUserByName(userName);
        if(oldUser != null && loginUser != null){
            //loginExt.getParentZone().getUserManager().disconnectUser(oldUser);
            getApi().logout(oldUser);
            SFSObject obj = new SFSObject();
            obj.putInt("result", GlobalConstants.LOGOUT_KICKOFF);
            obj.putUtfString("msg","your account login in other place");
            loginExt.send(CmdsUtils.SFS_EVENT_ACCOUNT_LOGOUT,obj,oldUser);
        }

		if(loginUser == null){
			SFSErrorData errData = new SFSErrorData(SFSErrorCode.LOGIN_BAD_USERNAME);
			errData.addParameter(userName);
            logger.debug(System.currentTimeMillis()+"\t"+userName+"\t"+ "login"+"\t"+
                    session.getAddress()+"\t"+"failed"+"\t"+"not_exists");
			throw new SFSLoginException("Bad user name: " + userName, errData);
		}else{
            if (!getApi().checkSecurePassword(session, loginUser.getToken(), cryptedPass)
                    || "".equals(loginUser.getToken()) || loginUser.getExpire()==0
                    || System.currentTimeMillis()>loginUser.getExpire()) {

                logger.debug(System.currentTimeMillis()+"\t"+userName+"\t"+  "login"+"\t"+
                        session.getAddress()+"\t"+"failed"+"\t"+"token_err");
                SFSErrorData data = new SFSErrorData(SFSErrorCode.LOGIN_BAD_PASSWORD);
                data.addParameter(userName);
                throw new SFSLoginException("Login failed for user: "  + userName, data);
            }else {
                try{
                    loginUser.setStatus(2);
                    LoginService.storeUser2redis(loginUser);
                    session.setProperty("sfsobj", loginUser.toSFSObject());
                    trace("user " + userName + " login sucess");
                    logger.debug(System.currentTimeMillis()+"\t"+userName+"\t"+  "login"+"\t"+
                            session.getAddress()+"\t"+"success"+"\t"+"login_success");

                }catch (PersistException e){
                    processException(userName,loginExt,session);
                }
            }
		}

	}

	private void processException(String userName, ROCHExtension loginExt,ISession session) throws SFSLoginException {
        SFSErrorData errData = new SFSErrorData(SFSErrorCode.LOGIN_SERVER_FULL);
        errData.addParameter(userName);
        loginExt.trace("user "+userName +" does not exists");
        logger.debug(System.currentTimeMillis()+"\t"+userName+"\t"+  "login"+"\t"+
                session.getAddress()+"\t"+"failed"+"\t"+"system_err");
        throw new SFSLoginException("System err: " + userName, errData);
    }


}
