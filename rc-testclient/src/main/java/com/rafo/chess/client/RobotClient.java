package com.rafo.chess.client;

import com.smartfoxserver.v2.exceptions.SFSException;
import sfs2x.client.SmartFox;
import sfs2x.client.core.BaseEvent;
import sfs2x.client.core.IEventListener;
import sfs2x.client.core.SFSEvent;

/**
 * Created by Administrator on 2016/9/26.
 */
public class RobotClient implements IEventListener {

    private SmartFox sfs;
    private String zoneName = "BasicExamples";
    boolean rejoin = false;

    public void startUp()
    {
        sfs = new SmartFox();

        sfs.addEventListener(SFSEvent.CONNECTION, this);
        sfs.addEventListener(SFSEvent.CONNECTION_LOST, this);
        sfs.addEventListener(SFSEvent.LOGIN, this);
        sfs.addEventListener(SFSEvent.ROOM_JOIN, this);
        sfs.addEventListener(SFSEvent.PUBLIC_MESSAGE, this);
        sfs.addEventListener(SFSEvent.USER_VARIABLES_UPDATE, this);
        sfs.addEventListener(SFSEvent.EXTENSION_RESPONSE ,this);

        sfs.connect("119.29.252.188",9933);

        System.out.println("sfs is connected : " + sfs.isConnected());
    }

    public void dispatch(BaseEvent event) throws SFSException {
        if (event.getType().equalsIgnoreCase(SFSEvent.CONNECTION)) {
            System.out.println("-------connected----------");
        }
    }
}
