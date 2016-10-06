package com.rafo.chess.common.exception;

/**
 * Created by Administrator on 2016/9/22.
 */
public class DBInitException extends Exception {

    public DBInitException() {
        super();
    }

    public DBInitException(String message) {
        super(message);
    }

    public DBInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBInitException(Throwable cause) {
        super(cause);
    }

    protected DBInitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
