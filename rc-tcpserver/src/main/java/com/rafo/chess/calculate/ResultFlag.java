package com.rafo.chess.calculate;

public enum ResultFlag {
	HU_PINGHU(21),
	HU_DADUIZI(22),
	HU_QIDUI(23),
	HU_LONGQIDUI(24),
	HU_QINGYISE(25),
	HU_QINGQIDUI(26),
	HU_QINGDADUI(27),
	HU_QINGLONGDUI(28),
	
	ACT_WEIJIAOZUI(100),
	ACT_DIANPAO(101),
	ACT_ZIMO(102),
	ACT_JIAOZUI(103),
	ACT_PUTONGJI(104),
	ACT_CHONGFENGJI(105),
	ACT_FANPAIJI(106),
	ACT_ZERENJI(107),
	ACT_MINGDOU(108),
	ACT_DIANMINGDOU(109),
	ACT_ANDOU(110),
	ACT_BUDOU(111),
	ACT_GANGSHANGHU(112),
	ACT_GANGHOUPAO(113),
	ACT_DIANGANGHOUPAO(114),
	ACT_QIANGGANGHU(115),
	ACT_YINGBAO(116),
	ACT_RUANBAO(117),
	
	
	
	;
	
	ResultFlag(int flag){
		this.flag = flag;
	}
	
	private int flag;

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	
}
