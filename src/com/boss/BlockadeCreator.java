package com.boss;

import com.util.Image;
import com.util.State.FieldObject;
import java.util.Random;
import com.elements.*;

public class BlockadeCreator extends MoveSetController {
	private int x, y;
	private Random rng;
	
	public BlockadeCreator(int cd, int delay, int duration) {
		super(cd, delay, duration);
		rng = new Random();
	}
	
	public void cast() {
		super.cast();
	}
	
	@Override
	public void choose() { 
		if (delay==DELAY)
			do {
				x = rng.nextInt(35);
				y = rng.nextInt(20);
			} while(GameMain.field[y][x] != FieldObject.Blank);
			GameMain.field[y][x] = FieldObject.LockOnObstacles;
		if (delay==10) GameMain.obstacleSpriteCount=2;
		if (delay==9) GameMain.obstacleSpriteCount=2;
		if (delay==8) GameMain.obstacleSpriteCount=2;
		if (delay==7) GameMain.obstacleSpriteCount=2;
		if (delay==6) GameMain.obstacleSpriteCount=3;
		if (delay==5) GameMain.obstacleSpriteCount=3;
		if (delay==4) GameMain.obstacleSpriteCount=4;
		if (delay==3) GameMain.obstacleSpriteCount=4;
		if (delay==2) GameMain.obstacleSpriteCount=5;
		if (delay==1) GameMain.obstacleSpriteCount=5;
	}
	
	@Override
	public void create() {								//create pernament blockade and reset
		GameMain.field[y][x] = FieldObject.Obstacles;
		GameMain.tile[y][x].setIcon(Image.obstaclesImage[1]);
	}
	
	@Override
	public void cleanup() {
		cd = CD;
		duration = DURATION;
		delay = DELAY;
	}
}
