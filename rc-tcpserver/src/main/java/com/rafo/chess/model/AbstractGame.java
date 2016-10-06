package com.rafo.chess.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by 亚文 on 2016/9/8.
 */
public abstract class AbstractGame {


    private static final Logger logger = LoggerFactory.getLogger(AbstractGame.class);
    protected AbstractDealer gdealer; //牌局


    /**
     * 发牌
     */
    public List<GamePlayer>  dealFaPai(){
      return   gdealer.fapai();
    };

    /**
     *  出牌处理
     */
    public List<GamePlayer>  dealOutPai( int playerId ,GameAction action ){
        return gdealer.dapai( playerId,action );
    }



    /**
     * 进牌处理
     */
    public List<GamePlayer>  dealInPai( int playerId ,GameAction action   ){
        return gdealer.inpai(playerId,action );
    }

    /**
     * 过牌处理
     */
    public List<GamePlayer>  dealGuoPai( int playerId ){
        return gdealer.inGUO(playerId );
    }


    /**
     * 听牌处理
     */
    public List<GamePlayer>  dealTingPai( int playerId ,GameAction action   ){
        return gdealer.inTING(playerId ,action);
    }




    /**
     * 当前轮到谁
     */
    public int getCurrTurnPlayer(){
        return gdealer.getCurrTurnPlayer();
    }



    /**
     * 当前玩家打出的牌
     */
    public int getCurrOutCard(){
        return gdealer.getCurrOutCard();
    }


    /**
     * 当前翻牌雞
     */
    public int getFanPaiJi(){
        return gdealer.getFPJCard();
    }


    /**
     * 當前牌局是否結束
     */
    public boolean isGameOver(){
        return gdealer.isOver();
    }


    /**
     * 清理牌局
     * */
    public void  clearDealer(){
        gdealer.clearDealer();
    }

    /*
    * 当前玩家詳細列表
    * */
    public List<GamePlayer> getPlayers(){
        return gdealer.getPlayers();
    }



    /**
     *  牌局結束的戰鬥結果
     */
    public List<ScoreDetail> battleResult(){



        return null;
    }



    /**
     * 解散房間
     * */
    public void releaseRoom(){



    }




    public AbstractDealer getGdealer(){
        return gdealer;
    }












}
