package enums;

public enum State {
	
	CHOOSING(0), PLAYING(1), PAUSED(2);
	
	public final int type;
	
	State(int type){
		this.type = type;
	}
}
