package com.rafo.chess.logic.gymj;

import com.rafo.chess.model.*;

import java.util.List;

import static com.rafo.chess.model.AbstractDealerFactory.dealerInstance;

/**
 * Created by 亚文 on 2016/9/8.
 */
public class GYMJGame extends AbstractGame {


    public GYMJGame(RafoRoom room , List<GamePlayer> plyers ){
       gdealer = AbstractDealerFactory.create(room,plyers); //牌局处理器
    }


   public GYMJGame(int roomId  ){
        gdealer = dealerInstance(roomId); //根据房间号获取牌局处理器
    }

























}
