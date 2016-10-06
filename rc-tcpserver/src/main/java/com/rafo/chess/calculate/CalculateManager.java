package com.rafo.chess.calculate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kodgames.battle.entity.battle.BattleBalance;
import com.kodgames.battle.entity.battle.BattleCensus;
import com.kodgames.battle.entity.battle.BattleData;
import com.rafo.chess.calculate.majiang.OperationManager;
import com.rafo.chess.calculate.majiang.operation.OptPlayer;
import com.rafo.chess.calculate.majiang.operation.PayDetailed;
import com.rafo.chess.common.Avatas;
import com.rafo.chess.model.AbstractDealer;
import com.rafo.chess.model.ActionType;
import com.rafo.chess.model.GameAction;
import com.rafo.chess.model.GamePlayer;
import com.rafo.chess.model.GameRdb;

public class CalculateManager {
	// public static HashMap<Integer, BattleData> battleDataMap = new
	// HashMap<Integer, BattleData>();

	// public static void addBattleData(int roomId, BattleData bData) {
	// battleDataMap.put(roomId, bData);
	// }

	//胡牌的判断需要叫一张牌，所以这里处理将手牌的最后一样作为叫牌处理
	public static GameAction judgeHU(Integer[] handCards, Integer[] openCards,
			ActionType actionType) {
		GameAction ga = null;
		Integer[] handCardsTemp = new Integer[handCards.length - 1];
		for (int i = 0; i < handCardsTemp.length; i++) {
			handCardsTemp[i] = handCards[i];
		}
		ga = Avatas.judgeHU(handCardsTemp, openCards,
				handCards[handCards.length - 1], actionType);
		return ga;
	}

	public static void calculateByRoomId(int dealerId, BattleData bData) {
		AbstractDealer dealer = GameRdb.getDealerById(dealerId);
		if (dealer == null)
			return;
		OperationManager operationManager = new OperationManager(dealer);
		operationManager.calculate();
		// //
		// BattleData bData = battleDataMap.get(dealer.getRoom().getRoomId());
		// if(bData ==null){
		// battleDataMap.put(dealer.getRoom().getRoomId(), bData);
		// }
		List<BattleBalance> bbList = bData.getBattleBalances();
		if (bbList.size() == 0) {
			for (GamePlayer gp : dealer.getPlayers()) {

				BattleBalance bb = new BattleBalance();
				bb.setPlayerId(gp.getUid());
				bbList.add(bb);
				List<Integer> cards = new ArrayList<Integer>();
				Integer[] open = Avatas.OpenCards2Array(gp.getOpenCards());
				for (int i = 0; i < open.length; i++) {
					cards.add(open[i]);
				}
				for (int i = 0; i < gp.getHandCards().length; i++) {
					cards.add(gp.getHandCards()[i]);
				}
				bb.setCards(cards);
			}

			List<BattleCensus> battleCensuss = bData.getBattleCensuss();
			if (battleCensuss.size() == 0) {
				for (GamePlayer gp : dealer.getPlayers()) {
					BattleCensus bc = new BattleCensus();
					bc.setPlayerId(gp.getUid());
					battleCensuss.add(bc);
				}
			}
		}
		ArrayList<OptPlayer> optList = operationManager.getOptPlayers();
		// 记录旧的分数
		HashMap<Integer, Integer> scoreTempMap = new HashMap<Integer, Integer>();
		for (OptPlayer optPlayer : optList) {
			scoreTempMap.put(optPlayer.getUid(), optPlayer.getGamePlayer()
					.getScore());
		}
		ArrayList<PayDetailed> pdList = operationManager.getPayDetailedList();
		for (PayDetailed pd : pdList) {
			for (OptPlayer optPlayer : optList) {
				for (BattleBalance bb : bData.getBattleBalances()) {
					if (bb.getPlayerId() != optPlayer.getUid())
						continue;
					for (BattleCensus bc : bData.getBattleCensuss()) {
						if (bc.getPlayerId() != bb.getPlayerId())
							continue;
						pd.execute(bb, bc, optPlayer);
					}
				}
			}
		}
		// 比较分数
		for (BattleCensus bc : bData.getBattleCensuss()) {
			int oldScore = scoreTempMap.get(bc.getPlayerId());
			for (OptPlayer optPlayer : optList) {
				if (bc.getPlayerId() != optPlayer.getUid())
					continue;
				bc.setPoint(optPlayer.getGamePlayer().getScore() - oldScore);
			}
		}
	}

}
