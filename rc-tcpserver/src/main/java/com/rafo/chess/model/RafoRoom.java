package com.rafo.chess.model;

import com.kodgames.battle.common.Player;
import com.kodgames.battle.common.VoteResultType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 亚文 on 2016/9/7.
 */
public class RafoRoom {
    private int roomId;
    private int password;
    private int level;
    private int gameType; // 房间的类型，0或者1
    private int playType;
    private String playTypeExt1;
    private String playTypeExt2;
    private int roomSize;
    private int totalRounds;
    private int currRounds;
    private int roomStatus;
    private int bankPlayer;
    private int ownerId;
    private long createTime;
    private boolean hasSubCard;
    private boolean isInBattle; // 是否处于打牌中
    private int status;
    private ConcurrentHashMap<String, VoteResultType> voteDestroyResult = new ConcurrentHashMap<String, VoteResultType>();
    private List<GamePlayer> players = new ArrayList<>();
    private ConcurrentHashMap<Integer, GamePlayer> playerMapping = new ConcurrentHashMap<Integer, GamePlayer>();

    public RafoRoom(){ }



    public RafoRoom(int password,  int gameType, int playType, String playTypeExt1, String playTypeExt2, int roomSize, int totalRounds, int currRounds, int roomStatus ,int bankPlayer){
        this.roomId = password;
        this.password = password;
        this.level = 1;
        this.gameType = gameType;
        this.playType = playType;
        this.playTypeExt1 = playTypeExt1;
        this.playTypeExt2 = playTypeExt2;
        this.roomSize = roomSize;
        this.totalRounds = totalRounds;
        this.currRounds = currRounds;
        this.roomStatus = roomStatus;
        this.bankPlayer = bankPlayer;
    }



    public RafoRoom(GameRoomSettings settings){
        this.roomId = settings.getPassword();
        this.password = settings.getPassword();
        this.level = 1;
        this.gameType = settings.getGameType();
        this.playType = settings.getPlayType();
        this.playTypeExt1 = settings.getPlayTypeExt1();
        this.playTypeExt2 = settings.getPlayTypeExt2();
        this.roomSize = settings.getRoomSize();
        this.totalRounds = settings.getTotalRounds();
        this.currRounds = settings.getCurrRounds();
        this.roomStatus = 0;
        this.ownerId = settings.getOwnerId();
    }

    public int getBankPlayer() {
        return bankPlayer;
    }

    public void setBankPlayer(int bankPlayer) {
        this.bankPlayer = bankPlayer;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public int getPlayType() {
        return playType;
    }

    public void setPlayType(int playType) {
        this.playType = playType;
    }

    public String getPlayTypeExt1() {
        return playTypeExt1;
    }

    public void setPlayTypeExt1(String playTypeExt1) {
        this.playTypeExt1 = playTypeExt1;
    }

    public String getPlayTypeExt2() {
        return playTypeExt2;
    }

    public void setPlayTypeExt2(String  playTypeExt2) {
        this.playTypeExt2 = playTypeExt2;
    }

    public int getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(int roomSize) {
        this.roomSize = roomSize;
    }

    public int getCurrRounds() {
        return currRounds;
    }

    public void setCurrRounds(int currRounds) {
        this.currRounds = currRounds;
    }

    public int getTotalRounds() {
        return totalRounds;
    }

    public void setTotalRounds(int totalRounds) {
        this.totalRounds = totalRounds;
    }

    public int getRoomStatus() {
        return roomStatus;
    }



    public void setRoomStatus(int roomStatus) {

        this.roomStatus = roomStatus;
    }

    public int getOwnerId() {
        return ownerId;
    }


    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    /*
 * 是否第一次进行投票，如果是，表示玩家是发起者
 */
    public boolean isFirstApplyDestroy()
    {
        return voteDestroyResult.size() == 0 ? true : false;
    }

    public boolean hasVoteApply()
    {
        return voteDestroyResult.size() == 0 ? false : true;
    }

    /*
     * 如果里面存储的结果达到了房间人数，并且没有拒绝，则可以解散
     */
    public boolean isCouldDestroy()
    {
        if(voteDestroyResult.size() != roomSize)
            return false;
        for (Map.Entry<String, VoteResultType> player2Vote : voteDestroyResult.entrySet())
        {
            if(player2Vote.getValue() == VoteResultType.REFUSE)
                return false;
        }

        return true;
    }

    public void addVoteResult(String accountID, VoteResultType voteResult)
    {
        voteDestroyResult.put(accountID, voteResult);
    }

    public boolean hasRefuse()
    {
        for (Map.Entry<String, VoteResultType> player2Vote : voteDestroyResult.entrySet())
        {
            if(player2Vote.getValue() == VoteResultType.REFUSE)
                return true;
        }
        return false;
    }

    /*
     * 是否已经投过票
     */
    public boolean hasVoted(String accountID)
    {
        if(voteDestroyResult.containsKey(accountID))
            return true;
        return false;
    }

    public void canceDestroy()
    {
        voteDestroyResult.clear();
    }

    public final Map<String, VoteResultType> getVoteRecord()
    {
        return voteDestroyResult;
    }



    public boolean isPostive() // 是否扣过房卡
    {
        return this.hasSubCard;
    }

    public long getCreateTime()
    {
        return createTime;
    }


    public boolean isInBattle()
    {
        return isInBattle;
    }

    public void setInBattle(boolean isInBattle)
    {
        this.isInBattle = isInBattle;
    }

    public boolean isHasSubCard()
    {
        return hasSubCard;
    }

    public void setHasSubCard(boolean hasSubCard)
    {
        this.hasSubCard = hasSubCard;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<GamePlayer> getPlayers() {
        return players;
    }

    public void addPlayer(GamePlayer player){
        this.players.add(player);
        this.playerMapping.put(player.getUid(), player);
    }

    public void removePlayer(int playerId){
        GamePlayer player = this.playerMapping.get(playerId);
        this.players.remove(player);
        this.playerMapping.remove(playerId);
    }

    public void setPlayers(List<GamePlayer> players) {
        this.players = players;
    }

    public ConcurrentHashMap<Integer, GamePlayer> getPlayerMapping() {
        return playerMapping;
    }

    public void setPlayerMapping(ConcurrentHashMap<Integer, GamePlayer> playerMapping) {
        this.playerMapping = playerMapping;
    }
}
