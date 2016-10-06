package com.rafo.chess.core;

import com.kodgames.battle.action.room.GBRoomCreateREQAction;
import com.kodgames.battle.service.room.VoteDestroyTask;
import com.rafo.chess.filter.SFSJoinRoomFilter;
import com.rafo.chess.handlers.ChatMessageHandler;
import com.rafo.chess.handlers.LoginEventHandler;
import com.rafo.chess.handlers.OnUserGoneHandler;
import com.rafo.chess.handlers.ZoneJoinEventHandler;
import com.rafo.chess.manager.MySQLManager;
import com.rafo.chess.manager.RedisManager;
import com.rafo.chess.utils.CmdsUtils;
import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.controllers.SystemRequest;
import com.smartfoxserver.v2.controllers.filter.ISystemFilterChain;
import com.smartfoxserver.v2.controllers.filter.SysControllerFilterChain;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.SFSExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;



/**
 * Created by 亚文 on 2016/9/6.
 */

public class ROCHExtension extends SFSExtension
{
	private final Logger logger = LoggerFactory.getLogger("Extensions");
	ScheduledFuture<?> taskHandle;
    private static final int SCHEDULE_TIME = 60;

	@Override
	public void init()
	{
		logger.info("Rafo game Extension  started =================" );
		RedisManager.getInstance().init(this);
		MySQLManager.getInstance().init(this);

		addRequestHandler(CmdsUtils.CMD_MSG, ChatMessageHandler.class);
		addRequestHandler(CmdsUtils.CMD_CREATROOM, GBRoomCreateREQAction.class);

		addEventHandler(SFSEventType.USER_LOGIN, LoginEventHandler.class);
		addEventHandler(SFSEventType.USER_JOIN_ZONE, ZoneJoinEventHandler.class);
		addEventHandler(SFSEventType.USER_DISCONNECT, OnUserGoneHandler.class);
		addEventHandler(SFSEventType.USER_LOGOUT, OnUserGoneHandler.class);

        getParentZone().resetSystemFilterChain();
        ISystemFilterChain filterChain = new SysControllerFilterChain();
        filterChain.addFilter("roomfilter", new SFSJoinRoomFilter());
        getParentZone().setFilterChain(SystemRequest.JoinRoom, filterChain);


		SmartFoxServer sfs = SmartFoxServer.getInstance();
        VoteDestroyTask voteDestroy = new VoteDestroyTask(this);
		taskHandle = sfs.getTaskScheduler().scheduleAtFixedRate(voteDestroy, 0, SCHEDULE_TIME, TimeUnit.SECONDS);


	}


	@Override
	public void destroy()
	{
		super.destroy();
		logger.info("Rafo game destroyed!======================");
	}



	@Override
	protected void addEventHandler(SFSEventType eventType, Class<?> theClass) {
		super.addEventHandler(eventType, theClass);
	}

	@Override
	public void addRequestHandler(String requestId, Class<?> theClass) {
		super.addRequestHandler(requestId, theClass);
	}






}
