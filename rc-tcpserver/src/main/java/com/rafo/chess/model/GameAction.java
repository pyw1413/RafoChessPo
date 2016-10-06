package com.rafo.chess.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/8.
 */
public class GameAction {

    //动作
    private Action action;
    //动作类型
    private ActionType actionType;

    //候选的出牌
    private List<Integer[]> cards;

    private int playerId;

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public List<Integer[]> getCards() {
        return cards;
    }

    public void setCards(List<Integer[]> cards) {
        this.cards = cards;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
