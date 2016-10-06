package com.rafo.chess.filter;

import com.kodgames.battle.common.constants.GlobalConstants;
import com.rafo.chess.utils.CmdsUtils;
import com.smartfoxserver.v2.controllers.filter.SysControllerFilter;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.filter.FilterAction;

/**
 * Created by Administrator on 2016/9/22.
 */
public class SFSJoinRoomFilter extends SysControllerFilter {
    @Override
    public FilterAction handleClientRequest(User user, ISFSObject params) throws SFSException {

        String roomId = params.getUtfString("n");
        Room room = null ;
        if (roomId != null && roomId.trim().length() > 0) {
            room = user.getZone().getRoomByName(roomId);
        }

        if(room == null){ //TODO: 是否有异常情况下，用户已经在房间了，又重复加入房间？
            SFSObject data = new SFSObject();
            data.putInt("result", GlobalConstants.ROOM_ENTER_FAILED_NUMBER_ERROR);

            user.getZone().getExtension().send("joinroom" , data, user);
            return FilterAction.HALT;
        }

        if(room.getSize().getUserCount()==4){
            SFSObject data = new SFSObject();
            data.putInt("result", GlobalConstants.ROOM_ENTER_FAILED_ROOM_FULL);
            user.getZone().getExtension().send(CmdsUtils.CMD_JOINROOM, data, user);
        }

        return FilterAction.CONTINUE;
    }
}
