package paulevs.creative;

public interface CreativePlayer {
	public boolean isCreative();
	
	public void setCreative(boolean creative);
	
	public boolean isFlying();
	
	public void setFlying(boolean flying);
	
	public int getJumpTicks();
	
	public void setJumpTicks(int ticks);
	
	/*public boolean isFlyingKeyPressed();
	
	public boolean isSneakingKeyPressed();
	
	public void setFlyingKeyPressed(boolean value);
	
	public void setSneakingKeyPressed(boolean value);*/
}
