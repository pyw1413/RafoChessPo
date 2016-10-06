package com.kodgames.battle.service.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kodgames.battle.common.constants.GlobalConstants;
import com.kodgames.battle.entity.account.BGAccountModifyREQ;
import com.kodgames.battle.entity.battle.*;
import com.kodgames.battle.service.server.KodgamesExtension;
import com.rafo.chess.utils.CmdsUtils;
import com.smartfoxserver.v2.extensions.SFSExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battle.common.Player;
import com.kodgames.battle.common.Player.PlayState;
import com.kodgames.battle.common.Room;
import com.kodgames.battle.service.record.PlayerPointInfo;
import com.kodgames.battle.service.record.RecordService;
import com.kodgames.battle.service.record.RoomStatistic;
import com.kodgames.battle.service.record.RoundRecord;


public class BattleService
{
	private static final long serialVersionUID = -8513687732800111630L;
	private static Logger logger = LoggerFactory.getLogger(BattleService.class);
	private BattleHelper battleHelper;
	private Room room;
	private KodgamesExtension roomExt;

	public SFSExtension getRoomExt() {
		return roomExt;
	}

	public void setRoomExt(KodgamesExtension roomExt) {
		this.roomExt = roomExt;
	}

	public BattleService(){
	}

	public void init(Room room){
		this.room = room;
		this.battleHelper = new BattleHelper(roomExt,room);
	}
	
	public List<MBattleStep> sendPlayerStatus(String accountId, int roomId, boolean offline)
	{
		if(room == null || roomId != room.getRoomID())
		{
			logger.error("BattleService room {} not found.", roomId);
			return null;
		}
		Player player = room.getPlayer(accountId);
		if(player == null)
		{
			logger.error("BattleService player {} not found. roomID={}", accountId, roomId);
			return null;
		}

		player.setOffline(offline);
		if(!offline)
			player.setPlayState(PlayState.Ready);
		if(battleHelper.isBattleStart())
			player.setPlayState(PlayState.Battle);
		
		sendBattleStatus(accountId, roomId);
		
		if(!offline) {
			return battleHelper.setPlayerBattleStatus(accountId);
		}

		return null;
	}
	
	public void sendBattleStatus(String accountId, int roomId)
	{
		if(room == null || roomId != room.getRoomID())
		{
			logger.error("BattleService room {} not found.", roomId);
			return;
		}
		
		// 当玩家全部准备，设置为战斗状态
		{
			String unReadyPlayer = null;
			for(Map.Entry<String, Player> entry : room.getPlayers().entrySet())
			{
				if(entry.getValue().getPlayState() == PlayState.Idle || 
				   entry.getValue().isOffline())
				{
					unReadyPlayer = entry.getKey();
					break;
				}
			}
			
			if((unReadyPlayer == null && room.isFullNumber()))
			{
				for(Map.Entry<String, Player> entry : room.getPlayers().entrySet())
				{
					entry.getValue().setPlayState(PlayState.Battle);
				}
			}
		}

		//
		String xx = "";
		for(Player player : room.getPlayers().values()){
			xx+= player.getAccountID()+","+player.getPlayState() + ";";
		}
		logger.debug("==============sendBattleStatus================== " + xx);
		BWBattleStartRES res = this.getBattleStatus(accountId);
		this.roomExt.send(CmdsUtils.CMD_BATTLE_READY, res.toSFSObject(),this.roomExt.getParentRoom().getUserList());

	}

	public BWBattleStartRES getBattleStatus(String accountId){

		// 发送玩家状态
		BWBattleStartRES res = new BWBattleStartRES();
		for(Map.Entry<String, Player> entry : room.getPlayers().entrySet())
		{
			if(entry.getKey().equals(accountId))
				res.setPlayerId(entry.getValue().getID());

			BattlePlayerStatus statusBuilder = new BattlePlayerStatus();
			statusBuilder.setPlayerId(entry.getValue().getID());
			statusBuilder.setStatus(entry.getValue().getPlayState().ordinal());
			statusBuilder.setPoints(entry.getValue().getPoints());
			statusBuilder.setOffline(entry.getValue().isOffline());
			res.addPlayerStatus(statusBuilder);
		}
		res.setCurrentBattleCount(room.getBattleTime());
		res.setAccountId(accountId);

		return res;
	}
	
	public Map<String, BWBattleStepRES> battleStart(WBBattleStartREQ message)
	{
		battleHelper.getFailedAccountID().clear(); //清空失败的

		List<MBattleStep> steps = sendPlayerStatus(message.getAccountId(), message.getRoomId(), false);

		return battleStepToRES(steps);
	}
	
	public void playerOffline(String accountId, int roomId)
	{
		battleHelper.getFailedAccountID().clear(); //清空失败的
		if(room == null)
			return;
		
		Player player = room.getPlayer(accountId);
		if(player == null)
			return;

		sendPlayerStatus(accountId, roomId, true);
	}
	
	public Map<String, BWBattleStepRES> battleStep(WBBattleStepREQ message)
	{
		if(room == null || message.getRoomId() != room.getRoomID())
		{
			logger.error("BattleService room {} not found.", message.getRoomId());
			return null;
		}
		logger.debug("BattleService battleStep message {} .", message);
		if(battleHelper == null){
			BWBattleStepRES res = new BWBattleStepRES();
			res.setAccountId(message.getAccountId());
			res.setResult(GlobalConstants.BW_Battle_Step_InValid_Operator);
			logger.error("BattleService battleStep battleHelper {} not found.", message.getRoomId());

			Map<String, BWBattleStepRES> result = new HashMap<>();
			result.put(message.getAccountId(), res);
			return result;
		}else {
			battleHelper.getFailedAccountID().clear();
			List<MBattleStep> steps = battleHelper.processBattle(message.getAccountId(), message.getPlayType(), message.getCard());
			return battleStepToRES(steps);
		}
	}
	
	public void destroyRoom(int roomId)
	{
/*		if(battleHelpers.containsKey(roomId))
			battleHelpers.remove(roomId);*/
	}
	
	public BWBattleStepRES sendStepToGateway(int roomId, String accountID, BWBattleStepRES res)
	{
		if(room == null || roomId != room.getRoomID())
		{
			logger.error("BattleService room {} not found.", roomId);
			return null;
		}
		
		Player player = room.getPlayer(accountID);
		if(player == null)
		{
			logger.error("BattleService player {} not found.", accountID);
			return null;
		}
		
		logger.debug("Send to Client {}, {}", accountID, res.toString());
//		Transmitter.getInstance().write(null, GlobalConstants.DEFAULT_CALLBACK, res);
		return res;
	}
	
	public BGAccountModifyREQ modifyAccountInfo(int roomID, String accountID, int id, Boolean alterCard, int alterPoint)
	{
		if(room == null || roomID != room.getRoomID())
		{
			logger.error("BattleService room {} not found.", roomID);
			return null;
		}
		Player player = room.getPlayer(accountID);
		if(player == null)
		{
			logger.error("BattleService player {} not found.", accountID);
			return null;
		}
		if(alterCard)
		{
			room.setHasSubCard(true);
		}
		player.modifyPoints(alterPoint);
		BGAccountModifyREQ builder = new BGAccountModifyREQ();
		builder.setRoomID(roomID);
	    builder.setAccountID(accountID);
	    builder.setId(id);
	    builder.setAlterCard(alterCard);
//	    Transmitter.getInstance().write(server.getGameNode(), GlobalConstants.DEFAULT_CALLBACK, builder.build());
		return builder;
	}
	
	public void recordBattleData(int roomId, Long startTime, BattleData battleData)
	{
		if(room == null || roomId != room.getRoomID())
		{
			logger.error("BattleService recordBattleData room {} not found.", roomId);
			return;
		}
		
		// room roomStatistic.
		List<PlayerPointInfo> battleInfos = new ArrayList<PlayerPointInfo>();
		for(BattleCensus census : battleData.getBattleCensuss())
		{
			Player player = room.getPlayer(census.getPlayerId());
			if(player == null)
			{
				logger.error("BattleService recordBattleData player {} not found.", census.getPlayerId());
				return;
			}
			PlayerPointInfo info = new PlayerPointInfo(player.getID(), player.getName(), player.getChair(), census.getPoint());
			battleInfos.add(info);
		}
		List<PlayerPointInfo> roundInfos = new ArrayList<PlayerPointInfo>();
		for(BattleBalance balance : battleData.getBattleBalances())
		{
			Player player = room.getPlayer(balance.getPlayerId());
			if(player == null)
			{
				logger.error("BattleService recordBattleData player {} not found.", balance.getPlayerId());
				return;
			}
			PlayerPointInfo info = new PlayerPointInfo(player.getID(), player.getName(), player.getChair(), balance.getWinPoint());
			roundInfos.add(info);
		}
		
		RoomStatistic roomStatistic = new RoomStatistic(room.getCreateTime(), roomId, battleInfos);
		RoundRecord roundRecord = new RoundRecord(startTime, roundInfos, battleData);
		RecordService recordService = new RecordService();
		recordService.saveRoundToDB(roomId, room.getOwerID(), room.getRoomType(), room.isFinishBattle(), roomStatistic, roundRecord);
	}

	public Map<String, BWBattleStepRES> battleStepToRES(List<MBattleStep> steps){

		if(battleHelper.getFailedAccountID().size() > 0){
			//失败
			Map<String, BWBattleStepRES> results = new HashMap<>();
			for(String accountID : battleHelper.getFailedAccountID()){
				BWBattleStepRES res = new BWBattleStepRES();
				res.setResult(GlobalConstants.BW_Battle_Step_InValid_Operator);
				res.setAccountId(accountID);
				results.put(accountID, res);
			}
			return results;
		}

		Map<String, BWBattleStepRES> results = new HashMap<>();
		if(steps == null || steps.size() == 0){
			return results;
		}

		for(MBattleStep battleStep : steps) {

			boolean ignoreOther = battleStep.isIgnoreOther() ||
					(!battleStep.isSelf() &&
							(battleStep.getPlayType() == MBattleStep.PlayType.CanDotKong ||
									battleStep.getPlayType() == MBattleStep.PlayType.CanHu ||
									battleStep.getPlayType() == MBattleStep.PlayType.CanPong));

			String accountID = null;
			for (int i = 0; i < MajhongHelper.ONE_CARD_MAX; i++) {
				if (accountID == null)
					accountID = battleStep.getOwernAccId();
				else
					accountID = battleHelper.getNextPlayerAcc(accountID);

				if (ignoreOther && accountID.equals(battleStep.getOwernAccId()) == false)
					continue;

				Player player = room.getPlayer(accountID);
				if (player.getPlayState().ordinal() < Player.PlayState.Ready.ordinal())
					continue;

				BWBattleStepRES res = results.get(accountID);
				BattleData builder ;
				if(res == null){
					res = new BWBattleStepRES();
					results.put(accountID, res);
					builder = new BattleData();
					res.setBattleData(builder);
				}else{
					builder = res.getBattleData();
				}

				res.setResult(GlobalConstants.BW_Battle_Step_SUCCESS);
				res.setAccountId(accountID);

				// 设置庄主id
				builder.setBankerId(battleHelper.getPlayerID(battleHelper.getBankerId()));
				// 设置局数
				builder.setBattleTime(room.getBattleTime());
				builder.setBattleCount(room.getBattleCount());

				BattleStep stepBuilder = battleStep.toBattleStep();
				if(MBattleStep.PlayType.Draw == battleStep.getPlayType() && !accountID.equals(battleStep.getOwernAccId())){ //如果是别人摸牌，不能让他知道牌的内容
					stepBuilder.getCard().clear();
					stepBuilder.getCard().add(-1); //设置一张空的牌，防止被别人看到
				}

				stepBuilder.setRemainCardCount(battleHelper.getCardPools().size());
				if (battleStep.isTargetValid())
					stepBuilder.setTargetId(battleHelper.getPlayerID(battleStep.getTargetAccId()));

				if (battleStep.getPlayType() == MBattleStep.PlayType.Deal) {
					stepBuilder.setOwnerId(battleHelper.getPlayerID(accountID));
					String nextAccountId = accountID;
					for (int j = 0; j < MajhongHelper.ONE_CARD_MAX; j++) {
						int playerId = battleHelper.getPlayerID(nextAccountId);
						BattleDealCard dealBuild = new BattleDealCard();
						dealBuild.setPlayerId(playerId);
						if (playerId == player.getID()) {
							// 设置手上的牌
							for (int card : battleHelper.getPlayCards().get(nextAccountId))
								dealBuild.addCards(card);
						}

						// 设置打出去的牌
						for (int card : battleHelper.getDisposeCards().get(nextAccountId))
							dealBuild.addDisposeCards(card);
						builder.addBattleDealCards(dealBuild);

						// 设置玩家状态信息
						if (battleHelper.getObtains().get(nextAccountId).size() > 0) {
							BattleBalance blanceBuild = new BattleBalance();
							blanceBuild.setPlayerId(playerId);
							for (MJObtain obtain : battleHelper.getObtains().get(nextAccountId)) {
								CardBalance cardBlance = new CardBalance();
								cardBlance.setType(obtain.getType());
								cardBlance.setCard(obtain.getCard());
								blanceBuild.addBalances(cardBlance);
							}

							builder.addBattleBalances(blanceBuild);
						}

						nextAccountId = battleHelper.getNextPlayerAcc(nextAccountId);
					}

				} else
					stepBuilder.setOwnerId(battleHelper.getPlayerID(battleStep.getOwernAccId()));

				builder.addBattleSteps(stepBuilder);
			}
		}
		return results;
	}

	public BattleHelper getBattleHelper(){
		return battleHelper;
	}
}
