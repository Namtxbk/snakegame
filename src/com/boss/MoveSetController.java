package com.boss;

public abstract class MoveSetController {
	protected final int CD, DELAY, DURATION;
	protected int cd;
	protected int delay;
	protected int duration;
	
	protected MoveSetController(int cd, int delay, int duration) {
		CD = cd; DELAY = delay; DURATION = duration;	
		this.cd = cd; this.delay = delay; this.duration = duration;
	}
	
	protected void cast() {
		if (cd > 0) { 								//check cooldown
			cd--;
			return;
		}
		
		if(cd <= 0 && delay != 0) {
			choose();
			delay--;
		}
		
		
		if(cd <= 0 && delay <= 0 && duration != 0) {
			create();
			duration--;
		}
		
		if(cd <= 0 && delay <= 0 && duration <= 0)
			cleanup();
	}
	
	protected abstract void choose();
	protected abstract void create();
	protected abstract void cleanup();	
}
