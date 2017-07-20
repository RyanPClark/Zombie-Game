package enums;

public enum Mode {
	
	PLAYER(0), DEV(1);
	
	public final int type;
	
	Mode(int type){
		this.type = type;
	}
}