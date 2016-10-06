package com.rafo.chess.calculate.majiang.operation;

import com.rafo.chess.calculate.majiang.operation.impl.ChiOperation;
import com.rafo.chess.calculate.majiang.operation.impl.DaOperation;
import com.rafo.chess.calculate.majiang.operation.impl.FinalOperation;
import com.rafo.chess.calculate.majiang.operation.impl.GangOperation;
import com.rafo.chess.calculate.majiang.operation.impl.GuoOperation;
import com.rafo.chess.calculate.majiang.operation.impl.HuOperation;
import com.rafo.chess.calculate.majiang.operation.impl.JiaozuiOperation;
import com.rafo.chess.calculate.majiang.operation.impl.LiujuOperation;
import com.rafo.chess.calculate.majiang.operation.impl.PengOperation;
import com.rafo.chess.calculate.majiang.operation.impl.TingOperation;
import com.rafo.chess.calculate.majiang.operation.impl.ZhuaOperation;

/***
 * 1 抓拍 2尺牌 3 出牌 4 杠 5胡6碰7听8刘局
 * @author Administrator
 *
 */
public enum OperationEnum {
	ZHUA(1) {
		@Override
		public Class<ZhuaOperation> getOperationClass() {
			return ZhuaOperation.class;
		}
	},
	CHICARD(2) {
		@Override
		public Class<ChiOperation> getOperationClass() {
			return ChiOperation.class;
		}
	},
	DACARD(3) {
		@Override
		public Class<DaOperation> getOperationClass() {
			return DaOperation.class;
		}
	},
	GANG(4) {
		@Override
		public Class<GangOperation> getOperationClass() {
			return GangOperation.class;
		}
	},
	HU(5) {
		@Override
		public Class<HuOperation> getOperationClass() {
			return HuOperation.class;
		}
	},
	PENG(6) {
		@Override
		public Class<PengOperation> getOperationClass() {
			return PengOperation.class;
		}
	},
	TING(7) {
		@Override
		public Class<TingOperation> getOperationClass() {
			return TingOperation.class;
		}
	},
	JIAOZUI(8) {
		@Override
		public Class<JiaozuiOperation> getOperationClass() {
			return JiaozuiOperation.class;
		}
	},
	FINAL(9) {
		@Override
		public Class<FinalOperation> getOperationClass() {
			return FinalOperation.class;
		}
	},
	LIUJU(10) {
		@Override
		public Class<LiujuOperation> getOperationClass() {
			return LiujuOperation.class;
		}
	},
	GUO(11) {
		@Override
		public Class<GuoOperation> getOperationClass() {
			return GuoOperation.class;
		}
	},
	;

	private int flag;
	
	OperationEnum(int flag){
		this.flag = flag;
	}
	
    public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public static OperationEnum getByFlag(int flag) {
		for(OperationEnum opt : values()){
			if(opt.getFlag()==flag)
				return opt;
		}
        return null;
    }
	public abstract Class<? extends IOperation> getOperationClass();
	
	public static OperationEnum getByClass(Class clazz){
		for(OperationEnum en : values()){
			if(en.getOperationClass()!=clazz)
				continue;
			return en;
		}
		return null;
		
	}
	
}
