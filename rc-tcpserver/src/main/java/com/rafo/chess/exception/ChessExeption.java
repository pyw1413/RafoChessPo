package com.rafo.chess.exception;

import static com.rafo.chess.exception.ExceptionCode.*;

/**
 * Created by 亚文 on 2016/9/17.
 */
public class ChessExeption  extends  RuntimeException{
    private ExceptionCode code;

    public ChessExeption( String desc  ){
        super(desc);
        this.code = INVALID_OPT;
    }

    public ChessExeption( String desc ,ExceptionCode code ){
        super( desc );
        this.code = code;
    }







}
