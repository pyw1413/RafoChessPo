package com.kodgames.battle.entity.record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/17.
 */
public class BWRoundRecordRES {
    private int result ;
    private String accountID ;
    private List<RoundDataPROTO> roundData = new ArrayList<>(); // 内含回放

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public List<RoundDataPROTO> getRoundData() {
        return roundData;
    }

    public void setRoundData(List<RoundDataPROTO> roundData) {
        this.roundData = roundData;
    }

    public void addRoundData(RoundDataPROTO roundDataPROTO) {
        this.roundData.add(roundDataPROTO);
    }
}
