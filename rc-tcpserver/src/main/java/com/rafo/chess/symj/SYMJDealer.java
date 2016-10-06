package com.rafo.chess.symj;

import static com.rafo.chess.model.GameRdb.putDealer;
import static com.rafo.chess.model.GameRdb.putGamePlayers;
import static com.rafo.chess.model.GameRdb.putRoom;
import static com.rafo.chess.model.GameRdb.putRoomPlayerIds;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.rafo.chess.common.Avatas;
import com.rafo.chess.model.AbstractDealer;
import com.rafo.chess.model.GameAction;
import com.rafo.chess.model.GamePlayer;
import com.rafo.chess.model.GameRdb;
import com.rafo.chess.model.GameRoomSettings;
import com.rafo.chess.model.RafoRoom;

/**
 * @author KZC 沈阳麻将牌局处理
 */
public class SYMJDealer extends AbstractDealer {

	/** 开牌是否能开旋风杠 */
	private boolean initShowXuanFengGangTurn;

	/** 开门吃牌设定*() */
	private boolean openEatTurn;

	/** 开门胡开关 */
	private boolean openHuTurn;

	/** 旋风杠开关 */
	private Map<Integer, Boolean> playerXfgTurn;

	/** 吃牌开关 */
	private Map<Integer, Boolean> playerEatTurn;

	public SYMJDealer() {

	}

	public SYMJDealer(GameRoomSettings settings, List<GamePlayer> plyers) {
		init(new RafoRoom(settings), plyers);
	}

	public void init(RafoRoom aroom, List<GamePlayer> plyers) {
		room = aroom; // 设定房间
		players = plyers; // 匹配玩家

		makeDealerId();// 牌局ID生成
		bankerSettings();// 定庄
		shuffle();// 选择牌池并洗牌
		currTurnPlayer = 0; // 当前轮到谁
		lastTurnPlayer = 0; // 上次轮到谁
		lastOutCard = 0; // 上次打出的牌
		currOutCard = 0; // 当前打出的牌

		putRoom(room);
		putGamePlayers(players);
		putRoomPlayerIds(room.getRoomId(), players);
		putDealer(this);
	}

	public SYMJDealer(int roomId) {
		SYMJDealer dealer = GameRdb.getSYDealerByRoomId(roomId);
		if (dealer != null) {
			room = dealer.room;
			players = dealer.players;
			dealerId = dealer.dealerId;
			cardsPool = dealer.cardsPool;
			currTurnPlayer = dealer.currTurnPlayer;
			lastTurnPlayer = dealer.lastTurnPlayer;
			bankPlayer = dealer.bankPlayer;
			currOutCard = dealer.currOutCard;
			lastOutCard = dealer.lastOutCard;
			waitPlayerGameActionList = dealer.waitPlayerGameActionList;
			waitActionList = dealer.waitActionList;
		}

	}

	@Override
	// 发牌
	public List<GamePlayer> fapai() {
		return super.fapai();
	}

	@Override
	// 取牌
	public Integer[] getCards(int count) {
		return super.getCards(count);
	}

	// 胡牌处理
	@Override
	protected List<GamePlayer> inHU(int playerId, GameAction action) {
		//// TODO: 2016/9/21 算分算番，数据清理
		return players;
	}

	@Override
	// 发牌之后
	public List<GameAction> afterFaPai(Integer[] handCards, List<GameAction> openCards, Integer mCard) {
		List<GameAction> actions = new ArrayList<>();
		// 开牌就可以开旋风杠
		if (initShowXuanFengGangTurn) {
			GameAction actionXfg = SYMJUtils.judgeXuanFengGang(handCards, mCard);
			if (actionXfg != null) {
				actions.add(actionXfg);
			}
		}
		// 处理通用逻辑
		actions.addAll(super.afterFaPai(handCards, openCards, mCard));
		return actions;
	}

	// 摸牌后可进行的操作
	@Override
    protected  List<GameAction> afterMoPai(Integer[] handCards ,List<GameAction> openCards ,Integer[] mCard ,boolean reachStat ){
		return super.afterMoPai(handCards, openCards, mCard, reachStat);
	}
}
