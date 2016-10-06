package com.rafo.chess.calculate.majiang.operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kodgames.battle.entity.battle.BattleBalance;
import com.kodgames.battle.entity.battle.BattleCensus;
import com.kodgames.battle.entity.battle.CardBalance;
import com.rafo.chess.calculate.ResultFlag;
import com.rafo.chess.common.Avatas;
import com.rafo.chess.resources.DataContainer;
import com.rafo.chess.resources.impl.PluginTemplateGen;

public class PayDetailed {
	/** 第几步 */
	private int step;
	/** 插件id */
	private int pluginId;
	/** 是否有效 */
	private boolean isValid = true;
	/** 支付分数的玩家 */
	private int[] fromUids;
	/** 获得分数的玩家 */
	private int toUid;
	/** 番 */
	private int rate;
	
	private int card;

	public void execute(BattleBalance bb, BattleCensus bc, OptPlayer optPlayer) {
		PluginTemplateGen gen = (PluginTemplateGen) DataContainer.getInstance()
				.getMapDataByName("pluginTemplateGen").get("" + pluginId);
		int payNum = fromUids.length;
		List<CardBalance> list = bb.getBalances();
		// 胡牌统计数据处理
		if (gen.getPluginClass().equals("HUPlugin")) {
			// 手牌
			List<Integer> cards = new ArrayList<Integer>();
			for (Integer c : optPlayer.getGamePlayer().getHandCards()) {
				cards.add(c);
			}
			Integer[] openCards = Avatas.OpenCards2Array(optPlayer
					.getGamePlayer().getOpenCards());
			for (Integer c : openCards) {
				cards.add(c);
			}
			bb.setCards(cards);
			if (optPlayer.getUid() == toUid) {
				// 统计自摸，点和
				if (payNum == 3) {
					bc.setWinSelf(bc.getWinSelf() + 1);
				} else {
					bc.setWinOther(bc.getWinOther() + 1);
				}

				// 胡的牌行
				int add = +payNum * rate;
				optPlayer.getGamePlayer().setScore(
						optPlayer.getGamePlayer().getScore() + add);

				bb.setWinType(Integer.parseInt(gen.getConditionStr()));
				bb.setWinPoint(rate);
				bb.setWinScore(bb.getWinScore() + rate * payNum);
			} else {
				// 点炮次数
				if (payNum != 3) {
					bc.setDiscardOther(bc.getDiscardOther() + 1);
				}
				// 分数处理
				optPlayer.getGamePlayer().setScore(
						optPlayer.getGamePlayer().getScore() - rate);
				int winType = ResultFlag.ACT_WEIJIAOZUI.getFlag();
				if(optPlayer.isJiaozui())
					winType = ResultFlag.ACT_JIAOZUI.getFlag();
				bb.setWinType(winType);
				bb.setWinPoint(rate);
				bb.setWinScore(bb.getWinScore() + -rate);
			}
		}

		// 杠牌统计数据处理
		if (gen.getPluginClass().equals("GangPlugin")) {
			if (gen.getConditionStr().equals("7")) {
				bc.setCealedKong(bc.getCealedKong() + 1);
			} else if (gen.getConditionStr().equals("8")) {
				bc.setKong(bc.getKong() + 1);
			}
		}

		// 同一动作处理,加分减分,标示动作名目
		list = bb.getBalances();
		CardBalance cb = new CardBalance();
		cb.setCard(card);
		cb.setScore(rate);
		//加分的
		if (optPlayer.getUid() == toUid) {
			if (payNum == 3) {
				cb.setType(gen.getZimoFlag());
			} else {
				cb.setType(gen.getDianedFlag());
			}
			int score = optPlayer.getGamePlayer().getScore() + payNum * rate;
			optPlayer.getGamePlayer().setScore(score);
			bb.setWinScore(bb.getWinScore() + rate * payNum);
		} 
		//减分的
		else {
			cb.setScore(-rate);
			if (payNum == 3) {
				cb.setType(gen.getZimoedFlag());
			} else {
				cb.setType(gen.getDianFlag());
			}
			int score = optPlayer.getGamePlayer().getScore() - rate;
			optPlayer.getGamePlayer().setScore(score);
			bb.setWinScore(bb.getWinScore() - rate);
		}
		if(cb.getType() != 0) {
			list.add(cb);
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("step :" + step);
		sb.append(",");
		sb.append("pluginId :" + pluginId);
		sb.append(",");
		sb.append("isValid :" + isValid);
		sb.append(",");
		sb.append("toUid :" + toUid);
		sb.append(",");
		sb.append("fromUids :" + fromUids);
		sb.append(",");
		sb.append("rate :" + rate);
		sb.append(",");
		return sb.toString();
	}

	public int[] getFromUid() {
		return fromUids;
	}

	public void setFromUid(int[] fromUids) {
		this.fromUids = fromUids;
	}

	public int getToUid() {
		return toUid;
	}

	public void setToUid(int toUid) {
		this.toUid = toUid;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public int getPluginId() {
		return pluginId;
	}

	public void setPluginId(int pluginId) {
		this.pluginId = pluginId;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public int getCard() {
		return card;
	}

	public void setCard(int card) {
		this.card = card;
	}

}