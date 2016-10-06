package com.rafo.chess.calculate.majiang;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rafo.chess.calculate.CalculateManager;
import com.rafo.chess.calculate.majiang.card.MJCard;
import com.rafo.chess.calculate.majiang.operation.IOperation;
import com.rafo.chess.calculate.majiang.operation.OperationEnum;
import com.rafo.chess.calculate.majiang.operation.OptPlayer;
import com.rafo.chess.calculate.majiang.operation.PayDetailed;
import com.rafo.chess.calculate.majiang.operation.config.IPluginConfig;
import com.rafo.chess.calculate.majiang.operation.config.PluginConfig;
import com.rafo.chess.calculate.majiang.operation.impl.FinalOperation;
import com.rafo.chess.calculate.majiang.operation.impl.JiaozuiOperation;
import com.rafo.chess.calculate.majiang.operation.impl.LiujuOperation;
import com.rafo.chess.calculate.majiang.operation.plugin.IOptPlugin;
import com.rafo.chess.calculate.majiang.operation.plugin.OptPluginContainer;
import com.rafo.chess.calculate.majiang.operation.plugin.OptPluginFactory;
import com.rafo.chess.model.AbstractDealer;
import com.rafo.chess.model.Action;
import com.rafo.chess.model.GamePlayer;
import com.rafo.chess.model.GameRdb;
import com.rafo.chess.model.RecorderInfo;
import com.rafo.chess.resources.DataContainer;
import com.rafo.chess.resources.impl.PluginTemplateGen;

public class OperationManager<O extends IOperation> {
	private AbstractDealer dealer;
	
	//进行核算的玩家
	private ArrayList<OptPlayer> optPlayers = new ArrayList<OptPlayer>();
	//房间内动作配置的插件
	private HashMap<Class<O>, ArrayList<IOptPlugin<O>>> optPluginMap = new HashMap<Class<O>, ArrayList<IOptPlugin<O>>>();
	//一局所有的操作集合
	private ArrayList<IOperation> operationlist = new ArrayList<IOperation>();
	//一局所有的支付流水
	private ArrayList<PayDetailed> PayDetailedList = new ArrayList<PayDetailed>();
	
	
	public OperationManager(AbstractDealer dealer){
		ArrayList<IPluginConfig> list = new ArrayList<IPluginConfig>();
		//初始化插件
		Map<String, Object> genList =  DataContainer.getInstance().getMapDataByName("pluginTemplateGen");
		for(Object obj : genList.values()){
			PluginTemplateGen gen = (PluginTemplateGen) obj;
			IPluginConfig pluginConfig = new PluginConfig(gen);
			list.add(pluginConfig);
		}
        this.dealer = dealer;
        List<GamePlayer> players = dealer.getPlayers();
        for(GamePlayer p : players){
        	OptPlayer optPlayer = new OptPlayer();
        	optPlayer.setGamePlayer(p);
        	optPlayers.add(optPlayer);
        }
        //玩法
		int playerType = dealer.getRoom().getPlayType();
		for(IPluginConfig pluginConf : list){
			//初始化插件，将插件与动作绑定
			if(pluginConf.getPlayerType()==playerType){
				for(OperationEnum optEnum : OperationEnum.values()){
//					if(optEnum.getFlag()==pluginConf.getOperationFlag()){
					if(pluginConf.inTheOperationFlag(optEnum.getFlag())){
						ArrayList<IOptPlugin<O>> tempList = optPluginMap.get(optEnum.getOperationClass());
						if(tempList==null){
							tempList = new ArrayList<IOptPlugin<O>>();
							optPluginMap.put((Class<O>) optEnum.getOperationClass(), tempList);
						}
						IOptPlugin<O> plugin = OptPluginFactory.createOptPlugin(pluginConf); 
						tempList.add(plugin);
					}
				}
			}
		}
	}
	
	
	public OperationManager(int dealerId){
		ArrayList<IPluginConfig> list = new ArrayList<IPluginConfig>();
		//初始化插件
		Map<String, Object> genList =  DataContainer.getInstance().getMapDataByName("pluginTemplateGen");
		for(Object obj : genList.values()){
			PluginTemplateGen gen = (PluginTemplateGen) obj;
			IPluginConfig pluginConfig = new PluginConfig(gen);
			list.add(pluginConfig);
		}
        dealer = GameRdb.getDealerById( dealerId );
        List<GamePlayer> players = dealer.getPlayers();
        for(GamePlayer p : players){
        	OptPlayer optPlayer = new OptPlayer();
        	optPlayer.setGamePlayer(p);
        	optPlayers.add(optPlayer);
        }
        //玩法
		int playerType = dealer.getRoom().getPlayType();
		for(IPluginConfig pluginConf : list){
			//初始化插件，将插件与动作绑定
			if(pluginConf.getPlayerType()==playerType){
				for(OperationEnum optEnum : OperationEnum.values()){
//					if(optEnum.getFlag()==pluginConf.getOperationFlag()){
					if(pluginConf.inTheOperationFlag(optEnum.getFlag())){
						ArrayList<IOptPlugin<O>> tempList = optPluginMap.get(optEnum.getOperationClass());
						if(tempList==null){
							tempList = new ArrayList<IOptPlugin<O>>();
							optPluginMap.put((Class<O>) optEnum.getOperationClass(), tempList);
						}
						IOptPlugin<O> plugin = OptPluginFactory.createOptPlugin(pluginConf); 
						tempList.add(plugin);
					}
				}
			}
		}
	}
	

	public ArrayList<OptPlayer> getOptPlayers() {
		return optPlayers;
	}

	public ArrayList<IOptPlugin<O>> getOperationPluginList(IOperation opt){
		return optPluginMap.get(opt.getClass());
	}
	
	//记录玩家的操作
	public void addOperation(IOperation opt){
		operationlist.add(opt);
	}
	//存放所有支付的流水
	public void addPayDetailed(PayDetailed pd){
		PayDetailedList.add(pd);
	}
	
	public AbstractDealer getDealer(){
		return this.dealer;
	}
	/***
	 * 取出指定操作
	 * @param step
	 * @return
	 */
	public IOperation getOperationByStep(int step){
		return operationlist.get(step);
	}
	/***
	 * 取出指定操作
	 * @param step
	 * @return
	 */
	public ArrayList<IOperation> getOperationList(){
		return operationlist;
	}
	/***
	 * 取出指定步产生的结果
	 * @param step
	 * @return
	 */
	public ArrayList<PayDetailed> getPayDetailedByStep(int step){
		ArrayList<PayDetailed> list = new ArrayList<PayDetailed>();
		for(PayDetailed pd : PayDetailedList){
			if(pd.getStep()==step){
				list.add(pd);
			}
		}
		return list;
	}
	/***
	 * 所有流水集合
	 * @return
	 */
	public ArrayList<PayDetailed> getPayDetailedList(){
		return PayDetailedList;
	}
	
	/**结算一局的得失,重写*/
	public void calculate(){
		Integer step =0;
		for(OptPlayer plsyer : optPlayers){
			//计算叫嘴
			JiaozuiOperation jiaozuiOpt = new JiaozuiOperation(step++,null);
			this.addOperation(jiaozuiOpt);
			jiaozuiOpt.setOperator(plsyer);
			jiaozuiOpt.setOperationManager(this);
			RecorderInfo r = new RecorderInfo();
			r.setUid(plsyer.getUid());
			jiaozuiOpt.setRecorderInfo(r);
//			jiaozuiOpt.process();
//			//计算普通鸡
//			FinalOperation finalOpt = new FinalOperation(step++,null);
//			this.addOperation(finalOpt);
//			finalOpt.setOperator(plsyer);
//			finalOpt.setOperationManager(this);
////			finalOpt.process();
//			//计算刘局
//			LiujuOperation liuju = new LiujuOperation(step++,null);
//			this.addOperation(liuju);
//			liuju.setOperator(plsyer);
//			liuju.setOperationManager(this);
////			liuju.process();
		}
		//取得这一局的操作流
		List<RecorderInfo> list = GameRdb.getRecord(dealer.getDealerId());
		
		for(RecorderInfo info:list){
			OperationEnum optEnum = getByAction(info.getPlayType());
			if(optEnum==null)
				continue;
			MJCard card = new MJCard();
			card.setOpt(optEnum);
			card.setNumber(info.getCard());
			try {
				Constructor con =optEnum.getOperationClass().getConstructor(new Class[]{Integer.class ,MJCard.class});
				IOperation opt = (IOperation) con.newInstance(new Object[]{step++,card});
				int uid = info.getUid();
				int fromId = info.getCardFrom();
				for(OptPlayer plsyer : optPlayers){
					if(uid==plsyer.getUid())
						opt.setOperator(plsyer);
					if(fromId==plsyer.getUid())
						opt.setFireder(plsyer);
				}
				
				opt.setRecorderInfo(info);//需要修改
				opt.setOperationManager(this);
				this.addOperation(opt);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		
		for(OptPlayer plsyer : optPlayers){
			//计算普通鸡,翻牌机
			FinalOperation finalOpt = new FinalOperation(step++,null);
			this.addOperation(finalOpt);
			finalOpt.setOperator(plsyer);
			finalOpt.setOperationManager(this);
			RecorderInfo r = new RecorderInfo();
			r.setUid(plsyer.getUid());
			finalOpt.setRecorderInfo(r);
//			finalOpt.process();
			//计算刘局
			LiujuOperation liuju = new LiujuOperation(step++,null);
			this.addOperation(liuju);
			liuju.setOperator(plsyer);
			liuju.setOperationManager(this);
			RecorderInfo liujuOpt = new RecorderInfo();
			r.setUid(plsyer.getUid());
			finalOpt.setRecorderInfo(liujuOpt);
//			liuju.process();
		}
		
		//核算操作步骤
		for(IOperation opt : operationlist){
			opt.process();
		}
	}
	
	/***
	 * 比对与上游的动作类型
	 * @param index
	 * @return
	 */
	public static OperationEnum getByAction(int index){
		Action act = Action.getByCode(index);
		if(act==Action.TING){
			return OperationEnum.TING;
		}
		if(act==Action.MO){
			return OperationEnum.ZHUA;
		}
		if(act==Action.DA){
			return OperationEnum.DACARD;
		}
		if(act==Action.CHI){
			return OperationEnum.CHICARD;
		}
		if(act==Action.PENG){
			return OperationEnum.PENG;
		}
		if(act==Action.GANG||act==Action.ANGANG||act==Action.DIANGANG){
			return OperationEnum.GANG;
		}
		if(act==Action.PING_HU||act==Action.DADUI_HU||act==Action.QIDUI_HU
				||act==Action.LONGQIDUI_HU||act==Action.QINGYISE_HU
			||act==Action.QINGQIDUI_HU||act==Action.QINGDADUI_HU||act==Action.QINGLONGDUI_HU)
			return OperationEnum.HU;
		if(act==Action.GUO){
			return OperationEnum.GUO;
		}
		return null;
	}
	
}
