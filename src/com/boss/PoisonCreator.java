package com.boss;

import com.elements.Food;

public class PoisonCreator extends MoveSetController {

	private final int MAX;
	
	protected PoisonCreator(int cd, int delay, int duration, int max) {
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
		if (Food.getCFoodCount() >= MAX)
			return;
		Food.createConfusionFood();
	}

	@Override
	protected void cleanup() {
		cd = CD;
		delay = DELAY;
		duration = DURATION;
	}

}
