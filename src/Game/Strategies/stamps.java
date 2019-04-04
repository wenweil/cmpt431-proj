package Game.Strategies;

public enum stamps {
	CONNECTIONREQUEST (1),
	CONNECTIONRESPONSE (2),
	SQUAREIDREQUEST (3),
	SQUAREIDRESPONSE (4),
	MUTEXLOCKREQUEST (5),
	MUTEXLOCKRESPONSE (6),
	MUTEXUNLOCKREQUEST (7),
	MUTEXUNLOCKRESPONSE (8),
	
	DRAWINGDATA(55),
	DRAWFAIL(56),
	DRAWCLAIM(57),
	
	UPDATELISTOFUSER(90);
	
	private byte b;
	
	stamps(int b){
		this.b =(byte) b;
	}
	
	public byte val() {
		return b;
	}
	
	
}
