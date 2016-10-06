package com.kodgames.battle.common;

public enum VoteResultType {

    // 利用构造函数传参
    AGREE(0), REFUSE(1), START(2);

    // 定义私有变量
    private int voteResult;

    // 构造函数，枚举类型只能为私有
    private VoteResultType(int voteResult) {

        this.voteResult = voteResult;
    }

    public static VoteResultType valueOf(int value) {    //    手写的从int到enum的转换函数
        switch (value) {
        case 0:
            return AGREE;
        case 1:
            return REFUSE;
        case 2:
        	return START;
        default:
            return null;
        }
    }

    public int value() {
        return this.voteResult;
    }
    @Override
    public String toString() {

        return String.valueOf(this.voteResult);
    }

}