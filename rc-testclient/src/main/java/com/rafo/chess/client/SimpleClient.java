package com.rafo.chess.client;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;
import sfs2x.client.SmartFox;
import sfs2x.client.core.BaseEvent;
import sfs2x.client.core.IEventListener;
import sfs2x.client.core.SFSEvent;
import sfs2x.client.entities.User;
import sfs2x.client.entities.variables.UserVariable;
import sfs2x.client.requests.ExtensionRequest;
import sfs2x.client.requests.JoinRoomRequest;
import sfs2x.client.requests.LoginRequest;
import sfs2x.client.requests.PublicMessageRequest;
import sfs2x.client.util.ClientDisconnectionReason;

/**
 * Created by Administrator on 2016/9/7.
 */
public class SimpleClient implements IEventListener{

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
        sfs.addEventListener(SFSEvent.PUBLIC_MESSAGE, this);
        sfs.addEventListener(SFSEvent.PUBLIC_MESSAGE, this);
        sfs.addEventListener(SFSEvent.PUBLIC_MESSAGE, this);
        sfs.addEventListener(SFSEvent.PUBLIC_MESSAGE, this);
        sfs.addEventListener(SFSEvent.EXTENSION_RESPONSE ,this);

        sfs.connect("localhost",9933);
//        sfs.connect("192.168.1.19",9933);
//        sfs.connect("192.168.1.33",9933);

        System.out.println("sfs is connected : " + sfs.isConnected());

    }


    @Override
    public void dispatch(BaseEvent event) throws SFSException {
        testJoinRoom(event);
//        testRejoinRoom(event);
//        testNormalJoin(event);
    }
    public void testNormalJoin(BaseEvent event){
        if (event.getType().equalsIgnoreCase(SFSEvent.CONNECTION)) {
            System.out.println("-------connected----------");

            sfs.send(new LoginRequest("55", "", zoneName));
//            sfs.disconnect();
        }else if (event.getType().equalsIgnoreCase(SFSEvent.CONNECTION_LOST)) {
            System.out.println("-------CONNECTION_LOST----------");

            System.out.println("Ouch, connection was lost. Reason: " + (String)event.getArguments().get("reason"));

        }else if (event.getType().equalsIgnoreCase(SFSEvent.LOGIN)) {

            System.out.println("-------LOGIN----------");
            String message = "503025";
            //  sfs.send(new PublicMessageRequest(message));

//            isos.putUtfString("data",message);
//            sfs.send(new ExtensionRequest("msg",isos));
            System.out.println("-------SEND MESSAGE:"+ message + "-----------");

           ISFSObject isos = new SFSObject();
            isos = new SFSObject();
          // isos.putUtfString("data","vvvss");
           sfs.send(new ExtensionRequest("getServer",isos));

/*            isos = new SFSObject();
            isos.putUtfString("data","434xx");
            sfs.send(new ExtensionRequest("ready",isos));*/

           // sfs.send(new JoinRoomRequest("vvvss", ""));
        }else if (event.getType().equalsIgnoreCase(SFSEvent.SOCKET_ERROR)) {
            System.out.println("===========SOCKET_ERROR=========");
        }else if (event.getType().equalsIgnoreCase(SFSEvent.ROOM_JOIN)) {
            System.out.println("===========ROOM_JOIN=========");
        }else if (event.getType().equalsIgnoreCase(SFSEvent.ROOM_JOIN_ERROR)) {
            System.out.println("===========ROOM_JOIN_ERROR=========");
        }
    }

    public void testJoinRoom(BaseEvent event){
        if (event.getType().equalsIgnoreCase(SFSEvent.CONNECTION)) {
            System.out.println("-------connected----------");

            sfs.send(new LoginRequest("4", "", zoneName));
        }else if (event.getType().equalsIgnoreCase(SFSEvent.CONNECTION_LOST)) {
            System.out.println("-------CONNECTION_LOST----------  " + (String)event.getArguments().get("reason"));

        }else if (event.getType().equalsIgnoreCase(SFSEvent.LOGIN)) {

            User user = (User) event.getArguments().get("user");
            UserVariable variable = user.getVariable("sfsobj");

            System.out.println("-------LOGIN----------" + user.getVariables().size());

            ISFSObject isos = new SFSObject();
            isos = new SFSObject();
           // isos.putUtfString("data","vvvss");
           sfs.send(new ExtensionRequest("getServer",isos));

//            ISFSObject isos = new SFSObject();
//            isos.putInt("roomid", 870870 );
//            sfs.send(new ExtensionRequest("joinroom", isos));
           // sfs.send(new JoinRoomRequest("vvvss", ""));

        }else if (event.getType().equalsIgnoreCase(SFSEvent.SOCKET_ERROR)) {
            System.out.println("===========SOCKET_ERROR=========");
        }else if (event.getType().equalsIgnoreCase(SFSEvent.ROOM_JOIN)) {
            System.out.println("===========ROOM_JOIN=========");
        }else if (event.getType().equalsIgnoreCase(SFSEvent.ROOM_JOIN_ERROR)) {
            System.out.println("===========ROOM_JOIN_ERROR=========");
        }else if (event.getType().equalsIgnoreCase(SFSEvent.USER_VARIABLES_UPDATE)) {
            System.out.println("===========USER_VARIABLES_UPDATE=========");
        }else if (event.getType().equalsIgnoreCase(SFSEvent.EXTENSION_RESPONSE)) {
            String cmd = (String)event.getArguments().get("cmd");
            if (cmd.equalsIgnoreCase("getServer")) {
                ISFSObject resObj = (ISFSObject) event.getArguments().get("params");
                System.out.println("ip:"  + resObj.getUtfString("ip"));
            }

            ISFSObject isos = new SFSObject();
           sfs.send(new ExtensionRequest("SFS_EVENT_ROUND_RECORD", isos));
            if (cmd.equalsIgnoreCase("SFS_EVENT_ROUND_RECORD")) {
                ISFSObject resObj = (ISFSObject) event.getArguments().get("params");
                SFSObject sub = (SFSObject)(resObj.getSFSObject("roundData"));
                System.out.println("content:"  + sub.getInt("id"));
            }
        }



        System.out.println("##############"+event.getType());
    }

    public void testRejoinRoom(BaseEvent event){
        if (event.getType().equalsIgnoreCase(SFSEvent.CONNECTION)) {
            System.out.println("-------connected----------");

            sfs.send(new LoginRequest("tttt", "", zoneName));
            if(!rejoin) {
                sfs.disconnect();
            }
        }else if (event.getType().equalsIgnoreCase(SFSEvent.CONNECTION_LOST)) {
            System.out.println("-------CONNECTION_LOST----------  " + (String)event.getArguments().get("reason"));
            String reason = (String)event.getArguments().get("reason");

            if (reason.equals(ClientDisconnectionReason.MANUAL)){
                rejoin = true;
                sfs.connect("localhost",9933); //模拟再次登录

            }
        }

    }


    public static void main(String[] args){
        SimpleClient client = new SimpleClient();
        client.startUp();

        //System.exit(1);
    }
}
