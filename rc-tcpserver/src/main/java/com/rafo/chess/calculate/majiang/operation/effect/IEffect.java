package com.rafo.chess.calculate.majiang.operation.effect;

/***
 * 插件效果
 * @author Administrator
 *
 */
public interface IEffect {
	/**所有的人都支付分数*/
	public final static int ALL_PAY_PLAYERS = 0;
	/**点牌的人都支付分数*/
	public final static int DA_PAY_PLAYER = 1;
	/**自摸的是所有人，否则是点牌的人支付分数*/
	public final static int AUTO_PAY_PLAYERS = 2;
	
	
	/**开始执行效果*/
	public void fireEffect();
}
