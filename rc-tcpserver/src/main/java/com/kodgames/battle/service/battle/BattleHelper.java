package com.kodgames.battle.service.battle;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.kodgames.battle.common.constants.GlobalConstants;
import com.kodgames.battle.entity.battle.*;
import com.kodgames.battle.service.server.KodgamesExtension;
import com.rafo.chess.utils.CmdsUtils;
import com.smartfoxserver.v2.extensions.SFSExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kodgames.battle.common.Player;
import com.kodgames.battle.common.Room;
import com.kodgames.battle.service.battle.MBattleStep.PlayType;
import com.kodgames.battle.service.battle.MajhongHelper.CardType;
import com.kodgames.battle.service.battle.MajhongHelper.HuType;
import com.kodgames.battle.service.room.RoomService;


public class BattleHelper
{
	private static Logger logger = LoggerFactory.getLogger(BattleHelper.class);
	private Room roomInfo;
	private ArrayList<Integer> cardPools = new ArrayList<Integer>();
	private ConcurrentHashMap<String, ConcurrentHashMap<Integer,Integer>> playerDealCards = new ConcurrentHashMap<String, ConcurrentHashMap<Integer,Integer>>();
	private ConcurrentHashMap<String, ArrayList<Integer>> playCards = new ConcurrentHashMap<String, ArrayList<Integer>>();
	private ConcurrentHashMap<String, ArrayList<Integer>> disposeCards = new ConcurrentHashMap<String, ArrayList<Integer>>();
	private ConcurrentHashMap<String, ArrayList<MJObtain>> obtains = new ConcurrentHashMap<String, ArrayList<MJObtain>>();
	private ConcurrentHashMap<String, MBattleCensus> battleCensus = new ConcurrentHashMap<String, MBattleCensus>();
	private ArrayList<MBattleStep> battleSteps = new ArrayList<MBattleStep>();
	private ArrayList<String> playerAcIds = new ArrayList<String>();

	public ArrayList<Integer> getCardPools() {
		return cardPools;
	}

	public ConcurrentHashMap<String, ConcurrentHashMap<Integer, Integer>> getPlayerDealCards() {
		return playerDealCards;
	}

	public ConcurrentHashMap<String, ArrayList<Integer>> getPlayCards() {
		return playCards;
	}

	public ConcurrentHashMap<String, ArrayList<Integer>> getDisposeCards() {
		return disposeCards;
	}

	public ConcurrentHashMap<String, ArrayList<MJObtain>> getObtains() {
		return obtains;
	}

	public ConcurrentHashMap<String, MBattleCensus> getBattleCensus() {
		return battleCensus;
	}

	public ArrayList<String> getPlayerAcIds() {
		return playerAcIds;
	}

	public ArrayList<String> getPassHuAcIds() {
		return passHuAcIds;
	}

	private ArrayList<String> passHuAcIds = new ArrayList<String>();
	private MajhongHelper majhongHelper = new MajhongHelper();

	public String getBankerId() {
		return bankerId;
	}

	private String bankerId = "";
	private long startTime;
	private Set<String> failedAccountID = new HashSet<>();
	private KodgamesExtension gameExt;

	public BattleHelper(KodgamesExtension gameExt, Room roomInfo)
	{
		this.gameExt = gameExt;
		this.roomInfo = roomInfo;
	}

	public List<MBattleStep> setPlayerBattleStatus(String accountId)
	{
		Player player = roomInfo.getPlayer(accountId);
		if(player == null)
		{
			logger.error("BattleService battleStart Player is null");
			return null;
		}

		for(int i = playerAcIds.size() - 1 ; i >= 0 ; i--)
		{
			String playerID = playerAcIds.get(i);
			if(roomInfo.getPlayer(playerID) == null)
			{
				playerAcIds.remove(playerID);
				battleCensus.remove(playerID);
			}
		}

		if(playerAcIds.contains(accountId) == false)
			playerAcIds.add(accountId);
		Collections.sort(playerAcIds, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return roomInfo.getPlayer(o1).getChair() - roomInfo.getPlayer(o2).getChair();
			}
		});
		if(battleCensus.containsKey(accountId) == false)
			battleCensus.put(accountId, new MBattleCensus(player.getID()));

		return startBattle(accountId);
	}

	public boolean isPlayerReady()
	{
		if(roomInfo == null || !roomInfo.isFullNumber())
			return false;

		return checkPlayerState() == null;
	}

	public boolean isBattleStart()
	{
		return battleSteps.size() > 0;
	}

	private String checkPlayerState()
	{
		for(Map.Entry<String, Player> entry : roomInfo.getPlayers().entrySet())
		{
			if(entry.getValue().isOffline() || entry.getValue().getPlayState() != Player.PlayState.Battle)
				return entry.getKey();
		}
		return null;
	}

	private List<MBattleStep> startBattle(String accountId)
	{
		// 第一局庄家随机产生，伺候庄家由generateBanker()生成
		if(bankerId.equals(""))
			bankerId = playerAcIds.get(new Random().nextInt(playerAcIds.size()));

		List<MBattleStep> steps = new ArrayList<>();
		// 处理断线情况
		if(battleSteps.size() > 0)
		{

			MBattleStep lastBattleStep = null;
			for(int i = battleSteps.size() - 1; i >= 0; i--)
			{
				if(battleSteps.get(i).getPlayType() != PlayType.DutyChicken &&
						battleSteps.get(i).getPlayType() != PlayType.ChargeChicken)
				{
					lastBattleStep = battleSteps.get(i);
					break;
				}
			}
			if(lastBattleStep != null)
			{
				MBattleStep dealStep = new MBattleStep();
				dealStep.setOwernAccId(accountId);
				dealStep.setTargetAccId(accountId);
				dealStep.setPlayType(PlayType.Deal);
				sendBattleStepSuccess(dealStep, true, steps);

				dealStep = lastBattleStep.copyOf();

				if(lastBattleStep.getOwernAccId().equals(accountId))
				{
					if((dealStep.getPlayType() & PlayType.Draw) == PlayType.Draw)
						dealStep.setCard(0, 0);
					steps.add(dealStep);

//					sendBattleStep2Target(accountId, dealStep);
				}
				else if((dealStep.getPlayType() & PlayType.Draw) == PlayType.Draw){
					steps.add(dealStep);
					//					sendBattleStep2Target(accountId, dealStep);
				}


			}
		}
		else if(isPlayerReady()){
			processBattleStep(steps);
		}
		return steps;

	}

	// 处理客户端发送的请求，打牌，碰，杠，胡
	public List<MBattleStep> processBattle(String accountId, int stepTypeValue, int card)
	{
		if(!playerAcIds.contains(accountId))
			return null;
		List<MBattleStep> steps = new ArrayList<>();
		MBattleStep lastBattleStep = battleSteps.get(battleSteps.size() - 1);
		// 检测操作是否合法
		if(!checkStepType(accountId, stepTypeValue, card))
		{
			logger.error("Client Step : Error accId {} {}", accountId, lastBattleStep.getOwernAccId());
			sendBattleStepFailed(accountId);
			return null;
		}

		MBattleStep battleStep = new MBattleStep();
		battleStep.setOwernAccId(accountId);
		battleStep.setTargetAccId(lastBattleStep.getTargetAccId());
		battleStep.setPlayType(stepTypeValue);
		battleStep.setWinType(lastBattleStep.getWinType());
		battleStep.setCard(card);
		battleStep.setGangDiscard(lastBattleStep.isGangDiscard());
		battleStep.setGangDraw(lastBattleStep.isGangDraw());
		battleStep.setGangAccId(lastBattleStep.getGangAccId());
		battleStep.setHuLists(lastBattleStep.getHuLists());
		battleSteps.add(battleStep);
		processBattleStep(steps);
		return steps;
	}

	// 检测打牌操作是否合法
	private boolean checkStepType(String accountId, int stepType, int card)
	{
		int lastIdex = battleSteps.size() - 1;
		MBattleStep lastBattleStep = null;
		if(battleSteps.get(lastIdex).getPlayType() == PlayType.ChargeChicken ||
				battleSteps.get(lastIdex).getPlayType() == PlayType.DutyChicken)
			lastBattleStep = battleSteps.get(lastIdex - 1);
		else
			lastBattleStep = battleSteps.get(lastIdex);

		if(lastBattleStep.getOwernAccId().equals(accountId) == false)
		{
			logger.error("Invalid, it's not your turn. Client Step : Error accId {} {}", accountId, lastBattleStep.getOwernAccId());
			return false;
		}

		int lastPlayType = lastBattleStep.getPlayType();	
/*
 * 	cards[0] 摸牌
	cards[1-3] 明杠
	cards[4-6] 暗杠
	cards[7+] 听牌
 */
		switch (stepType) {
			case PlayType.Pong:
				return  (lastPlayType & PlayType.CanPong) == PlayType.CanPong &&
						lastBattleStep.getCard(0) == card && card > 0;
			case PlayType.Kong:
				if((lastPlayType & PlayType.CanKong) != PlayType.CanKong || card <= 0)
					return false;

				if(lastBattleStep.cardSize() < 4)
					return false;

				for(int i = 1; i < 4; i++)
					if(lastBattleStep.getCard(i) == card)
						return true;

				return false;
			case PlayType.CealedKong:
				if((lastPlayType & PlayType.CanCealedKong) != PlayType.CanCealedKong || card <= 0)
					return false;

				if(lastBattleStep.cardSize() < 7)
					return false;

				for(int i = 4; i < 7; i++)
					if(lastBattleStep.getCard(i) == card)
						return true;

				return false;
			case PlayType.DotKong:
				return (lastPlayType & PlayType.CanDotKong) == PlayType.CanDotKong &&
						lastBattleStep.getCard(0) == card && card > 0;
			case PlayType.Hu:
				return (lastPlayType & PlayType.CanHu) == PlayType.CanHu;
			case PlayType.ReadyHand:
				if((lastPlayType & PlayType.CanReadyHand) != PlayType.CanReadyHand || card <= 0)
					return false;

				if(lastBattleStep.cardSize() < 8)
					return false;

				for(int i = 7; i < lastBattleStep.cardSize(); i++)
					if(lastBattleStep.getCard(i) == card)
						return true;

				return false;
			case PlayType.Discard:
				// 出牌非法检测
				// 手牌中是否含有这张牌
				if(!playCards.get(accountId).contains(card))
					return false;

				int cardCount = 0;
				for(int ownerCard : playCards.get(accountId))
				{
					if(ownerCard == card)
						cardCount++;
				}

				if(cardCount <= 0)
					return false;

				for(MJObtain obtain : obtains.get(accountId))
				{
					if(obtain.getCard() != card)
						continue;

					if(obtain.getType() == PlayType.Pong)
					{
						cardCount -= 3;
						break;
					}
					else if(obtain.getType() == PlayType.CealedKong ||
							obtain.getType() == PlayType.DotKong ||
							obtain.getType() == PlayType.Kong)
					{
						cardCount -= 4;
						break;
					}
				}

				return cardCount > 0;
			case PlayType.Pass:
				if(lastBattleStep.getOwernAccId().equals(lastBattleStep.getTargetAccId()))
					return false;

				// 如果玩家可以胡其他玩家的出牌，但是没有胡，那么标记过胡
				if((lastPlayType & PlayType.CanHu) == PlayType.CanHu && !lastBattleStep.isSelf())
					passHuAcIds.add(accountId);

				return (lastPlayType & PlayType.CanPong) == PlayType.CanPong ||
						(lastPlayType & PlayType.CanKong) == PlayType.CanKong ||
						(lastPlayType & PlayType.CanDotKong) == PlayType.CanDotKong ||
						(lastPlayType & PlayType.CanCealedKong) == PlayType.CanCealedKong ||
						(lastPlayType & PlayType.CanHu) == PlayType.CanHu;
			default:
				return false;
		}
	}

	private void processBattleStep(List<MBattleStep> steps)
	{
		processBattleStep(-1, null, false, steps);
	}

	private void processBattleStep(int stepIndex, MBattleStep nextBattleStep, boolean fromPass, List<MBattleStep> steps)
	{
		if(battleSteps.size() <= 0)
		{
			// 开始游戏时，将当前战斗局数设置为1
			if(roomInfo.getBattleTime() == 0)
				roomInfo.addBattleTime();

			// 初始化牌池，
			randomSuffle();
			// 发牌
			dealPlayerCard();
			// 发送洗牌发牌操作
			sendBattleStepSuccess(steps);
		}

		stepIndex = (stepIndex < 0 || stepIndex >= battleSteps.size()) ? battleSteps.size() - 1 : stepIndex;
		MBattleStep battleStep = battleSteps.get(stepIndex);
		if(nextBattleStep == null)
		{
			nextBattleStep = new MBattleStep();
			nextBattleStep.setGangDraw(battleStep.isGangDraw());
			nextBattleStep.setGangDiscard(battleStep.isGangDiscard());

			if(battleStep.isGangDraw() && !battleStep.checkGangAccId())
			{
				battleStep.setGangDraw(false);
				nextBattleStep.setGangDraw(false);
			}

			if(battleStep.isGangDiscard() && battleStep.getPlayType() != PlayType.Hu)
			{
				battleStep.setGangDiscard(false);
				battleStep.setGangDraw(false);
				nextBattleStep.setGangDiscard(false);
				nextBattleStep.setGangDraw(false);
			}
		}

		switch (battleStep.getPlayType()) {
			case PlayType.Deal:
				battleStepDrawCard(battleStep, nextBattleStep, steps);
				break;
			case PlayType.ReadyHand:
				battleStepReadyhand(battleStep, nextBattleStep, fromPass, steps);
				break;
			case PlayType.Discard:
				battleStepDiscard(battleStep, nextBattleStep, fromPass, steps);
				break;
			case PlayType.Pong:
				battleStepPong(battleStep, nextBattleStep, steps);
				break;
			case PlayType.Kong:
				battleStepKong(PlayType.Kong, battleStep, nextBattleStep, fromPass, steps);
				break;
			case PlayType.CealedKong:
				battleStepKong(PlayType.CealedKong, battleStep, nextBattleStep, fromPass, steps);
				break;
			case PlayType.DotKong:
				battleStepKong(PlayType.DotKong, battleStep, nextBattleStep, fromPass, steps);
				break;
			case PlayType.Hu:
				calculateBattleResult(steps);
				return;
			case PlayType.Pass:
				battleStepPass(battleStep, nextBattleStep, steps);
				return;
		}

		if(nextBattleStep.getPlayType() == PlayType.Idle)
		{
			sendBattleStepFailed(battleStep.getOwernAccId());
			battleSteps.remove(battleSteps.size() - 1);
			return;
		}
		else
		{
			battleSteps.add(nextBattleStep);
			if(nextBattleStep.getPlayType() == PlayType.He ||
					nextBattleStep.getPlayType() == PlayType.Hu)
				calculateBattleResult(steps);
			else
				sendBattleStepSuccess(steps);
		}
	}

	private void battleStepDiscard(MBattleStep battleStep, MBattleStep nextBattleStep, boolean fromPass, List<MBattleStep> steps)
	{
		if(fromPass == false)
		{
			int discard = battleStep.getCard(0);
			// Modify Card.
			removeCard(playCards.get(battleStep.getOwernAccId()), discard);
			disposeCards.get(battleStep.getOwernAccId()).add(discard);

			// 到玩家打牌时，删除过胡标记
			deletePassHu(battleStep.getOwernAccId());

			// 通知其他玩家
			sendBattleStepSuccess(steps);

			if(nextBattleStep.isGangDraw())
			{
				nextBattleStep.setGangDiscard(true);
				nextBattleStep.setGangDraw(false);
			}
		}

		// 检测出牌对其他玩家的影响
		battleStepCheckCard(battleStep, nextBattleStep);

		if(nextBattleStep.getPlayType() == PlayType.Idle)
			battleStepDrawCard(battleStep, nextBattleStep, steps);
	}

	private void battleStepDrawCard(MBattleStep battleStep, MBattleStep nextBattleStep, List<MBattleStep> steps)
	{
		if(cardPools.size() > 0)
		{
			// 检测冲锋鸡，责任鸡
			checkChiken(battleStep, steps);

			int newCard = cardPools.get(0);
			nextBattleStep.setCard(0, newCard);
			// 判定下一次摸牌玩家
			switch (battleStep.getPlayType()) {
				case PlayType.Deal:
					newCard = 0;
					nextBattleStep.setOwernAccId(battleStep.getOwernAccId());
					nextBattleStep.setCard(0, newCard); //庄家此时有14张
					break;
				case PlayType.Discard:
				case PlayType.ReadyHand:
					String accountID = getNextPlayerAcc(battleStep.getOwernAccId());
					nextBattleStep.setOwernAccId(accountID);
					nextBattleStep.setTargetAccId(accountID);
					break;
				case PlayType.Kong:
				case PlayType.CealedKong:
				case PlayType.DotKong:
					nextBattleStep.setOwernAccId(battleStep.getOwernAccId());
					nextBattleStep.setTargetAccId(battleStep.getOwernAccId());
					nextBattleStep.setGangDraw(true);
					nextBattleStep.setGangAccId(battleStep.getOwernAccId());
					break;
			}

			// 到玩家摸牌时，删除过胡标记
			deletePassHu(nextBattleStep.getOwernAccId());

			// 判定摸牌玩家可以操作的类型，是否可以胡，是否可以听，是否可以杠，还是直接出牌(Draw)
			nextBattleStep.clearIgnoreLists();
			battleStepCheckCard(nextBattleStep, nextBattleStep);
			nextBattleStep.setPlayType(PlayType.Draw);

			if(newCard > 0)
			{
				removeCard(cardPools, newCard);
				playCards.get(nextBattleStep.getOwernAccId()).add(newCard);
			}
		}
		else
		{
			nextBattleStep.setOwernAccId(bankerId);
			nextBattleStep.setPlayType(PlayType.He);
		}
	}

	private void battleStepReadyhand(MBattleStep battleStep, MBattleStep nextBattleStep, boolean fromPass, List<MBattleStep> steps)
	{
		if(fromPass == false)
		{
			// 设置该玩家的听牌状态
			setObtainData(battleStep.getOwernAccId(), PlayType.ReadyHand, 0);
		}

		// 处理出牌操作
		battleStepDiscard(battleStep, nextBattleStep, fromPass, steps);
	}

	private void battleStepPong(MBattleStep battleStep, MBattleStep nextBattleStep, List<MBattleStep> steps)
	{
		String accountID = battleStep.getOwernAccId();
		int discard = battleStep.getCard(0);

		if(checkPong(accountID, discard) != discard)
		{
			nextBattleStep.setPlayType(PlayType.Idle);
		}
		else
		{
			sendBattleStepSuccess(steps);
			// 存储碰
			setObtainData(accountID, PlayType.Pong, discard);
			playCards.get(accountID).add(discard);
			removeCard(disposeCards.get(battleStep.getTargetAccId()), discard);
			if(disposeCards.get(battleStep.getTargetAccId()).size() <= 0)
				disposeCards.get(battleStep.getTargetAccId()).add(0);
			// 检测冲锋鸡，责任鸡
			checkChiken(battleStep, steps);
			nextBattleStep.setOwernAccId(battleStep.getOwernAccId());
			nextBattleStep.setTargetAccId(battleStep.getOwernAccId());
			nextBattleStep.setPlayType(PlayType.Draw);
		}
	}

	private void battleStepKong(int kongType, MBattleStep battleStep, MBattleStep nextBattleStep, boolean fromPass, List<MBattleStep> steps)
	{
		String accountID = battleStep.getOwernAccId();
		int discard = battleStep.getCard(0);
		if(fromPass == false)
		{
			if(enableKong(battleStep.getOwernAccId(), kongType, discard))
			{
				// 存储杠
				if(kongType == PlayType.CealedKong)
				{
					//暗杠过杠不算番
					int battleSize = battleSteps.size();
					if(battleSize < 2)
						setObtainData(accountID, kongType, discard);
					else
					{
						MBattleStep lastBattleStep = battleSteps.get(battleSize - 2);
						boolean valid = lastBattleStep.getCard(0) == discard || (disposeCards.get(accountID).size() <= 0 && obtains.get(accountID).size() <= 0);
						setObtainData(accountID, kongType, discard, null, valid);
					}
				}
				else
				{
					MJObtain obtain = removeObtainData(accountID, PlayType.Pong, discard);
					setObtainData(accountID, kongType, discard, kongType == PlayType.DotKong ? battleStep.getTargetAccId() : null, obtain == null ? true : obtain.isValid());
				}

				if(kongType == PlayType.DotKong)
				{
					playCards.get(accountID).add(discard);
					removeCard(disposeCards.get(battleStep.getTargetAccId()), discard);
					if(disposeCards.get(battleStep.getTargetAccId()).size() <= 0)
						disposeCards.get(battleStep.getTargetAccId()).add(0);
				}

				// 通知其他玩家发生杠牌操作。
				sendBattleStepSuccess(steps);
				// 检测冲锋鸡，责任鸡
				checkChiken(battleStep, steps);
			}
			else
				nextBattleStep.setPlayType(PlayType.Idle);
		}

		if(kongType == PlayType.Kong)
			battleStepCheckCard(battleStep, nextBattleStep);
		if(nextBattleStep.getPlayType() == PlayType.Idle)
			battleStepDrawCard(battleStep, nextBattleStep, steps);
	}

	private void battleStepPass(MBattleStep battleStep, MBattleStep nextBattleStep, List<MBattleStep> steps)
	{
		String targetAccID = battleStep.getTargetAccId();
		nextBattleStep.clearIgnoreLists();
		sendBattleStepSuccess(battleStep, true, steps);
		for(int i = battleSteps.size() - 2; i >= 0 ; i--)
		{
			if(battleSteps.get(i).getPlayType() == PlayType.ChargeChicken ||
					battleSteps.get(i).getPlayType() == PlayType.DutyChicken)
				continue;
			String accountID = battleSteps.get(i).getOwernAccId();
			if(accountID.equals(targetAccID))
			{
				processBattleStep(i, nextBattleStep, true, steps);
				break;
			}
			else
				nextBattleStep.addIgnoreId(accountID);
		}
	}

	private void battleStepCheckCard(MBattleStep battleStep, MBattleStep nextBattleStep)
	{
		int discard = battleStep.getCard(0);
		boolean checkSelf = battleStep.getOwernAccId().equals(nextBattleStep.getOwernAccId());
		// 之前操作是否转弯豆
		boolean isKong = battleStep.getPlayType() == PlayType.Kong;

		String accountID = battleStep.getOwernAccId();
		ArrayList<String> huList = new ArrayList<String>();
		for(int i = 0; i < MajhongHelper.ONE_CARD_MAX; i++)
		{
			if((!checkSelf && i == 0) || nextBattleStep.containsIgoreID(accountID) || (!checkSelf && passHuAcIds.contains(accountID)))
			{
				accountID = getNextPlayerAcc(accountID);
				continue;
			}

			boolean canPingHu = checkSelf || isKong || nextBattleStep.isGangDiscard() || isbattleStepKongPre(accountID, battleStep.getOwernAccId());
			HuType huType = checkHu(accountID, discard, canPingHu);
			if(huType.ordinal() != HuType.Unknow.ordinal())
				huList.add(accountID);

			if(checkSelf)
				break;

			accountID = getNextPlayerAcc(accountID);
		}

		if(!checkSelf)
			nextBattleStep.setCard(0, discard);

		nextBattleStep.setTargetAccId(battleStep.getOwernAccId());
		nextBattleStep.setHuLists(huList);
		if(huList.size() > 1)
		{
			nextBattleStep.setPlayType(PlayType.Hu);
			if(isKong)
				nextBattleStep.setWinType(PlayType.QiangGang);
			else if(nextBattleStep.isGangDiscard())
				nextBattleStep.setWinType(PlayType.GangHouPao);
			else if(hasObtain(battleStep.getOwernAccId(), PlayType.ReadyHand))
				nextBattleStep.setWinType(PlayType.KillReadyHand);
			else
				nextBattleStep.setWinType(PlayType.DiscardOther);
			return;
		}
		else if(huList.size() == 1)
		{
			nextBattleStep.setOwernAccId(huList.get(0));
			nextBattleStep.setPlayType(PlayType.CanHu);

			if(isKong)
				nextBattleStep.setWinType(PlayType.QiangGang);
			else if(nextBattleStep.isGangDraw())
				nextBattleStep.setWinType(PlayType.GangShangHu);
			else if(checkSelf)
				nextBattleStep.setWinType(PlayType.WinSelf);
			else if(nextBattleStep.isGangDiscard())
				nextBattleStep.setWinType(PlayType.GangHouPao);
			else if(hasObtain(battleStep.getOwernAccId(), PlayType.ReadyHand))
				nextBattleStep.setWinType(PlayType.KillReadyHand);
			else
				nextBattleStep.setWinType(PlayType.DiscardOther);
		}

		// 如果转弯豆,不检测其他情况
		if(isKong)
			return;

		accountID = battleStep.getOwernAccId();
		for(int i = 0; i < MajhongHelper.ONE_CARD_MAX; i++)
		{
			// 跳过自己或者选择过的玩家
			if((!checkSelf && i == 0) || nextBattleStep.containsIgoreID(accountID))
			{
				accountID = getNextPlayerAcc(accountID);
				continue;
			}

			int playType = nextBattleStep.getPlayType();
			if(playType != PlayType.Idle && accountID.equals(nextBattleStep.getOwernAccId()) == false)
			{
				accountID = getNextPlayerAcc(accountID);
				continue;
			}
			
			/* 检测听
			 * 摸牌时，可以胡，听，杠，
					cards[0] 摸牌
					cards[1-3] 明杠
					cards[4-6] 暗杠
					cards[7+] 听牌
			 */
			{
				if(checkSelf && i == 0)
				{
					int[] finds = checkTing(accountID, discard);
					if(finds != null && finds[0] > 0)
					{
						// 设置暗杠明杠位
						setKongCards(nextBattleStep);
						for(int j = 0; j < finds.length; j++)
						{
							if(finds[j] <= 0)
								break;

							nextBattleStep.setCard(finds[j]);
						}
						nextBattleStep.setOwernAccId(accountID);
						nextBattleStep.setPlayType(PlayType.CanReadyHand);
					}
				}
			}

			// 检测碰(pong),此时不检测自己
			if(!accountID.equals(battleStep.getOwernAccId()))
			{
				if(playType != PlayType.Idle && accountID.equals(nextBattleStep.getOwernAccId()) == false)
				{
					accountID = getNextPlayerAcc(accountID);
					continue;
				}

				if(checkPong(accountID, discard) == discard)
				{
					nextBattleStep.setPlayType(PlayType.CanPong);
					nextBattleStep.setOwernAccId(accountID);
				}
			}

			// 检测杠
			{
				if(checkKong(accountID, battleStep.getOwernAccId(), discard, nextBattleStep))
				{
					nextBattleStep.setOwernAccId(accountID);
					break;
				}
			}

			accountID = getNextPlayerAcc(accountID);
			if(checkSelf)
				break;
		}
	}

	private void deletePassHu(String accountID)
	{
		// 到玩家摸牌时，删除过胡标记
		// 软报玩家只可以自摸
		if(passHuAcIds.contains(accountID) && !hasObtain(accountID, PlayType.ReadyHand))
			passHuAcIds.remove(accountID);
	}

	private void calculateBattleResult(List<MBattleStep> steps)
	{
		MBattleStep lastBattleStep = battleSteps.get(battleSteps.size() - 1);
		// 检测冲锋鸡，责任鸡
		checkChiken(lastBattleStep, steps);

		//为了保证数据的顺序 先把数据发出去
		if(steps.size() > 0){
			Map<String, BWBattleStepRES>  results = gameExt.getBattleService().battleStepToRES(steps);

			for(Map.Entry<String, BWBattleStepRES> userRes : results.entrySet()){
				gameExt.send(CmdsUtils.CMD_BATTLE_STEP, userRes.getValue().toSFSObject(), gameExt.getParentRoom().getUserByName(userRes.getKey()));
			}
		}

		BattleService battleService = this.gameExt.getBattleService();
		// 扣除房卡
		// TODO: 房卡提到service层处理
		int battleTime = roomInfo.getBattleTime();
		if(battleTime == 1)
			battleService.modifyAccountInfo(roomInfo.getRoomID(), roomInfo.getOwner(), getPlayerID(roomInfo.getOwner()), true, 0);
		// 增加战斗场次
		roomInfo.addBattleTime();

		BattleData builder = new BattleData();
		builder.setBankerId(getPlayerID(bankerId));
		builder.setBattleTime(battleTime);
		builder.setBattleCount(roomInfo.getBattleCount());

		BattleStep stepBuilder = new BattleStep();
		stepBuilder.setPlayType(lastBattleStep.getPlayType());
		stepBuilder.setRemainCardCount(cardPools.size());
		stepBuilder.setOwnerId(builder.getBankerId());
		builder.addBattleSteps(stepBuilder);

		ConcurrentHashMap<String, Integer> scores = new ConcurrentHashMap<String, Integer>();
		ConcurrentHashMap<String, BattleBalance> blances = new ConcurrentHashMap<String, BattleBalance>();

		if(lastBattleStep.getPlayType() == PlayType.He)
		{
			ConcurrentHashMap<String, HuType> tingPlayers = getTingPlayers();
			for(Map.Entry<String, Player> entry : roomInfo.getPlayers().entrySet())
			{
				scores.put(entry.getKey(), 0);
				Player player = entry.getValue();
				BattleBalance balanceBuilder = new BattleBalance();
				balanceBuilder.setPlayerId(player.getID());

				boolean showHuType = tingPlayers.size() > 0 && tingPlayers.size() < MajhongHelper.ONE_CARD_MAX;

				// 和牌设置听牌的类型
				if(tingPlayers.containsKey(player.getAccountID()))
					balanceBuilder.setWinType(showHuType ? tingPlayers.get(player.getAccountID()).ordinal() : HuType.Ting.ordinal());
				else
					balanceBuilder.setWinType(HuType.Unknow.ordinal());

				// 结算界面牌
				for(int card : playCards.get(entry.getKey()))
					balanceBuilder.addCards(card);

				// 和牌只设置杠，碰信息用于显示
				for(MJObtain obtain : obtains.get(player.getAccountID()))
				{
					if(obtain.getType() != PlayType.Pong &&
							obtain.getType() != PlayType.Kong &&
							obtain.getType() != PlayType.DotKong &&
							obtain.getType() != PlayType.CealedKong)
						continue;

					// 统计明杠、暗杠次数
					if(obtain.getType() == PlayType.Pong)
						battleCensus.get(player.getAccountID()).addKong();
					else if(obtain.getType() == PlayType.CealedKong)
						battleCensus.get(player.getAccountID()).addCealedKong();

					CardBalance cardBlanceBuilder = new CardBalance();
					cardBlanceBuilder.setType(obtain.getType());
					cardBlanceBuilder.setCard(obtain.getCard());
					balanceBuilder.addBalances(cardBlanceBuilder);
				}
				blances.put(player.getAccountID(), balanceBuilder);
			}

			// 计算和牌番值
			if(tingPlayers.size() < MajhongHelper.ONE_CARD_MAX)
			{
				for(String accountId : playerAcIds)
				{
					if(tingPlayers.containsKey(accountId))
						continue;

					for(String subId : playerAcIds)
					{
						if(accountId.equals(subId) || !tingPlayers.containsKey(subId))
							continue;

						calculateScore(scores, subId, MBattleStep.getHuTypeScore(tingPlayers.get(subId)), accountId);
					}
				}
			}

			// 计算庄家
			if(tingPlayers.size() > 0 && tingPlayers.size() < MajhongHelper.ONE_CARD_MAX)
			{
				if(tingPlayers.size() == 3)
				{
					for(String playerId : playerAcIds)
					{
						if(tingPlayers.containsKey(playerId) == false)
						{
							bankerId = playerId;
							break;
						}
					}
				}
				else if(tingPlayers.containsKey(bankerId) == false)
				{
					for(int i = 0; i < MajhongHelper.ONE_CARD_MAX; i++)
					{
						bankerId = getNextPlayerAcc(bankerId);
						if(tingPlayers.containsKey(bankerId))
							break;
					}
				}
			}
		}
		else if(lastBattleStep.getPlayType() == PlayType.Hu)
		{
			String targetAccId = lastBattleStep.getTargetAccId();
			int discard = lastBattleStep.getCard(0);
			int normalChicken = MajhongHelper.CardType.Tiao.Value();
			int flopChicken = 0;
			if(cardPools.size() > 0)
			{
				flopChicken = cardPools.get(0);
				flopChicken = (flopChicken / 10) * 10 + (flopChicken % 10 == 9 ? 1 : flopChicken % 10 + 1);
			}

			// 天胡，增加报听番，不加牌型番数
			boolean isHardReadHand = false;
			if(lastBattleStep.isSelf() && disposeCards.get(targetAccId).size() <= 0 && obtains.get(targetAccId).size() <= 0 )
			{
				setObtainData(targetAccId, PlayType.HardReadHand, discard);
				isHardReadHand = true;
			}

			// 设置翻牌鸡,普通鸡
			boolean isGoldChicken = flopChicken == normalChicken;
			for(String accountId : playerAcIds)
			{
				setObtainData(accountId, PlayType.FlopChicken, flopChicken);
				setObtainData(accountId, PlayType.NormalChicken, normalChicken);
			}

			// 抢杠的话，杠牌无效,鸡牌和普通鸡无效
			int winType = lastBattleStep.getWinType();
			boolean winSelf = winType == PlayType.WinSelf || winType == PlayType.GangShangHu;
			if(winType == PlayType.QiangGang)
			{
				invalidObtainPongChicken(targetAccId);
				removeObtainData(targetAccId, PlayType.Kong, discard);
				removeCard(playCards.get(targetAccId), discard);
			}
			else if(winType == PlayType.GangHouPao)
			{
				invalidObtainPongChicken(targetAccId);
				removeObtainData(targetAccId, PlayType.Kong, discard);
				removeObtainData(targetAccId, PlayType.CealedKong, discard);
				removeObtainData(targetAccId, PlayType.DotKong, discard);
				removeCard(disposeCards.get(targetAccId), discard);
			}
			else if(!winSelf)
				removeCard(disposeCards.get(targetAccId), discard);

			// 设置胜利原因
			ArrayList<String> tingLists = new ArrayList<String>();
			for(String accountID : playerAcIds)
			{
				scores.put(accountID, 0);
				// 添加balance记录
				BattleBalance blanceBuilder = new BattleBalance();
				blanceBuilder.setPlayerId(getPlayerID(accountID));
				blances.put(accountID, blanceBuilder);
				if(lastBattleStep.getHuLists().contains(accountID))
				{
					tingLists.add(accountID);
					if(!winSelf)
					{
						playCards.get(accountID).add(discard);
						setObtainData(accountID, winType, discard, targetAccId);
						// 统计接炮次数
						battleCensus.get(accountID).addWinOther();
					}
					else
					{
						// 统计自摸次数
						battleCensus.get(accountID).addWinSelf();
						setObtainData(accountID, winType, discard, null, !isHardReadHand);
						// 杠上胡，发送标记自摸给客户端显示，这个自摸不计算番值
						if(lastBattleStep.getWinType() == PlayType.GangShangHu)
							setObtainData(accountID, PlayType.WinSelf, discard, null, false);
					}
				}
				else
				{
					HuType tingType = majhongHelper.checkTing(getCheckPai(accountID, 0), new int[MajhongHelper.PLAY_CARD_COUNT], false);
					if(tingType != HuType.Unknow)
						tingLists.add(accountID);

					if(!winSelf && targetAccId.equals(accountID))
					{
						// 统计点炮次数
						battleCensus.get(targetAccId).addDiscardOther();

						// 点炮玩家增加点炮状态
						CardBalance cardBuilder = new CardBalance();
						cardBuilder.setType(PlayType.DiscardOther);
						cardBuilder.setCard(discard);
						blanceBuilder.addBalances(cardBuilder);
					}
				}
			}

			// 杀报， 自摸时，如果有其他玩家软听，那么也算杀报
			if(winType == PlayType.KillReadyHand)
				setObtainData(targetAccId, PlayType.BeKilledReadHand, discard);
			else if(winSelf)
			{
				boolean addKillReadHand = false;
				for(String accountID : playerAcIds)
				{
					if(lastBattleStep.getHuLists().contains(accountID))
						continue;

					if(hasObtain(accountID, PlayType.ReadyHand))
					{
						setObtainData(accountID, PlayType.BeKilledReadHand, 0);
						addKillReadHand = true;
					}
				}

				if(addKillReadHand)
					setObtainData(lastBattleStep.getOwernAccId(), PlayType.KillReadyHand, discard);
			}

			// 计算番
			for(String accountID : playerAcIds)
			{
				boolean isWiner = lastBattleStep.getHuLists().contains(accountID);
				BattleBalance blanceBuilder = blances.get(accountID);
				HuType huType = HuType.Unknow;
				if(isWiner)
					huType = checkHu(accountID, -1 * discard, true);
				else if(tingLists.contains(accountID))
					huType = HuType.Ting;

				blanceBuilder.setWinType(huType.ordinal());

				// 设置显示card
				for(int card : playCards.get(accountID))
					blanceBuilder.addCards(card);

				if(isWiner && !isHardReadHand)
				{
					// 计算牌型番值
					if(winSelf)
						calculateScore(scores, accountID, MBattleStep.getHuTypeScore(huType), null);
					else
						calculateScore(scores, accountID, MBattleStep.getHuTypeScore(huType), targetAccId);
				}

				// 
				for(MJObtain obtain : obtains.get(accountID))
				{
					if(obtain.getType() == PlayType.DiscardOther)
					{
						continue;
					}
					CardBalance cardBuilder = new CardBalance();
					int score = MBattleStep.getObtypeScore(obtain.getType());
					if(obtain.getType() == PlayType.NormalChicken)
					{
						// 普通鸡
						// 计算规则:叫嘴，没有被烧
						if(tingLists.contains(accountID))
						{
							if(obtain.isValid())
							{
								int cardCount = getCardCount(accountID, obtain.getCard());
								if(hasObtain(accountID, PlayType.ChargeChicken))
									cardCount = Math.max(0, cardCount - 1);
								score = score * cardCount * (isGoldChicken ? 2 : 1);
								calculateScore(scores, accountID, score, null);
							}
							else
								score = 0;
						}
						else
						{
							// 反赔计算规则:出手和碰杠幺鸡需要反赔
							score *= -1 * (isGoldChicken ? 2 : 1);
							int cardCount = 0;
							for(int card : disposeCards.get(accountID))
								if(card == normalChicken)
									cardCount++;
							if(hasObtain(accountID, PlayType.ChargeChicken))
								cardCount = Math.max(0, cardCount - 1);
							for(MJObtain ob : obtains.get(accountID))
							{
								if(ob.getCard() != normalChicken)
									continue;

								if(ob.getType() == PlayType.Pong)
								{
									cardCount += 3;
									break;
								}
								else if(ob.getType() == PlayType.Kong ||
										ob.getType() == PlayType.CealedKong ||
										ob.getType() == PlayType.DotKong)
								{
									cardCount += 4;
									break;
								}
							}
							score *= cardCount;
							calculateScore(scores, accountID, score, null, tingLists);
						}
					}
					else if(obtain.getType() == PlayType.FlopChicken)
					{
						// 翻牌鸡
						// 计算规则:叫嘴，不是金鸡，没有被烧
						if(tingLists.contains(accountID) && obtain.isValid() && !isGoldChicken)
						{
							int cardCount = getCardCount(accountID, obtain.getCard());
							score = score * cardCount;
							calculateScore(scores, accountID, score, null);
						}
						else
							score = 0;
					}
					else if(obtain.getType() == PlayType.ChargeChicken)
					{
						// 冲锋鸡
						score = (tingLists.contains(accountID) ? 1 : -1) *
								score *
								(isGoldChicken ? 2 : 1);

						// 如果被烧，只计算反赔
						if(score > 0 && !obtain.isValid())
							score = 0;

						calculateScore(scores, accountID, score, null, tingLists);
					}
					else if(obtain.getType() == PlayType.DutyChicken)
					{
						// 责任鸡
						// 计算规则:叫嘴，不计算金鸡的影响
						boolean invliad = false;
						if((winType == PlayType.GangHouPao &&
								winType == PlayType.QiangGang) &&
								lastBattleStep.getTargetAccId().equals(obtain.getTarget()))
							invliad = true;

						if(tingLists.contains(obtain.getTarget()))
						{
							if(invliad)
								score = 0;
						}
						else if(tingLists.contains(accountID))
							score *= -1;
						else
							score = 0;

						calculateScore(scores, obtain.getTarget(), score, accountID, tingLists);

						// 反向添加责任鸡显示
						CardBalance reverseCardBuilder = new CardBalance();
						reverseCardBuilder.setCard(obtain.getCard());
						reverseCardBuilder.setType(obtain.getType());
						reverseCardBuilder.setScore(score);
						blances.get(obtain.getTarget()).addBalances(reverseCardBuilder);

						score *= -1;
					}
					else if(obtain.getType() == PlayType.Kong ||
							obtain.getType() == PlayType.CealedKong ||
							obtain.getType() == PlayType.DotKong )
					{
						// 统计明杠、暗杠次数
						if(obtain.getType() == PlayType.Kong)
							battleCensus.get(accountID).addKong();
						else if(obtain.getType() == PlayType.CealedKong)
							battleCensus.get(accountID).addCealedKong();

						// 计算豆, 叫嘴并且没有被烧
						if(tingLists.contains(accountID) && obtain.isValid())
						{
							cardBuilder.setCard(obtain.getCard());
							cardBuilder.setScore(score);
							calculateScore(scores, accountID, score, obtain.getTarget());

							// 反向添加点杠显示
							if(obtain.getType() == PlayType.DotKong)
							{
								CardBalance reverseCardBuilder = new CardBalance();
								reverseCardBuilder.setCard(obtain.getCard());
								reverseCardBuilder.setType(PlayType.DotKongOther);
								reverseCardBuilder.setScore(-1 * score);
								blances.get(obtain.getTarget()).addBalances(reverseCardBuilder);
							}
						}
						else
							score = 0;
					}
					else if(obtain.getType() == PlayType.WinSelf ||
							obtain.getType() == PlayType.GangShangHu)
					{
						// 自摸作为标记时，不算番
						if(!obtain.isValid() && obtain.getType() == PlayType.WinSelf)
							score = 0;

						calculateScore(scores, accountID, score, null);
					}
					else if(obtain.getType() == PlayType.ReadyHand ||
							obtain.getType() == PlayType.HardReadHand)
					{
						if(isWiner && obtain.isValid())
						{
							if(winSelf)
								calculateScore(scores, accountID, score, null);
							else
								calculateScore(scores, accountID, score, targetAccId);
						}
						else
							score = 0;
					}
					else if(obtain.getType() == PlayType.BeKilledReadHand)
					{
						// 被杀报玩家计算自己显示的数值，不计算真实分数
						score *= lastBattleStep.getHuLists().size();
					}
					else if(obtain.getType() == PlayType.KillReadyHand)
					{
						// 杀报玩家计算真实分数
						int killCount = 0;
						if(winSelf)
						{
							for(String playerID : playerAcIds)
							{
								if(playerID.equals(accountID))
									continue;

								if(hasObtain(playerID, PlayType.ReadyHand))
								{
									killCount++;
									calculateScore(scores, accountID, score, playerID);
								}
							}
						}
						else
						{
							killCount++;
							calculateScore(scores, accountID, score, targetAccId);
						}

						score *= killCount;
					}
					else if(obtain.getType() == PlayType.GangHouPao||
							obtain.getType() == PlayType.QiangGang)
						calculateScore(scores, accountID, score, targetAccId);

					cardBuilder.setType(obtain.getType());
					cardBuilder.setCard(obtain.getCard());
					cardBuilder.setScore(score);
					blanceBuilder.addBalances(cardBuilder);
				}
			}

			// 2.0规则中，抢杠只是赔付所有胡牌着9番.
			// 抢杠包赔 , 
//			if(lastBattleStep.getWinType() == PlayType.QiangGang)
//			{
//				ArrayList<String> replaceIds = new ArrayList<String>();
//				for(Map.Entry<String, Integer> entry : scores.entrySet())
//				{
//					if(targetAccId.equals(entry.getKey()))
//						continue;
//					
//					if(entry.getValue() < 0)
//						replaceIds.add(entry.getKey());
//				}
//				
//				for(String repaceId : replaceIds)
//					calculateScore(scores, repaceId, -scores.get(repaceId), targetAccId);
//			}

			// 计算庄家
			if(lastBattleStep.getHuLists().size() > 2)
				bankerId = targetAccId;
			else if(lastBattleStep.getHuLists().size() == 2)
			{
				for(int i = 0; i < MajhongHelper.ONE_CARD_MAX; i++)
				{
					bankerId = getNextPlayerAcc(bankerId);
					if(lastBattleStep.getHuLists().contains(bankerId))
						break;
				}
			}
			else
				bankerId = lastBattleStep.getHuLists().get(0);
		}

		for(Map.Entry<String, BattleBalance> entry : blances.entrySet())
		{
			entry.getValue().setWinScore(scores.get(entry.getKey()));
			entry.getValue().setWinPoint(scores.get(entry.getKey()));
			builder.addBattleBalances(entry.getValue());

			// 统计积分
			battleCensus.get(entry.getKey()).addPoint(scores.get(entry.getKey()));
		}

		// 更新玩家积分信息 scores.
		for(Map.Entry<String, Player> entry : roomInfo.getPlayers().entrySet())
			battleService.modifyAccountInfo(roomInfo.getRoomID(), entry.getKey(), entry.getValue().getID(), false, scores.get(entry.getKey()));

		// 总战绩
		for(String accountId : playerAcIds)
			builder.addBattleCensuss(battleCensus.get(accountId).toBattleCensus());

		// 发送客户端
/*		for(String accountId : playerAcIds)
		{*/
			BWBattleStepRES res = new BWBattleStepRES();
			res.setResult(GlobalConstants.BW_Battle_Step_SUCCESS);
//			res.setAccountId(accountId);
			res.setBattleData(builder);
//			battleService.sendStepToGateway(roomInfo.getRoomID(), accountId, res);
		for(String accountId : playerAcIds){
			res.setAccountId(accountId);
			this.gameExt.send(CmdsUtils.CMD_BATTLE_STEP, res.toSFSObject(), this.gameExt.getParentRoom().getUserByName(accountId));
		}
		steps.clear();
//		}

		// 重置玩家状态
		for(Map.Entry<String, Player> entry : roomInfo.getPlayers().entrySet())
			entry.getValue().setPlayState(Player.PlayState.Idle);

		// 设置所有玩家的牌信息
		for(Map.Entry<String, Player> entry : roomInfo.getPlayers().entrySet())
		{
			BattleDealCard dealCardBuilder = new BattleDealCard();
			dealCardBuilder.setPlayerId(entry.getValue().getID());
			for(Map.Entry<Integer, Integer> cardEntry : playerDealCards.get(entry.getKey()).entrySet())
				for(int i = 0; i< cardEntry.getValue(); i++)
					dealCardBuilder.addCards(cardEntry.getKey());

			builder.addBattleDealCards(dealCardBuilder);
		}

		//调用存储数据库
        try {
            battleService.recordBattleData(roomInfo.getRoomID(), startTime, builder);
        }catch (Exception e){
            e.printStackTrace();
        }

		// 清理牌局
		cardPools.clear();
		playerDealCards.clear();
		playCards.clear();
		disposeCards.clear();
		obtains.clear();
		battleSteps.clear();
		passHuAcIds.clear();
		startTime = 0l;

		// 完成所有牌局，解散房间
		if(roomInfo.getBattleCount() < roomInfo.getBattleTime())
		{
			// 设置房间状态为打牌状态，为退出房间使用
			roomInfo.setInBattle(false);
			RoomService roomService = new RoomService();
			roomService.autoDestroyRoom(roomInfo.getRoomID());
		}
	}

	private int getCardCount(String accountID, int discard)
	{
		int sumCount = 0;
		for(int card : playCards.get(accountID))
			if(card == discard)
				sumCount++;

		for(int card : disposeCards.get(accountID))
			if(card == discard)
				sumCount++;

		return sumCount;
	}

	private void calculateScore(ConcurrentHashMap<String, Integer> scores, String accountID, int score, String targetID)
	{
		calculateScore(scores, accountID, score, targetID, null);
	}

	private void calculateScore(ConcurrentHashMap<String, Integer> scores, String accountID, int score, String targetID, ArrayList<String> tingLists)
	{
		for(String playerId : playerAcIds)
		{
			if(accountID.equals(playerId) ||
					(targetID != null && targetID.equals(playerId) == false))
				continue;

			// tingLists不为空时，反赔时，只反赔叫嘴玩家
			if(score < 0 && tingLists != null && !tingLists.contains(playerId))
				continue;

			scores.put(accountID, scores.get(accountID) + score);
			scores.put(playerId, scores.get(playerId) - score);
		}
	}

	private ConcurrentHashMap<String, HuType> getTingPlayers()
	{
		ConcurrentHashMap<String, HuType> tingPlayers = new ConcurrentHashMap<String, HuType>();
		for(String accountID : playerAcIds)
		{
			HuType tingType = majhongHelper.checkTing(getCheckPai(accountID, 0), new int[MajhongHelper.PLAY_CARD_COUNT], false);
			if(tingType != HuType.Unknow)
				tingPlayers.put(accountID, tingType);
		}

		return tingPlayers;
	}

	private void removeCard(ArrayList<Integer> list, int card)
	{
		int index = list.indexOf(card);
		if(index >= 0)
			list.remove(index);
	}

	private void invalidObtainPongChicken(String accountID)
	{
		invalidObtain(accountID, PlayType.Kong);
		invalidObtain(accountID, PlayType.DotKong);
		invalidObtain(accountID, PlayType.CealedKong);
		invalidObtain(accountID, PlayType.ChargeChicken);
		invalidObtain(accountID, PlayType.FlopChicken);
		invalidObtain(accountID, PlayType.NormalChicken);
	}

	private void invalidObtain(String accountID, int card, int type)
	{
		for(MJObtain obtain : obtains.get(accountID))
		{
			if(obtain.getType() == type && (card == 0 || card == obtain.getCard()))
				obtain.setValid(false);
		}
	}

	private void invalidObtain(String accountID, int type)
	{
		invalidObtain(accountID, 0, type);
	}

	private MJObtain removeObtainData(String accountID, int type, int card)
	{
		ArrayList<MJObtain> targetObs = obtains.get(accountID);
		for(int i = targetObs.size() - 1; i >= 0 ; i--)
		{
			if(targetObs.get(i).getType() == type &&
					targetObs.get(i).getCard() == card)
			{
				return targetObs.remove(i);
			}
		}
		return null;
	}

	private void setObtainData(String accountID, int type, int card)
	{
		setObtainData(accountID, type, card, null);
	}

	private void setObtainData(String accountID, int type, int card ,String targetID)
	{
		setObtainData(accountID, type, card, targetID, true);
	}

	private void setObtainData(String accountID, int type, int card ,String targetID, boolean valid)
	{
		MJObtain obtain = new MJObtain(type, card, targetID, valid);
		obtains.get(accountID).add(obtain);
	}

	private boolean hasObtain(String accountID, int type, int card)
	{
		for(MJObtain obtain : obtains.get(accountID))
		{
			if(obtain.getType() == type && (card <= 0 || obtain.getCard() == card))
				return true;
		}
		return false;
	}

	private boolean hasObtain(String accountID, int type)
	{
		return hasObtain(accountID, type, 0);
	}

	public String getNextPlayerAcc(String accountID)
	{
		int indexOfAccount = playerAcIds.indexOf(accountID);
		if(indexOfAccount == playerAcIds.size() - 1)
			indexOfAccount = 0;
		else
			indexOfAccount += 1;
		return playerAcIds.get(indexOfAccount);
	}

	// 查询该玩家之前的连续操作是否是杠, 是否听牌，是否有杠
	private boolean isbattleStepKongPre(String accountID, String targetID)
	{
		// 如果target听牌，那么不需要检测玩家是否有豆，听牌
		if(hasObtain(targetID, PlayType.ReadyHand))
			return true;

		for(MJObtain obtain : obtains.get(accountID))
		{
			if(obtain.getType() == PlayType.Kong ||
					obtain.getType() == PlayType.CealedKong ||
					obtain.getType() == PlayType.DotKong ||
					obtain.getType() == PlayType.ReadyHand)
				return true;
		}

		return false;
	}

	// 洗牌
	private void randomSuffle()
	{
		putCardByType(CardType.Wan, cardPools);
		putCardByType(CardType.Tiao, cardPools);
		putCardByType(CardType.Tong, cardPools);

		Random random = new Random();
		for(int i = 0; i< cardPools.size(); i++)
		{
			int pos = random.nextInt(cardPools.size());
			int temp = cardPools.get(i);
			cardPools.set(i, cardPools.get(pos));
			cardPools.set(pos, temp);
		}
		random = null;
		startTime = System.currentTimeMillis();
	}

	// 生成牌色对应牌值
	private void putCardByType(CardType cardType, ArrayList<Integer> cardPools)
	{
		for(int i = cardType.Value(); i < cardType.Value() + MajhongHelper.ONE_TYPE_MAX; i++)
			for(int j = 0; j < MajhongHelper.ONE_CARD_MAX; j++)
				cardPools.add(i);
	}

	// 发牌
	private void dealPlayerCard()
	{
		playerDealCards.clear();
		playCards.clear();
		disposeCards.clear();
		passHuAcIds.clear();
		for(String accountId : playerAcIds)
		{
			ConcurrentHashMap<Integer, Integer> dealMap = new ConcurrentHashMap<Integer, Integer>();
			playerDealCards.put(accountId, dealMap);
			playCards.put(accountId, new ArrayList<Integer>());
			disposeCards.put(accountId, new ArrayList<Integer>());
			obtains.put(accountId, new ArrayList<MJObtain>());
			Random random = new Random();
			for(int i = 0; i < MajhongHelper.PLAY_CARD_COUNT + (accountId.equals(bankerId) ? 1 : 0); i++)
			{
				int pos = random.nextInt(cardPools.size());
				int card = cardPools.remove(pos);
				playCards.get(accountId).add(card);
				if(dealMap.containsKey(card))
					dealMap.put(card, dealMap.get(card) + 1);
				else
					dealMap.put(card, 1);
			}
		}

		MBattleStep battleStep = new MBattleStep();
		battleStep.setOwernAccId(bankerId);
		battleStep.setPlayType(PlayType.Deal);
		battleSteps.add(battleStep);

		// 设置房间状态为打牌状态，为退出房间使用
		roomInfo.setInBattle(true);
	}

	private void checkChiken(MBattleStep battleStep, List<MBattleStep> steps)
	{
		boolean checkChiken = true;
		for(String accountID : playerAcIds)
		{
			if(hasObtain(accountID, PlayType.ChargeChicken) ||
					hasObtain(accountID, PlayType.DutyChicken))
			{
				checkChiken = false;
				break;
			}
		}

		if(checkChiken)
		{
			MBattleStep nextBattleStep = new MBattleStep();
			int chickenCard = MajhongHelper.CardType.Tiao.Value();
			switch (battleStep.getPlayType()) {
				case PlayType.ReadyHand:
				case PlayType.Discard:
				case PlayType.Pong:
				case PlayType.Kong:
				case PlayType.DotKong:
					if(battleStep.getCard(0) == chickenCard)
					{
						nextBattleStep.setCard(chickenCard);
						if(PlayType.Discard == battleStep.getPlayType() ||
								PlayType.ReadyHand == battleStep.getPlayType())
						{
							setObtainData(battleStep.getOwernAccId(), PlayType.ChargeChicken, chickenCard);
							nextBattleStep.setPlayType(PlayType.ChargeChicken);
							nextBattleStep.setOwernAccId(battleStep.getOwernAccId());
						}
						else
						{
							setObtainData(battleStep.getTargetAccId(), PlayType.DutyChicken, chickenCard, battleStep.getOwernAccId());
							nextBattleStep.setPlayType(PlayType.DutyChicken);
							nextBattleStep.setOwernAccId(battleStep.getTargetAccId());
						}
						battleSteps.add(nextBattleStep);
						sendBattleStepSuccess(steps);
					}
					break;
				case PlayType.Hu:
					// 没有责任鸡，冲锋鸡的情况下，一个玩家打出幺鸡点炮，被点玩家的幺鸡输量大于等于3，那么算责任鸡
					int huSize = battleStep.getHuLists().size();
					if(huSize > 0 && !battleStep.isSelf() && battleStep.cardSize() > 0 && battleStep.getCard(0) == chickenCard)
					{
						for(String huAccId : battleStep.getHuLists())
						{
							int chickCount = 0;
							for(int card : playCards.get(huAccId))
								if(card == chickenCard)
									chickCount++;

							if(chickCount >= 2)
							{
								setObtainData(battleStep.getTargetAccId(), PlayType.DutyChicken, chickenCard, huAccId);
								nextBattleStep.setPlayType(PlayType.DutyChicken);
								nextBattleStep.setOwernAccId(battleStep.getTargetAccId());
								nextBattleStep.setCard(chickenCard);
								battleSteps.add(battleSteps.size() - 1, nextBattleStep);
								sendBattleStepSuccess(nextBattleStep, false, steps);
								break;
							}
						}
					}
					break;
			}
		}
	}

	private int checkPong(String accountID, int drawCard)
	{
		// 如果听牌，不可以进行碰
		// 如果听牌，不检测杠
		if(hasObtain(accountID, PlayType.ReadyHand))
			return -1;

		return majhongHelper.checkPong(getCheckPai(accountID, 0), drawCard);
	}

	private boolean enableKong(String accountID, int kongType, int drawCard)
	{
		int[] checkPai = new int[CardType.Tong.Value() + MajhongHelper.ONE_TYPE_MAX];
		for(int card : playCards.get(accountID))
			checkPai[card] += 1;

		if(checkPai[drawCard] < 3)
			return false;

		boolean containsOb = false;
		for(MJObtain obtain : obtains.get(accountID))
		{
			if(obtain.getType() == PlayType.Pong &&
					obtain.getCard() == drawCard)
			{
				containsOb = true;
				break;
			}
		}
		if(kongType == PlayType.DotKong)
			return !containsOb;
		else
		{
			if(kongType == PlayType.Kong)
				return containsOb;
			else
				return !containsOb;
		}
	}

	private boolean checkKong(String accountID, String targetAccID, int drawCard, MBattleStep battleStep)
	{
		// 如果听牌，不检测杠
		if(hasObtain(accountID, PlayType.ReadyHand))
			return false;

		int[] checkPai = new int[CardType.Tong.Value() + MajhongHelper.ONE_TYPE_MAX];
		if(drawCard > 0)
			checkPai[drawCard] += 1;
		for(int card : playCards.get(accountID))
			checkPai[card] += 1;

		int[] cealedKong = new int[MajhongHelper.ONE_CARD_MAX - 1];
		int[] kong = new int[MajhongHelper.ONE_CARD_MAX - 1];
		int cealIndex = 0;
		int kongIndex = 0;
		for(int i = 0; i < checkPai.length; i++)
		{
			if(checkPai[i] < MajhongHelper.ONE_CARD_MAX)
				continue;

			boolean containsOb = false;
			boolean skip = false;
			for(MJObtain obtain : obtains.get(accountID))
			{
				if((obtain.getType() == PlayType.Kong ||
						obtain.getType() == PlayType.CealedKong ||
						obtain.getType() == PlayType.DotKong) && obtain.getCard() == i)
				{
					skip = true;
					break;
				}

				if(obtain.getType() == PlayType.Pong &&
						obtain.getCard() == i)
				{
					containsOb = true;
					break;
				}
			}

			if(skip)
				continue;

			if(accountID.equals(targetAccID))
			{
				if(containsOb)
				{
					kong[kongIndex++] = i;
					battleStep.setPlayType(PlayType.CanKong);

					// 明杠只有第一次摸牌算番
					if(i != drawCard)
						invalidObtain(accountID, i, PlayType.Pong);
				}
				else
				{
					cealedKong[cealIndex++] = i;
					battleStep.setPlayType(PlayType.CanCealedKong);
				}
			}
			else if(containsOb == false && i == drawCard)
				battleStep.setPlayType(PlayType.CanDotKong);
		}

		if(kong[0] > 0 || cealedKong[0] > 0)
		{
			setKongCards(battleStep);
			for(int i = 0; i < (MajhongHelper.ONE_CARD_MAX - 1) * 2; i++)
			{
				if(i < MajhongHelper.ONE_CARD_MAX - 1)
					battleStep.setCard(i + 1, kong[i]);
				else
					battleStep.setCard(i + 1, cealedKong[i - MajhongHelper.ONE_CARD_MAX + 1]);
			}
		}
		return (battleStep.getPlayType() & PlayType.CanKong) == PlayType.CanKong ||
				(battleStep.getPlayType() & PlayType.CanDotKong) == PlayType.CanDotKong ||
				(battleStep.getPlayType() & PlayType.CanCealedKong) == PlayType.CanCealedKong;
	}

	private void setKongCards(MBattleStep battleStep)
	{
		for(int j = 0; j < (MajhongHelper.ONE_CARD_MAX - 1) * 2; j++)
			battleStep.setCard(0);
	}

	// 检测是否可以听牌
	private int[] checkTing(String accountID, int drawCard)
	{
		if(disposeCards.get(accountID).size() > 0 || obtains.get(accountID).size() > 0)
			return null;

		int[] replace = new int[MajhongHelper.PLAY_CARD_COUNT];
		majhongHelper.checkTing(getCheckPai(accountID, drawCard), replace, true);
		return replace;
	}

	// 检测是否胡牌
	private HuType checkHu(String accountID, int drawCard, boolean checkPinghu)
	{
		return majhongHelper.checkHu(getCheckPai(accountID, drawCard), Math.abs(drawCard), checkPinghu);
	}

	private int[] getCheckPai(String accountID, int drawCard)
	{
		int[] checkPai = new int[CardType.Tong.Value() + MajhongHelper.ONE_TYPE_MAX];
		if(drawCard > 0)
			checkPai[drawCard] += 1;
		for(int card : playCards.get(accountID))
			checkPai[card] += 1;
		for(MJObtain obtain : obtains.get(accountID))
		{
			if(obtain.getType() == PlayType.Pong)
				checkPai[obtain.getCard()] -= 3 * 2;
			else if(obtain.getType() == PlayType.Kong ||
					obtain.getType() == PlayType.CealedKong ||
					obtain.getType() == PlayType.DotKong)
				checkPai[obtain.getCard()] -= 4 * 2;
		}
		return checkPai;
	}

	public int getPlayerID(String accountID)
	{
		if(roomInfo == null || accountID == null || accountID.equals(""))
			return -1;

		Player player = roomInfo.getPlayer(accountID);
		if(player == null)
			return -1;
		else
			return player.getID();
	}

	private void sendBattleStepSuccess(List<MBattleStep> steps)
	{
		sendBattleStepSuccess(null, false, steps);
	}

	private void sendBattleStepSuccess(MBattleStep battleStep, boolean ignoreOther,List<MBattleStep> steps)
	{
		if(battleStep == null)
			battleStep = battleSteps.get(battleSteps.size() - 1);

		steps.add(battleStep);

		//TODO: remove below old logic

		ignoreOther = ignoreOther ||
				(!battleStep.isSelf() &&
						((battleStep.getPlayType() & PlayType.CanDotKong) > 0 ||
								(battleStep.getPlayType() & PlayType.CanHu) > 0 ||
								(battleStep.getPlayType() & PlayType.CanPong) > 0));

		battleStep.setIgnoreOther(ignoreOther);

		String accountID = null;
		for(int i = 0; i < MajhongHelper.ONE_CARD_MAX; i++)
		{
			if(accountID == null)
				accountID = battleStep.getOwernAccId();
			else
				accountID = getNextPlayerAcc(accountID);

			if(ignoreOther && accountID.equals(battleStep.getOwernAccId()) == false)
				continue;

			Player player = roomInfo.getPlayer(accountID);
			if(player.getPlayState().ordinal() < Player.PlayState.Ready.ordinal())
				continue;

			BWBattleStepRES res = new BWBattleStepRES();
			res.setResult(GlobalConstants.BW_Battle_Step_SUCCESS);
			res.setAccountId(accountID);

			// 设置庄主id
			BattleData builder = new BattleData();
			builder.setBankerId(getPlayerID(bankerId));
			// 设置局数
			builder.setBattleTime(roomInfo.getBattleTime());
			builder.setBattleCount(roomInfo.getBattleCount());

			BattleStep stepBuilder = battleStep.toBattleStep();
			stepBuilder.setRemainCardCount(cardPools.size());
			if(battleStep.isTargetValid())
				stepBuilder.setTargetId(getPlayerID(battleStep.getTargetAccId()));

			if(battleStep.getPlayType() == PlayType.Deal)
			{
				stepBuilder.setOwnerId(getPlayerID(accountID));
				String nextAccountId = accountID;
				for(int j = 0; j < MajhongHelper.ONE_CARD_MAX; j++)
				{
					int playerId = getPlayerID(nextAccountId);
					BattleDealCard dealBuild = new BattleDealCard();
					dealBuild.setPlayerId(playerId);
					if(playerId == player.getID())
					{
						// 设置手上的牌
						for(int card : playCards.get(nextAccountId))
							dealBuild.addCards(card);
					}

					// 设置打出去的牌
					for(int card : disposeCards.get(nextAccountId))
						dealBuild.addDisposeCards(card);
					builder.addBattleDealCards(dealBuild);

					// 设置玩家状态信息
					if(obtains.get(nextAccountId).size() > 0)
					{
						BattleBalance blanceBuild = new BattleBalance();
						blanceBuild.setPlayerId(playerId);
						for(MJObtain obtain : obtains.get(nextAccountId))
						{
							CardBalance cardBlance = new CardBalance();
							cardBlance.setType(obtain.getType());
							cardBlance.setCard(obtain.getCard());
							blanceBuild.addBalances(cardBlance);
						}

						builder.addBattleBalances(blanceBuild);
					}

					nextAccountId = getNextPlayerAcc(nextAccountId);
				}

			}
			else
				stepBuilder.setOwnerId(getPlayerID(battleStep.getOwernAccId()));

			builder.addBattleSteps(stepBuilder);
			res.setBattleData(builder);
//			BattleService battleService = new BattleService(null);
//			battleService.sendStepToGateway(roomInfo.getRoomID(), player.getAccountID(), res);
		}
	}

	private BWBattleStepRES sendBattleStep2Target(String accountID, MBattleStep battleStep)
	{
		BWBattleStepRES res = new BWBattleStepRES();
		res.setResult(GlobalConstants.BW_Battle_Step_SUCCESS);
		res.setAccountId(accountID);

		// 设置庄主id
		BattleData builder = new BattleData();
		builder.setBankerId(getPlayerID(bankerId));
		// 设置局数
		builder.setBattleTime(roomInfo.getBattleTime());
		builder.setBattleCount(roomInfo.getBattleCount());

		BattleStep stepBuilder = battleStep.toBattleStep();
		stepBuilder.setOwnerId(getPlayerID(battleStep.getOwernAccId()));
		stepBuilder.setTargetId(getPlayerID(battleStep.getTargetAccId()));
		stepBuilder.setRemainCardCount(cardPools.size());
		if(battleStep.isTargetValid())
			stepBuilder.setTargetId(getPlayerID(battleStep.getTargetAccId()));

		builder.addBattleSteps(stepBuilder);
		res.setBattleData(builder);
/*		BattleService battleService = new BattleService();
		battleService.sendStepToGateway(roomInfo.getRoomID(), accountID, res);*/
		return res;
	}

	private BWBattleStepRES sendBattleStepFailed(String accountID)
	{
		//TODO: add to return type
		this.failedAccountID.add(accountID);
		BWBattleStepRES res = new BWBattleStepRES();
		res.setResult(GlobalConstants.BW_Battle_Step_InValid_Operator);
		res.setAccountId(accountID);
//		BattleService battleService = new BattleService();
//		battleService.sendStepToGateway(roomInfo.getRoomID(), accountID, res);
		return res;
	}

	public ArrayList<MBattleStep> getBattleSteps() {
		return battleSteps;
	}

	public Set<String> getFailedAccountID() {
		return failedAccountID;
	}

	//测试用的
	public void rebuildCards(){

	}
}
