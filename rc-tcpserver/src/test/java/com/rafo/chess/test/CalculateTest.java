package com.rafo.chess.test;
import java.util.List;
import java.util.Map;
import org.junit.Test;

import com.kodgames.battle.entity.battle.BattleBalance;
import com.kodgames.battle.entity.battle.BattleData;
import com.kodgames.battle.entity.battle.CardBalance;
import com.rafo.chess.calculate.CalculateManager;
import com.rafo.chess.calculate.majiang.OperationManager;
import com.rafo.chess.manager.RedisManager;
import com.rafo.chess.resources.DataContainer;

/**
 * Created by Administrator on 2016/9/6.
 */
public class CalculateTest {
	@Test
	public void calculate() {
		String resourcePath = "D:/workspace4/Resource/config";
		String dataPath = System.getProperty("user.dir") + "/bin";
		DataContainer.getInstance().init(dataPath, resourcePath);
		// com.rafo.chess.resources.impl.PluginTemplateGen.java
		Map<String, Object> map = DataContainer.getInstance().getMapDataByName(
				"pluginTemplateGen");
		RedisManager.getInstance().init("192.168.1.27");
		BattleData bData = new BattleData();
		CalculateManager.calculateByRoomId(123401, bData);
		List<BattleBalance> list = bData.getBattleBalances();
		for (BattleBalance bb : list) {
			System.out.println("id" + bb.getPlayerId() + ";" + bb.getWinType()+";" + bb.getWinScore()
					+ ";" + bb.getWinPoint() + ";"+bb.getCards().toString()+";");
			for(CardBalance cb : bb.getBalances()){
				System.out.println("===cb== "+cb.getType()+";"+cb.getScore()+";"+cb.getCard()+";");
			}
		}

		// @SuppressWarnings("rawtypes")
		// OperationManager operationManager = new OperationManager(123401);
		// // dealer = GameRdb.get
		// operationManager.calculate();

	}
}
