package com.kodgames.battle.entity.record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/17.
 */
public class RoundDatas {
    private List<RoundDataPROTO> roundDatas = new ArrayList<>();

    public List<RoundDataPROTO> getRoundDatas() {
        return roundDatas;
    }

    public void setRoundDatas(List<RoundDataPROTO> roundDatas) {
        this.roundDatas = roundDatas;
    }

    public void addRoundDatas(RoundDataPROTO roundDataPROTO) {
        this.roundDatas.add(roundDataPROTO);
    }
}
