package com.boss;

import com.elements.Food;

public class TeleportCreator extends MoveSetController {

	private final int MAX;
	
	protected TeleportCreator(int cd, int delay, int duration, int max) {
		super(cd, delay, duration);
		this.MAX = max;
	}

	public void cast() {
		super.cast();
	}
	
	@Override
	protected void choose() {
		

	}

	@Override
	protected void create() {
		if (Food.getTeleCount() == MAX)
			return;
		Food.createTeleport();
	}

	@Override
	protected void cleanup() {
		cd = CD;
		delay = DELAY;
		duration = DURATION;
	}

}
