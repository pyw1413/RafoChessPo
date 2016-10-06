package com.rafo.chess.model;

import java.util.List;

/**
 * Created by 亚文 on 2016/9/13.
 */
public class PlayerGameAction {

    private  int puid;
    private List<GameAction> gameActions;

    public int getPuid() {
        return puid;
    }

    public void setPuid(int puid) {
        this.puid = puid;
    }

    public List<GameAction> getGameActions() {
        return gameActions;
    }

    public void setGameActions(List<GameAction> gameActions) {
        this.gameActions = gameActions;
    }
}
