package com.rafo.chess.handlers;

import com.rafo.chess.core.ROCHExtension;
import com.rafo.chess.model.*;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.rafo.chess.common.Atakes.*;
import static com.rafo.chess.common.Avatas.*;
import static com.rafo.chess.model.AbstractGameFactory.gameInstance;
import static com.rafo.chess.utils.CmdsUtils.CMD_MSG;
import static java.lang.Thread.sleep;

public class ChatMessageHandler extends BaseClientRequestHandler
{
	private final Logger logger = LoggerFactory.getLogger("ChatMessageHandler");
	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		ROCHExtension gameExt = (ROCHExtension) getParentExtension();

		String password = params.getUtfString("data");
		GameRoomSettings gsetting =  new GameRoomSettings();
		gsetting.setPassword(Integer.parseInt( password) );
		gsetting.setGameType(0);
		gsetting.setPlayType(0);
		gsetting.setPlayTypeExt1("1,3,4");
		gsetting.setPlayTypeExt2("2,6,8,9,12");
		gsetting.setRoomSize(4);
		gsetting.setTotalRounds(8);
		gsetting.setCurrRounds(1);

		List<GamePlayer> plist =  new ArrayList<>();
		for(int i = 0 ; i < gsetting.getRoomSize() ; i ++ ){
			GamePlayer p = new GamePlayer();
			p.setUid( 1003800 + i );
			p.setRoomId(String.valueOf( gsetting.getPassword() )  );
			p.setWuid( "wx_"+ String.valueOf(p.getUid()) );
			p.setNickName("nick_"+ String.valueOf(p.getUid()) );
			p.setPic( "pic_" + String.valueOf(p.getUid()));
			p.setGender(0);//性别
			p.setIp("10.1.2.15");
			p.setRoomCard( 20 );
			p.setSeatNo( i+1 );
			if(p.getSeatNo() == 1 ){
				p.setBanker(true);
			}else {
				p.setBanker(false);
			}

			p.setScore( 0 );
			p.setCurrentActions(null);
			p.setLastGameAction(null);
			plist.add(p);
		}

		RafoRoom room = new RafoRoom(gsetting);
	    AbstractGame agame =  AbstractGameFactory.create(room,plist);
		List<GamePlayer> players = agame.dealFaPai();
		System.out.println("\n\n开始发牌===========================");
		for(GamePlayer p : players) {
			System.out.println( "玩家: ["+ p.getUid() +"] 手牌:"+ toP(ArraySort( p.getHandCards() )));
		}
		int currTurnPlayer = agame.getCurrTurnPlayer();
		System.out.println( "\n轮到玩家: ["+ currTurnPlayer +"] 出牌" );
		String outCard = ScreenInput();

		for(GamePlayer p : players ){
			if( p.getUid() == currTurnPlayer ){
				agame.dealOutPai(p.getUid(),  daPaiAction(Integer.parseInt(outCard ))  );
				System.out.println( "玩家: ["+ p.getUid() +"] 打出 =>" +  toPai( agame.getCurrOutCard() ) );
			}
		}

		while (agame.getCurrTurnPlayer() > 0){
			AbstractGame bgame = gameInstance(Integer.parseInt( password ) );
			List<GamePlayer> bplayers = bgame.getPlayers();
			for(GamePlayer p : bplayers) {
				System.out.println( "玩家: ["+ p.getUid() +"] 手牌:"+ toP(ArraySort( p.getHandCards() )) +"       弃牌:" + toP(ArraySort( p.getOutCards() )) +
					"       明牌:"+ 	toOpen(p.getOpenCards() )  );
			}
			for(GamePlayer p : bplayers) {
				if( p.getCurrentActions().size()>0 ){
					if( p.getCurrentActions().size() == 1 && p.getCurrentActions().get(0).getAction() == Action.MO ){
						System.out.println( "玩家: ["+ p.getUid()+"] 摸牌" );
					}else{
						System.out.println( "玩家: ["+ p.getUid()+"]可进行操作:【" + canOpt(p.getCurrentActions())  + "】" );
						String inCard = ScreenInput();
						if(inCard.equals("PENG")){
							bgame.dealInPai(p.getUid(),  pengPaiAction( bgame.getCurrOutCard() )  );
							System.out.println( "玩家: ["+ p.getUid()+"]进行操作:【碰】" );
						}else if(inCard.equals("GANG")){
							bgame.dealInPai(p.getUid(),  gangPaiAction( Action.GANG,null,bgame.getCurrOutCard() )  );
							System.out.println( "玩家: ["+ p.getUid()+"]进行操作:【杠】" );
						}else if(inCard.equals("ANGANG")){
							bgame.dealInPai(p.getUid(),  gangPaiAction( Action.ANGANG,null,bgame.getCurrOutCard() )  );
							System.out.println( "玩家: ["+ p.getUid()+"]进行操作:【暗杠】" );
						}else if(inCard.equals("DIANGANG")){
							bgame.dealInPai(p.getUid(),  gangPaiAction( Action.DIANGANG,null,bgame.getCurrOutCard() )  );
							System.out.println( "玩家: ["+ p.getUid()+"]进行操作:【点杠】" );
						}else if(inCard.equals("PING_HU")){
						bgame.dealInPai(p.getUid(),  gangPaiAction( Action.PING_HU,null,bgame.getCurrOutCard() )  );
						System.out.println( "玩家: ["+ p.getUid()+"]进行操作:【平胡】" );
						}else if(inCard.equals("DADUI_HU")){
							bgame.dealInPai(p.getUid(),  gangPaiAction( Action.DADUI_HU,null,bgame.getCurrOutCard() )  );
							System.out.println( "玩家: ["+ p.getUid()+"]进行操作:【大对子胡】" );
						}else if(inCard.equals("QIDUI_HU")){
							bgame.dealInPai(p.getUid(),  gangPaiAction( Action.QIDUI_HU,null,bgame.getCurrOutCard() )  );
							System.out.println( "玩家: ["+ p.getUid()+"]进行操作:【七对胡】" );
						}else if(inCard.equals("LONGQIDUI_HU")){
							bgame.dealInPai(p.getUid(),  gangPaiAction( Action.LONGQIDUI_HU,null,bgame.getCurrOutCard() )  );
							System.out.println( "玩家: ["+ p.getUid()+"]进行操作:【龙七对胡】" );
						}else if(inCard.equals("QINGYISE_HU")){
							bgame.dealInPai(p.getUid(),  gangPaiAction( Action.QINGYISE_HU,null,bgame.getCurrOutCard() )  );
							System.out.println( "玩家: ["+ p.getUid()+"]进行操作:【清一色胡】" );
						}else if(inCard.equals("QINGQIDUI_HU")){
							bgame.dealInPai(p.getUid(),  gangPaiAction( Action.QINGQIDUI_HU,null,bgame.getCurrOutCard() )  );
							System.out.println( "玩家: ["+ p.getUid()+"]进行操作:【清七对胡】" );
						}else if(inCard.equals("QINGDADUI_HU")){
							bgame.dealInPai(p.getUid(),  gangPaiAction( Action.QINGDADUI_HU,null,bgame.getCurrOutCard() )  );
							System.out.println( "玩家: ["+ p.getUid()+"]进行操作:【清大对胡】" );
						}else if(inCard.equals("QINGLONGDUI_HU")){
							bgame.dealInPai(p.getUid(),  gangPaiAction( Action.QINGLONGDUI_HU,null,bgame.getCurrOutCard() )  );
							System.out.println( "玩家: ["+ p.getUid()+"]进行操作:【清龙对胡】" );
						}else if(inCard.equals("GUO")){
							bgame.dealGuoPai(p.getUid());
							System.out.println( "玩家: ["+ p.getUid()+"]进行操作:【过】" );
						}else if(inCard.equals("TING")){
							bgame.dealTingPai(p.getUid(),tingPaiAction());
							System.out.println( "玩家: ["+ p.getUid()+"]进行操作:【听牌】" );
						}
					}
				}
			}

			currTurnPlayer = bgame.getCurrTurnPlayer();
			System.out.println( "\n轮到玩家: ["+ currTurnPlayer +"] 出牌" );

			String ouCard = ScreenInput();

			for(GamePlayer p : bplayers ){
				if( p.getUid() == currTurnPlayer ){
					bgame.dealOutPai(p.getUid(),  daPaiAction(Integer.parseInt(ouCard ))  );
					System.out.println( "玩家: ["+ p.getUid() +"] 打出 =>" +  toPai( bgame.getCurrOutCard() ) );
				}
			}
		}













		// Send update
		ISFSObject respObj = new SFSObject();
		respObj.putUtfString("password",password);
		gameExt.send(CMD_MSG, respObj, user);

	//	getApi().sendPublicMessage(user.getLastJoinedRoom(), user, "server: ", null);
	}
}
