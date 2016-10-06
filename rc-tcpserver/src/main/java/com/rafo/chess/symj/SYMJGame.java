package com.rafo.chess.symj;

import com.rafo.chess.model.*;

import java.util.List;

import static com.rafo.chess.model.AbstractDealerFactory.dealerInstance;

/**
 * @author KZC
 * 沈阳麻将牌局处理
 * 
 */
public class SYMJGame extends AbstractGame {


    public SYMJGame(RafoRoom room , List<GamePlayer> plyers ){
       gdealer = AbstractDealerFactory.create(room,plyers); //牌局处理器
    }


   public SYMJGame(int roomId  ){
        gdealer = dealerInstance(roomId); //根据房间号获取牌局处理器
    }

}
