package com.kodgames.battle.service.room;

import com.kodgames.battle.service.server.KodgamesExtension;
import com.rafo.chess.exception.PersistException;
import com.rafo.chess.service.LoginService;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.RoomVariable;
import com.smartfoxserver.v2.entities.variables.SFSUserVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.exceptions.SFSVariableException;
import com.smartfoxserver.v2.extensions.SFSExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class VoteDestroyTask implements Runnable {

    private SFSExtension extension;
    private final Logger logger = LoggerFactory.getLogger("voteTask");

    public VoteDestroyTask(SFSExtension zoneExt) {
        this.extension = zoneExt;
    }

    @Override
    public void run() {
        try {
            logger.debug("------task MarqueeSend, run.");
            int userCount = extension.getParentZone().getUserManager().getUserCount();

            Properties props = extension.getConfigProperties();
            int serverId = Integer.parseInt(props.getProperty("server.id").trim());
            LoginService.updateSeverUserCount(serverId,userCount);

            List<Room> rooms = extension.getParentZone().getRoomList();
            List<Room> roomList = new ArrayList<Room>();
            for(Room r:rooms){
                if(r.isGame()){
                    roomList.add(r);
                }
            }

            for(Room r:roomList){
                SFSExtension roomExt =  (SFSExtension)r.getExtension();
                checkVoteTime(r,roomExt);
            }

        }catch (Exception e){
            logger.debug("task error!!!!"+ e.getMessage());
            System.out.println(e.getMessage());
        }

    }


    private void checkVoteTime(Room room,SFSExtension roomExt) throws PersistException, SFSVariableException {
        RoomVariable isVote = room.getVariable("isVote");
        if(isVote != null){
            if(isVote.getBoolValue()){
                int voteTime = room.getVariable("voteTime").getIntValue();
                int now = (new Long(System.currentTimeMillis()).intValue())/1000;
                if((now - voteTime)>= 60){
                    for(User u:room.getUserList()){
                        UserVariable uv = u.getVariable("voteResult");
                        if(uv == null){
                            UserVariable voteResult = new SFSUserVariable("voteResult", room.getName()+":"+0);
                            voteResult.setHidden(true);
                            roomExt.getApi().setUserVariables(u, Arrays.asList(voteResult));
                        }
                    }

                    if(RoomHelper.couldDestroy(room)){
                        ISFSObject resp = new SFSObject();
                        RoomHelper.destroy(room,roomExt,resp);
                    }

                }

            }
        }

    }

}
