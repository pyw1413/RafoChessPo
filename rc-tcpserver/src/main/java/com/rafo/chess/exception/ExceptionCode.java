package com.rafo.chess.exception;

/**
 * Created by 亚文 on 2016/9/17.
 */
public enum ExceptionCode {
    //无效操作
    INVALID_OPT("E0000"),
    NO_CARD("E0001")

    ;





    private  String code;

    private ExceptionCode(String code ){
        this.code = code;
    }







}
