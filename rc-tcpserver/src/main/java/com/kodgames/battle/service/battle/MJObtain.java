package com.kodgames.battle.service.battle;

public class MJObtain 
{	
	private int type;
	private int card;
	private String target;
	private boolean valid = true;
	
	public MJObtain(int type, int card, String target, boolean valid)
	{
		this.type = type;
		this.card = card;
		this.target = target;
		this.valid = valid;
	}
	
	public int getType() 
	{
		return type;
	}
	
	public void setType(int type) 
	{
		this.type = type;
	}
	
	public int getCard() 
	{
		return card;
	}
	
	public void setCard(int card) 
	{
		this.card = card;
	}

	public String getTarget() 
	{
		return target;
	}

	public void setTarget(String target) 
	{
		this.target = target;
	}

	public boolean isValid() 
	{
		return valid;
	}

	public void setValid(boolean valid) 
	{
		this.valid = valid;
	}
	
	
}
