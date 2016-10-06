package com.rafo.chess.model;


import com.rafo.chess.common.Avatas;
import com.rafo.chess.exception.ChessExeption;
import com.rafo.chess.utils.GtypeUtils;
import org.apache.commons.lang.math.RandomUtils;

import java.util.ArrayList;
import java.util.List;

import static com.rafo.chess.common.Avatas.*;
import static com.rafo.chess.model.GameRdb.*;
import static com.rafo.chess.utils.CardsUtils.*;
import static com.rafo.chess.utils.ConfigUtils.*;

/**
 * 牌局处理者类
 * Created by 亚文 on 2016/9/8.
 */
public abstract class AbstractDealer {
    protected RafoRoom room;
    protected List<GamePlayer> players;

    protected int dealerId;
    //花色: 万 条 筒 1 2 3   东南西北 10 11 12 13 中发白 14 15 16
    protected Integer[] cardsPool;
    protected int currTurnPlayer;  //当前轮到谁(playerId)
    protected int lastTurnPlayer;  //上次是谁(playerId
    protected int bankPlayer;  //庄家(playerId)
    protected int CFJPlayer; //冲锋鸡玩家(playerId)
    protected int ZRJPlayer;//责任鸡玩家(playerId)
    protected int FPJCard;//翻牌鸡
    protected boolean isOver;//牌局是否結束
    protected int currOutCard ;  //当前打出的牌
    protected int lastOutCard ;  //上次打出的牌
    protected List<PlayerGameAction> waitPlayerGameActionList = new ArrayList<>();; //待定生效玩家及操作
    protected List<PlayerGameAction> waitActionList = new ArrayList<>(); //待定生效操作



    //牌局ID生成
    protected void  makeDealerId(){
        dealerId = room.getRoomId()* 100 +  room.getCurrRounds(); //牌局ID = 房间号+当前局数
    }


    /**
     *  ①定庄
     */
    protected void  bankerSettings(){
        RafoRoom newRoom =  GameRdb.getRoom( room.getRoomId() );
        if( newRoom == null ){ //创建房间者为庄家
            room.setBankPlayer( players.get(0).getUid() );
        }
        if(room.getBankPlayer() == 0 ){
            bankPlayer = players.get(0).getUid();
            room.setBankPlayer(bankPlayer);
        }else {
            bankPlayer = room.getBankPlayer();
        }
    }



    /**
     *  ②洗牌
     */
    public void shuffle(){
        cardsPool = ChooseCardsPool( GtypeUtils.PlayType.getByOrdinal( room.getPlayType() ));
       /* for(int i = 0; i< SHUFFLE_TIMES; i++ ){
            for(int j =0; j< cardsPool.length;j++){
                int index =  RandomUtils.nextInt(cardsPool.length - 1 );
                int tmp = cardsPool[j] ;
                cardsPool[j] = cardsPool[index];
                cardsPool[index] = tmp;
            }
        }*/
    }




    /**
     *  ③发牌
     */
    public List<GamePlayer> fapai(){

        makeDealerId();//牌局ID生成
        bankerSettings();//定庄
        shuffle();
        for( GamePlayer p :  players ){
            if( p.isBanker() ){
                Integer[] cards_13 = getCards( MJ_HANDCARDS ); //先发13张
                //// TODO: 2016/9/21 庄家听牌判断待策划案确定修改
                Integer[] mCard =  getCards(1); //再摸一张
                p.setmCard(mCard[0] );
                bankPlayer = p.getUid();
                currTurnPlayer = p.getUid();
                //发牌后庄家可进行 天胡、软听、杠牌操作
                p.setCurrentActions(  afterFaPai( p.getHandCards(),p.getOpenCards(),mCard[0] ) );
                p.setHandCards(  ArrayAdd( cards_13,mCard ) );
            }else { //闲家可以进行硬报
                p.setHandCards( getCards( MJ_HANDCARDS  ) );
                p.setCurrentActions(  afterFaPai(p.getHandCards(),p.getOpenCards()) );
            }
        }

        putGamePlayers( players );
        putDealer( this );
        return players;
    }

    //庄家发牌后可进行的操作[天胡、软报、暗杠]
    protected  List<GameAction> afterFaPai(Integer[] handCards , List<GameAction> openCards , Integer mCard  ){
        GameAction actionh  = Avatas.judgeCanHU( handCards,OpenCards2Array( openCards ),mCard,ActionType.TIANHU ); //庄家天胡判断
        GameAction actiont =  Avatas.judgeTING(handCards,OpenCards2Array( openCards ),mCard,ActionType.RUANTING);//软报
        GameAction actiong  = Avatas.judgeMOGANG( handCards,openCards,mCard ); //发牌后杠牌判断
        List<GameAction> list =  new ArrayList<>();

        if( actionh != null ){
            list.add( actionh );
        }
        if(actiont != null ) {
            list.add(actiont);
        }
        if(actiong != null ) {
            list.add(actiong);
        }
        return list;
    }

    //闲家发牌后可进行的操作 [硬听]
    protected  List<GameAction> afterFaPai(Integer[] handCards ,List<GameAction> openCards ){
        GameAction actiont  = Avatas.judgeYTING( handCards ,OpenCards2Array( openCards ),ActionType.YINGTING); //判断硬报
        List<GameAction> list =  new ArrayList<>();
        if(actiont != null ){
            list.add(actiont);
        }
        return list;
    }

    //摸牌后可进行的操作
    protected  List<GameAction> afterMoPai(Integer[] handCards ,List<GameAction> openCards ,Integer[] mCard ,boolean reachStat ){
        GameAction actionh  = Avatas.judgeCanHU( handCards,  OpenCards2Array( openCards ) ,mCard[0], ActionType.ZIMO ); //胡牌判断[自摸胡]
        GameAction actiont = Avatas.judgeTING(handCards,OpenCards2Array( openCards ),mCard[0],ActionType.RUANTING );//软听
        GameAction actiong  = Avatas.judgeMOGANG( handCards,openCards,mCard[0]); //摸牌后杠牌判断[暗杠、点杠]
        List<GameAction> list =  new ArrayList<>();
        list.add(moPaiAction( mCard ));

        if( actionh != null ){
            list.add( actionh );
        }
        if( actiont != null && lastOutCard == 0 ){
            list.add( actiont );
        }
        if(actiong != null &&  !reachStat ) { //非听牌状态
            list.add(actiong);
        }
            return list;
    }

    //杠牌后可进行的操作
    protected  List<GameAction> afterGangPai(Integer[] handCards ,List<GameAction> openCards ,Integer[] mCard ,boolean reachStat ){
        GameAction actionh  = Avatas.judgeCanHU( handCards,  OpenCards2Array( openCards ) ,mCard[0], ActionType.ZIMO ); //胡牌判断[杠上胡]
        GameAction actiong  = Avatas.judgeMOGANG( handCards,openCards,mCard[0]); //摸牌后杠牌判断[暗杠、点杠]
        List<GameAction> list =  new ArrayList<>();
        list.add(moPaiAction( mCard ));

        if( actionh != null ){
            list.add( actionh );
        }else if(actiong != null &&  !reachStat ) { //非听牌状态
            list.add(actiong);
        }
        return list;
    }


    //打牌后可进行的操作
    protected List<GameAction> afterDaPai(Integer[] handCards ,Integer[] openCards, Integer ocard  ){
        GameAction actionh = judgeCanHU(handCards,openCards,ocard,null);//计算出可胡牌
        GameAction actiong = judgeGANG(handCards,ocard);
        GameAction actionp = judgePENG(handCards,ocard);
        List<GameAction> list = new ArrayList<>();

        if( actionp != null ){
            list.add( actionp );
        }
        if( actiong != null ){
            list.add( actiong );
        }
        if( actionh != null ){
            list.add( actionh );
        }

        return  list;
    }



    //能否吃牌
    protected void canChiPai(int currSeatNo, Integer oCard ){
        if(CHIPAI_FLG){ //开关
            for( GamePlayer p: players ){
                if( p.getSeatNo() ==  (currSeatNo - 1)%4  ){ //下一家
                    GameAction actionc = judgeCHI(p.getHandCards(),oCard);
                    List<GameAction> list = p.getCurrentActions();
                    if( actionc != null ){
                        list.add(actionc );
                        p.setCurrentActions( list );
                        waitActionList.add(toPlayGameAction(p.getUid(),list) );//加入待判定生效操作
                    }
                }
            }
        }
    }



    //从牌池取若干牌
    protected Integer[] getCards( int count ){
        Integer[] gcards= new Integer[count];
        for(int i = 0 ; i< count ; i++ ){
            gcards[i] = cardsPool[i];
        }
        Integer[] rcards= new Integer[cardsPool.length - count];
        for(int j = count; j< cardsPool.length;j++){
            rcards[j - count] = cardsPool[j];
        }
        cardsPool = rcards;
        return  gcards;
    }



    /**
     *  ④打牌
     */
    public List<GamePlayer> dapai( int playerId ,GameAction action ){

        int seatNo = 0; //当前玩家的座位号
        Integer ocard = action.getCards().get(0)[0];//打出的牌

        int otherOptCount = 0;
        //1)其他玩家可进行的操作计算
        for(GamePlayer p: players){
            if(p.getUid() != playerId &&  p.isReachStatus() == false ){
                List<GameAction> list =  afterDaPai( p.getHandCards(),OpenCards2Array( p.getOpenCards()), ocard );
                if( list.size() > 0 ){
                    otherOptCount ++ ;
                    p.setCurrentActions( list );
                    waitActionList.add(  toPlayGameAction(p.getUid(),list) ) ;//加入待判定生效操作
                }else {
                    p.setCurrentActions( null );
                }
            }
        }

        //2)玩家手牌移出及牌局状态更新操作
        for( GamePlayer p : players ){
            if( p.getUid() == playerId){
                seatNo = p.getSeatNo();
                if(action.getAction() == Action.DA){
                    if(action.getCards() == null ) {
                        throw new ChessExeption("invalid operation!");
                    }
                    if(  Contains( p.getHandCards(),ocard ) != true ) {
                        throw new ChessExeption("player doesn't have this card!");
                    }
                    p.setHandCards( ArrayRemove( p.getHandCards(), ocard ) );
                    p.setOutCards(  ArrayAdd(p.getOutCards(),ocard ) );

                    updateCurrOutCard( ocard );//当前打出的牌

                    p.setLastGameAction(action);
                    p.setCurrentActions( null);
                    //冲锋鸡
                    if(CHONGFENGJI_FLG){
                        if( ocard == 201 && CFJPlayer == 0 ){
                            if( otherOptCount == 0  ){ //没有碰、杠
                                CFJPlayer = p.getUid();
                                GameAction cfjAction = action;
                                cfjAction.setAction(Action.CHONGFENGJI);
                                action.setActionType(ActionType.CHONGFENGJI);
                                List<GameAction> list = new ArrayList<>();
                                list.add(cfjAction );
                                p.setCurrentActions(list);
                            }else{ //有碰、杠 待定冲锋鸡
                                GameAction cfjAction = action;
                                cfjAction.setAction(Action.CHONGFENGJI);
                                List<GameAction> list = new ArrayList<>();
                                list.add(cfjAction );
                                waitPlayerGameActionList.add(toPlayGameAction(p.getUid(),list) );
                            }
                        }
                    }

                    //记录打牌动作
                    putRecord( dealerId,Action2Record(p.getUid(),action,0,null));
                }
            }
        }


        //下家能否吃牌计算
        canChiPai( seatNo,  ocard );


        //都没有吃碰胡可进行,轮到下家自动摸牌打牌
        if( otherOptCount == 0 ){
            for( GamePlayer p :  players ){
                //下一家
                if( p.getSeatNo() ==  nextPlayerSeatNo(seatNo)  ){
                  if( isFlowOver() == false )  { //黄庄判断
                      //先自动摸牌
                      Integer[] card = getCards(1);
                      p.setmCard(card[0] );
                      updateCurrTurnPlayer( p.getUid() );//更新当前轮到谁
                      //记录摸牌动作
                      GameAction maction = Avatas.moPaiAction(card);
                      putRecord( dealerId,Action2Record(p.getUid(),maction,0,null));
                      //摸的什么牌,摸牌后可进行的动作列表
                      List<GameAction> listm  =  afterMoPai( p.getHandCards(), p.getOpenCards(), card ,p.isReachStatus() );
                      p.setCurrentActions( listm);
                      p.setHandCards(  ArrayAdd( p.getHandCards(), card[0]) );
                  }else{ //黄庄
                      GameAction maction = Avatas.huangZhuangAction();
                      List<GameAction> listm = new ArrayList<>();
                      listm.add(maction );
                      p.setCurrentActions(listm );
                      isOver = true;
                      putRecord( dealerId,Action2Record(p.getUid(),maction,0,null));
                  }

                }
            }
        }

        putGamePlayers( players );
        putDealer( this );
        return  players;
    }



    /**
     *  ⑤进牌
     */
    public List<GamePlayer> inpai(  int playerId ,GameAction action ){
        //操作生效判断
        List<GamePlayer> list = null;
        Action act = action.getAction();
        switch (act){
            case TING:
                list =   inTING(playerId,action);
                break;
            case GUO:
                list =   inGUO(playerId);
                break;
            case CAN_HU:
                list =   inHU(playerId,action);
                break;
            case GANG:
                list =   inGANG(playerId,action);
                break;
            case ANGANG:
                list =   inANGANG(playerId,action);
                break;
            case DIANGANG:
                list =   inDIANGANG(playerId,action);
                break;
            case PENG:
                list =   inPENG(playerId,action);
                break;
            case CHI:
                list =   inCHI(playerId,action);
                break;
        }

        return  list;
    }



    //听牌处理
    protected List<GamePlayer> inTING (  int playerId ,GameAction action ){
        for(GamePlayer p  : players){
            if(p.getUid() == playerId && action.getAction() == Action.TING){
                p.setReachStatus(true);//状态变为听牌
            }
        }
        putGamePlayers( players );
        return  players;
    }


    //过牌处理
    protected List<GamePlayer> inGUO (  int playerId ){
        //从待生效队列中移除
        for(  int i = 0 ; i < waitActionList.size(); i ++  ){
            if( waitActionList.get(i).getPuid() == playerId ){
                waitActionList.remove(i);
            }
        }

        //记录过牌动作
        GameAction gaction = guoPaiAction();
        putRecord( dealerId,Action2Record(playerId,gaction,currTurnPlayer,daPaiAction(currOutCard)));

        int seatNo = 0 ;
        for(GamePlayer p: players){
            if(p.getUid() == currTurnPlayer ){
                seatNo = p.getSeatNo();
            }
        }

        //漏杠牌记录
        for(GamePlayer p: players ) {
            if (p.getUid() == playerId) {
                List<GameAction> galist = p.getCurrentActions();
                int cout = 0;
                for (GameAction ga : galist) {
                    if (ga.getAction() == Action.GANG || ga.getAction() == Action.ANGANG || ga.getAction() == Action.DIANGANG) {
                        cout++;
                    }
                }
                if (cout > 0) {
                    List<Integer> lostgangpai = p.getLostGangCards();
                    lostgangpai.add(currOutCard);
                    p.setLostGangCards(lostgangpai);
                }

                p.setCurrentActions(new ArrayList<GameAction>());//当前可进行操作清空
                p.setLastGameAction( guoPaiAction() );
            }
        }

        //剩余待生效队列中找出优先级最高的操作做生效处理
        if( waitPlayerGameActionList.size() > 0  ){
            Action act = null ;
            for(int j = 0; j < waitPlayerGameActionList.size() ; j ++ ){
                 act = waitPlayerGameActionList.get(j).getGameActions().get(0).getAction();
                if( act.code() < waitPlayerGameActionList.get(j).getGameActions().get(0).getAction().code() ){
                    act =  waitPlayerGameActionList.get(j).getGameActions().get(0).getAction();
                }
            }

            GameAction action = null;
            switch (act){
                case CHI:
                    break;
                case PENG:
                     action = pengPaiAction(currOutCard);
                     inPENG( playerId,  action );
                    break;
                case GANG:
                     action = gangPaiAction(Action.GANG,null,currOutCard);
                     inGANG( playerId,  action );
                    break;
                case DIANGANG:
                    action = gangPaiAction(Action.DIANGANG,null,currOutCard);
                    inDIANGANG( playerId,  action );
                    break;
                case CHONGFENGJI:
                    action = daPaiAction(currOutCard);
                    inCFJ( playerId,  action );
                    break;
            }
        }else { //下家摸牌
            for( GamePlayer p :  players ){
                //下一家
                if( p.getSeatNo() ==  nextPlayerSeatNo(seatNo)  ){
                    if( isFlowOver() == false )  { //黄庄判断
                        //先自动摸牌
                        Integer[] card = getCards(1);
                        p.setmCard(card[0] );

                        updateCurrTurnPlayer( p.getUid() );//更新当前轮到谁
                        //记录摸牌动作
                        GameAction maction = Avatas.moPaiAction(card);
                        putRecord( dealerId,Action2Record(p.getUid(),maction,0,null));
                        //摸的什么牌,摸牌后可进行的动作列表
                        List<GameAction> listm  =  afterMoPai( p.getHandCards(), p.getOpenCards(), card ,p.isReachStatus() );
                        p.setCurrentActions( listm);
                        p.setHandCards(  ArrayAdd( p.getHandCards(), card[0]) );
                    }else{ //黄庄
                        GameAction maction = Avatas.huangZhuangAction();
                        List<GameAction> listm = new ArrayList<>();
                        listm.add(maction );
                        p.setCurrentActions(listm );
                        isOver = true;
                        putRecord( dealerId,Action2Record(p.getUid(),maction,0,null));
                    }
                }
            }

        }

        putGamePlayers( players );
        putDealer( this );
        return  players;
    }

    //胡牌处理
    protected List<GamePlayer> inHU (  int playerId ,GameAction action ){
        //// TODO: 2016/9/21 一炮多响， 庄家计算，数据清理
        for( GamePlayer p : players ){
            if ( p.getUid() == playerId ) {
               if( isZiMo(p.getHandCards() ) ){//自摸
                   GameAction actionh  = judgeHU(ArrayRemove(p.getHandCards(),p.getmCard() ),OpenCards2Array( p.getOpenCards()),p.getmCard(),ActionType.ZIMO );
                   if(p.getOutCards() == null ){//天胡
                         actionh  = judgeHU(ArrayRemove(p.getHandCards(),p.getmCard() ),OpenCards2Array( p.getOpenCards()),p.getmCard(),ActionType.TIANHU);
                   }else if ( p.getLastGameAction().getAction() == Action.GANG || p.getLastGameAction().getAction() == Action.ANGANG || p.getLastGameAction().getAction() == Action.DIANGANG ){
                         actionh  = judgeHU(ArrayRemove(p.getHandCards(),p.getmCard() ),OpenCards2Array( p.getOpenCards()),p.getmCard(),ActionType.GANGSHANGHU );
                   }
                   p.setLastGameAction( actionh );
                   p.setCurrentActions(new ArrayList<GameAction>());
                   //記錄胡牌動作
                   putRecord( dealerId,Action2Record(p.getUid(), huPaiAction(actionh.getAction(),actionh.getActionType(),p.getmCard()) ,0,null ));
               }else{
                   //热炮
                   boolean lastOpIsGang = false; //当前玩家上次操作是杠
                   for( GamePlayer gp : players ){
                       if(gp.getUid() == currTurnPlayer ){
                           if( gp.getLastGameAction().getAction() == Action.GANG || gp.getLastGameAction().getAction() == Action.ANGANG || gp.getLastGameAction().getAction() == Action.DIANGANG ){
                               lastOpIsGang = true;
                               gp.setOutCards( ArrayRemove( gp.getOutCards(),currOutCard) );
                           }
                       }
                   }

                   for( GamePlayer mp : players ){ //一炮多响
                       if(mp.getUid() != currTurnPlayer ){
                           GameAction actionhl  = judgeHU(mp.getHandCards(),OpenCards2Array( mp.getOpenCards()),currOutCard,null );
                           if( actionhl != null ){
                               mp.setHandCards( ArrayAdd( mp.getHandCards(),currOutCard));//手牌移动
                               if( lastOpIsGang == true ){ //热炮
                                   actionhl.setActionType( ActionType.REPAO);
                               }
                               mp.setLastGameAction( actionhl);
                               mp.setCurrentActions(new ArrayList<GameAction>());
                               //記錄胡牌動作
                               putRecord( dealerId,Action2Record(mp.getUid(), huPaiAction(actionhl.getAction(),actionhl.getActionType(),currOutCard),currTurnPlayer,daPaiAction(currOutCard)  ));
                           }
                       }
                   }


               }
            }
        }

        //翻牌雞
        setFPJCard( getCards(1)[0] );
        isOver = true;//牌局結束

        //待判定生效项清空
        waitActionList = new ArrayList<>();
        waitPlayerGameActionList = new ArrayList<>();

        putGamePlayers( players );
        putDealer( this );

        return  players;
    }

    //清理牌局 庄家计算
    public void clearDealer (){

        //庄家计算
       GamePlayer p = getGamePlayer(room.getBankPlayer());
       int nextBank = getNextBankPlayer();
        //房间
        room.setBankPlayer(nextBank);
        room.setCurrRounds( room.getCurrRounds() + 1 );
        putRoom( room );

        //玩家
        for(GamePlayer gplayer: players){
            gplayer.setBanker(  false ) ;
            if(gplayer.getUid() == nextBank ){
                gplayer.setBanker(  true ) ;
            }
            gplayer.setHandCards( null );
            gplayer.setOpenCards( new ArrayList<GameAction>() );
            gplayer.setOutCards( null );
            gplayer.setInCards( null );
            gplayer.setmCard( 0 );
            gplayer.setLastGameAction( null);
            gplayer.setCurrentActions(  new ArrayList<GameAction>() );
            gplayer.setReachStatus( false );
            gplayer.setFirstMoPai( false );
            gplayer.setLostGangCards( new ArrayList<Integer>() );
        }
        putGamePlayers(players);

        //清理牌局数据
        delDealer(dealerId);
        delRecorder(dealerId);
        dealerId=0;
        currTurnPlayer = 0;  //当前轮到谁
        lastTurnPlayer = 0;  //上次轮到谁
        lastOutCard = 0; //上次打出的牌
        currOutCard = 0; //当前打出的牌
        CFJPlayer = 0; //冲锋鸡
        ZRJPlayer = 0;//责任鸡
        FPJCard = 0; //翻牌鸡
        isOver = false;//牌局開始
        waitPlayerGameActionList = new ArrayList<>();; //待定生效玩家及操作
        waitActionList = new ArrayList<>(); //待定生效操作


    }

    protected int getNextBankPlayer(){
        int couthu = 0;
        int nextPlayerId = 0;

        for(GamePlayer p : players){
            if(p.getLastGameAction() != null && p.getLastGameAction().getAction().code() >= 21 ){
                couthu ++ ;
                nextPlayerId= p.getUid();
            }
        }
        //胡牌者接庄
        if( couthu == 1 ){
            return  nextPlayerId;
        }
        //一炮双响
        if(couthu == 2 ){
            int seatNo = getGamePlayer(room.getBankPlayer()).getSeatNo();//庄家座位
            while( true ){
                seatNo= nextPlayerSeatNo(seatNo);
                for(GamePlayer p: players){
                    if(p.getSeatNo() == seatNo && p.getLastGameAction().getAction().code() >= 21 ){
                        return p.getUid();
                    }
                }
            }
        }

        //一炮多响
        if(couthu > 2 ){
            return currTurnPlayer;
        }

        if( couthu == 0 ){
            return  room.getBankPlayer();
        }

        return nextPlayerId;
    }



    //点杠处理
    protected List<GamePlayer> inDIANGANG (  int playerId ,GameAction action ){
        action = getGameActionFromWaitActions(playerId ,  action );
        Integer oCard = action.getCards().get(0)[0];
        //// TODO: 2016/9/26 抢杠胡
        for(GamePlayer p  : players){
            if(p.getUid() == playerId){
                p.setHandCards( ArrayRemove(p.getHandCards(),oCard ));//手牌移出1张
                List<GameAction> list =   p.getOpenCards();
                for(GameAction ac : list ){
                    if(ac.getAction() == Action.PENG &&  ac.getCards().get(0)[0] == oCard ){
                        list.remove( ac );
                    }
                }
                ActionType type = null;
               if( p.getLostGangCards().contains(currOutCard) ) {
                   type =  ActionType.LOSTGANG;
               }else {
                   type =  null;
               }

                GameAction gact = gangPaiAction(Action.DIANGANG, type, oCard);
                list.add(gact);

                p.setOpenCards(list );//明牌中放入4张
                p.setLastGameAction( gact );
                //记录动作
                putRecord( dealerId,Action2Record(p.getUid(),gact ,0, null  ));

                //自动摸一张牌
                Integer[] card = getCards(1);
                p.setHandCards(  ArrayAdd( p.getHandCards(), card[0]) );
                p.setmCard(card[0] );

                updateCurrTurnPlayer( p.getUid() );//更新当前轮到谁
                //记录摸牌动作
                GameAction maction = Avatas.moPaiAction(card);
                putRecord( dealerId,Action2Record(p.getUid(),maction ,0 , null ));
                //摸的什么牌,摸牌后可进行的动作列表
                List<GameAction> listm  =  afterGangPai( p.getHandCards(), p.getOpenCards(), card ,p.isReachStatus() );
                p.setCurrentActions( listm);
            }
        }
        //待判定生效项清空
        waitActionList = new ArrayList<>();
        waitPlayerGameActionList = new ArrayList<>();

        putGamePlayers( players );
        putDealer( this );
        return  players;
    }


    //暗杠处理
    protected List<GamePlayer> inANGANG (  int playerId ,GameAction action ){
        Integer oCard = action.getCards().get(0)[0];
            for(GamePlayer p  : players){
                if(p.getUid() == playerId){
                    p.setHandCards( ArrayRemoveAll(p.getHandCards(),oCard ));//手牌移出4张
                    List<GameAction> list =   p.getOpenCards();
                    ActionType type = null;
                    if( p.getLostGangCards().contains(currOutCard) ) {
                        type =  ActionType.LOSTGANG;
                    }else {
                        type =  null;
                    }
                    GameAction agact = gangPaiAction(Action.ANGANG,type,oCard);
                    list.add( agact );
                    p.setOpenCards(list );//明牌中放入4张
                    p.setLastGameAction( agact );
                    //记录动作
                    putRecord( dealerId,Action2Record(p.getUid(),gangPaiAction(Action.ANGANG,type,oCard),0,null  ));
                    //自动摸一张牌
                    Integer[] card = getCards(1);
                    p.setHandCards(  ArrayAdd( p.getHandCards(), card[0]) );
                    p.setmCard(card[0] );

                    updateCurrTurnPlayer( p.getUid() );//更新当前轮到谁
                    //记录摸牌动作
                    GameAction maction = Avatas.moPaiAction(card);
                    putRecord( dealerId,Action2Record(p.getUid(),maction , 0 , null ));
                    //摸的什么牌,摸牌后可进行的动作列表
                    List<GameAction> listm  =  afterGangPai( p.getHandCards(), p.getOpenCards(), card ,p.isReachStatus() );
                    p.setCurrentActions( listm);
                }
            }

        //待判定生效项清空
        waitActionList = new ArrayList<>();
        waitPlayerGameActionList = new ArrayList<>();

        putGamePlayers( players );
        putDealer( this );
        return  players;
    }



    //杠牌处理
    protected List<GamePlayer> inGANG (  int playerId  ,GameAction action ){
        action = getGameActionFromWaitActions(playerId ,  action );
        Integer oCard = action.getCards().get(0)[0];
        if( inDeed(action) ){
            for(GamePlayer p  : players){
                if(p.getUid() == playerId){
                        p.setHandCards( ArrayRemoveAll(p.getHandCards(),oCard ));//手牌移出3张
                        List<GameAction> list =   p.getOpenCards();
                        ActionType type = null;
                        if( p.getLostGangCards().contains(currOutCard) ) {
                            type =  ActionType.LOSTGANG;
                        }else {
                            type =  null;
                        }

                        GameAction gact = gangPaiAction(Action.GANG,type,oCard);
                        list.add(gact );
                        p.setOpenCards(list );//明牌中放入4张
                        p.setLastGameAction( gact );

                        GameAction zrjAction = null ; //额外增加一个责任鸡行为
                        //责任鸡
                        if(CHONGFENGJI_FLG){
                            if( oCard == 201 && CFJPlayer == 0  ){
                                CFJPlayer = lastTurnPlayer;
                                ZRJPlayer = p.getUid();
                                zrjAction = gact;
                                zrjAction.setAction(Action.ZERENJI);
                                gact.setActionType(ActionType.ZERENJI);
                            }
                        }

                        //记录杠牌动作
                        putRecord( dealerId,Action2Record(p.getUid(),gact ,lastTurnPlayer, daPaiAction(currOutCard)  ));

                        //自动摸一张牌
                        Integer[] card = getCards(1);
                        p.setHandCards(  ArrayAdd( p.getHandCards(), card[0]) );
                        p.setmCard(card[0] );

                        updateCurrTurnPlayer( p.getUid() );//更新当前轮到谁
                        //记录摸牌动作
                        GameAction maction = Avatas.moPaiAction(card);
                        putRecord( dealerId,Action2Record(p.getUid(),maction , 0 , null ));
                        //摸的什么牌,摸牌后可进行的动作列表
                        List<GameAction> listm  =  afterGangPai( p.getHandCards(), p.getOpenCards(), card ,p.isReachStatus() );
                        if(zrjAction != null ){
                            listm.add(zrjAction );
                        }
                        p.setCurrentActions( listm);

                }else if(p.getUid() == currTurnPlayer){//打出该张牌的玩家
                    p.setOutCards(ArrayRemove(p.getOutCards(),oCard ) );//移除该张牌
                }
            }

            //待判定生效项清空
            waitActionList = new ArrayList<>();
            waitPlayerGameActionList = new ArrayList<>();
        }else {//未生效存入待判定操作用户列表
            for(GamePlayer p  : players){
                if(p.getUid() == playerId){
                    List<GameAction> list = new ArrayList<>();
                    list.add(gangPaiAction(Action.GANG,null,oCard));
                    waitPlayerGameActionList.add(toPlayGameAction(p.getUid(),list));
                }
            }
        }

        putGamePlayers( players );
        putDealer( this );
        return  players;
    }


    //進牌.碰牌處理
    protected  List<GamePlayer> inPENG( int playerId  ,GameAction action ){

        action = getGameActionFromWaitActions(playerId ,  action );
        Integer oCard = action.getCards().get(0)[0];
        if( inDeed(action) ){
            for(GamePlayer p  : players){
                if(p.getUid() == playerId){
                   p.setHandCards( ArrayRemove( ArrayRemove(p.getHandCards(),oCard ), oCard ) );//手牌移出2张
                   List<GameAction> list =   p.getOpenCards();
                   GameAction pact = pengPaiAction(oCard);
                   list.add( pact );
                   p.setOpenCards(list );//明牌中放入3张

                    updateCurrTurnPlayer(p.getUid());

                    p.setLastGameAction( pact );
                    p.setCurrentActions(new ArrayList<GameAction>());

                    //责任鸡
                    if(CHONGFENGJI_FLG){
                        if( oCard == 201 && CFJPlayer == 0  ){
                            CFJPlayer = lastTurnPlayer;
                            ZRJPlayer = p.getUid();
                            GameAction zrjAction = action;
                            zrjAction.setAction(Action.ZERENJI);
                            pact.setActionType(ActionType.ZERENJI);
                            List<GameAction> zrjlist = new ArrayList<>();
                            zrjlist.add(zrjAction );
                            p.setCurrentActions(zrjlist);
                        }
                    }

                    //记录动作
                    putRecord( dealerId,Action2Record(p.getUid(),pact ,lastTurnPlayer , daPaiAction(currOutCard) ));
                }else if(p.getUid() == currTurnPlayer){//打出该张牌的玩家
                   p.setOutCards(ArrayRemove(p.getOutCards(),oCard ) );//移除该张牌
                }
            }

            //待判定生效项清空
            waitActionList = new ArrayList<>();
            waitPlayerGameActionList = new ArrayList<>();
        }else {//未生效存入待判定操作用户列表
            for(GamePlayer p  : players){
                if(p.getUid() == playerId){
                    List<GameAction> list = new ArrayList<>();
                    list.add(pengPaiAction(currOutCard));
                    waitPlayerGameActionList.add(toPlayGameAction(p.getUid(),list) );
                }
            }
        }


        putGamePlayers( players );
        putDealer( this );

        return players;
    }

    //进牌.吃牌处理
    protected  List<GamePlayer> inCHI(  int playerId ,GameAction action ){
        Integer[] chiCard = action.getCards().get(0);
        if( inDeed(action) ){
            for(GamePlayer p  : players){
                if(p.getUid() == playerId){
                    p.setHandCards( ArrayRemove( p.getHandCards(), ArrayRemove(chiCard,currOutCard ) ) );//手牌移出2张
                    List<GameAction> list =   p.getOpenCards();
                    list.add( chiPaiAction(Action.CHI,chiCard) );
                    p.setOpenCards(list );//明牌中放入3张

                    updateCurrTurnPlayer(p.getUid());
                    p.setLastGameAction( chiPaiAction(Action.CHI,chiCard) );
                    p.setCurrentActions(new ArrayList<GameAction>());

                    //记录动作
                    putRecord( dealerId,Action2Record(p.getUid(),chiPaiAction(Action.CHI,chiCard),lastTurnPlayer,daPaiAction(currOutCard) ));
                }else if(p.getUid() == currTurnPlayer){//打出该张牌的玩家
                    p.setOutCards(ArrayRemove(p.getOutCards(),currOutCard ) );//移除该张牌
                }
            }

            //待判定生效项清空
            waitActionList = new ArrayList<>() ;
            waitPlayerGameActionList = new ArrayList<>();
        }else {//未生效存入待判定操作用户列表
            for(GamePlayer p  : players){
                if(p.getUid() == playerId){
                    List<GameAction> list = new ArrayList<>();
                    list.add(chiPaiAction(Action.CHI,chiCard));
                    waitPlayerGameActionList.add(toPlayGameAction(p.getUid(),list) );
                }
            }
        }


        putGamePlayers( players );
        putDealer( this );


        return  players;
    }

    //过牌.冲锋鸡处理
    protected  List<GamePlayer> inCFJ(  int playerId ,GameAction action ){
        int seatNo = 0;
        for(GamePlayer p  : players){
            if(p.getUid() == playerId){
                seatNo = p.getSeatNo();
                //冲锋鸡
                if(CHONGFENGJI_FLG){
                    CFJPlayer = p.getUid();
                    GameAction cfjAction = action;
                    cfjAction.setAction(Action.CHONGFENGJI);
                    action.setActionType(ActionType.CHONGFENGJI);
                    List<GameAction> list = new ArrayList<>();
                    list.add(cfjAction );
                    p.setCurrentActions(list);
                }
                //记录动作
                putRecord( dealerId,Action2Record(p.getUid(),action,0,null));
            }
        }

        for( GamePlayer p :  players ){
            //下一家
            if( p.getSeatNo() ==  nextPlayerSeatNo(seatNo)  ){
                if( isFlowOver() == false )  { //黄庄判断
                    //先自动摸牌
                    Integer[] card = getCards(1);
                    p.setmCard(card[0] );

                    updateCurrTurnPlayer( p.getUid() );//更新当前轮到谁
                    //记录摸牌动作
                    GameAction maction = Avatas.moPaiAction(card);
                    putRecord( dealerId,Action2Record(p.getUid(),maction,0,null));
                    //摸的什么牌,摸牌后可进行的动作列表
                    List<GameAction> listm  =  afterMoPai( p.getHandCards(), p.getOpenCards(), card ,p.isReachStatus() );
                    p.setCurrentActions( listm);
                    p.setHandCards(  ArrayAdd( p.getHandCards(), card[0]) );
                }else{ //黄庄
                    GameAction maction = Avatas.huangZhuangAction();
                    List<GameAction> listm = new ArrayList<>();
                    listm.add(maction );
                    p.setCurrentActions(listm );
                    isOver = true;
                    putRecord( dealerId,Action2Record(p.getUid(),maction,0,null));
                }
            }
        }



            //待判定生效项清空
            waitActionList = new ArrayList<>() ;
            waitPlayerGameActionList = new ArrayList<>();

        putGamePlayers( players );
        putDealer( this );

        return  players;
    }



    //生效判断（吃、碰、杠）
    protected boolean inDeed ( GameAction action ){
        if(waitActionList.size()== 1 ){ //仅有一个行为待判定直接生效
            return true;
        }else {//大于1个需要判定生效
            int maxCount =0 ;
            for(  PlayerGameAction pa:    waitActionList ){
                if( action.getAction().code() < pa.getGameActions().get(0).getAction().code() ){
                    maxCount ++ ;
                }
            }
            if(maxCount == 0 ){ //当前行为在列表中优先级最高,直接生效
                return  true;
            }else {
                return false;//否则不生效
            }
        }
    }

























    
    
    //// TODO: 2016/9/18  戰鬥結果
    public List<ScoreDetail> scoreComputer(){



        return  null;
    }


    //是否黃莊
    public boolean isFlowOver(){
        if(cardsPool.length == 0 ){
            return true;

        }else {
            return  false;
        }
    }



    public int getDealerId() {
        return dealerId;
    }

    public void setDealerId(int dealerId) {
        this.dealerId = dealerId;
    }

    public Integer[] getCardsPool() {
        return cardsPool;
    }

    public void setCardsPool(Integer[] cardsPool) {
        this.cardsPool = cardsPool;
    }

    public int getCurrTurnPlayer() {
        return currTurnPlayer;
    }

    public void setCurrTurnPlayer(int currTurnPlayer) {
        this.currTurnPlayer = currTurnPlayer;
    }

    public int getLastTurnPlayer() {
        return lastTurnPlayer;
    }

    public void setLastTurnPlayer(int lastTurnPlayer) {
        this.lastTurnPlayer = lastTurnPlayer;
    }

    public int getBankPlayer() {
        return bankPlayer;
    }

    public void setBankPlayer(int bankPlayer) {
        this.bankPlayer = bankPlayer;
    }

    public int getLastOutCard() {
        return lastOutCard;
    }

    public void setLastOutCard(int lastOutCard) {
        this.lastOutCard = lastOutCard;
    }


    public List<PlayerGameAction> getWaitPlayerGameActionList() {
        return waitPlayerGameActionList;
    }

    public void setWaitPlayerGameActionList(List<PlayerGameAction> waitPlayerGameActionList) {
        this.waitPlayerGameActionList = waitPlayerGameActionList;
    }

    public List<PlayerGameAction> getWaitActionList() {
        return waitActionList;
    }

    public void setWaitActionList(List<PlayerGameAction> waitActionList) {
        this.waitActionList = waitActionList;
    }

    public int getCurrOutCard() {
        return currOutCard;
    }

    public void setCurrOutCard(int currOutCard) {
        this.currOutCard = currOutCard;
    }


    public RafoRoom getRoom() {
        return room;
    }

    public void setRoom(RafoRoom room) {
        this.room = room;
    }

    public List<GamePlayer> getPlayers() {
        return players;
    }

    public void setPlayers(List<GamePlayer> players) {
        this.players = players;
    }


    public int getCFJPlayer() {
        return CFJPlayer;
    }

    public void setCFJPlayer(int CFJPlayer) {
        this.CFJPlayer = CFJPlayer;
    }

    public int getZRJPlayer() {
        return ZRJPlayer;
    }

    public void setZRJPlayer(int ZRJPlayer) {
        this.ZRJPlayer = ZRJPlayer;
    }

    public int getFPJCard() {
        return FPJCard;
    }

    public void setFPJCard(int FPJCard) {
        this.FPJCard = FPJCard;
    }

    public boolean isOver() {
        return isOver;
    }

    public void setOver(boolean over) {
        isOver = over;
    }



    //更新当前轮到谁
    protected void updateCurrTurnPlayer( int playerId ){
        lastTurnPlayer = currTurnPlayer; //轮到谁
        currTurnPlayer = playerId;
    }

    //更新当前打出的牌
    protected  void updateCurrOutCard( Integer ocard ){
        lastOutCard = currOutCard;
        currOutCard = ocard;//当前打出的牌
    }

    protected GameAction getGameActionFromWaitActions(int playerId , GameAction action ){
        for( PlayerGameAction pga :  waitActionList){
            if( pga.getPuid()  == playerId ){
                for( GameAction ga  : pga.getGameActions() ){
                    if(ga.getAction() ==  action.getAction() ){
                        return ga;
                    }
                }
            }
        }

        return null;
    }




























































































}
