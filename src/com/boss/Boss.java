package com.boss;

import java.util.Random;
import javax.swing.JLabel;
import com.elements.*;
import com.util.State.*;
import com.util.Image;
import com.util.Location;



public class Boss {
	Random rng = new Random();
	
	static final int[] BLOCKADE_CREATOR_COOLDOWN = {25, 30}, BLOCKADE_CREATOR_DELAY = {10, 8};
	static final int[] BEAM_CREATOR_COOLDOWN = {10, 15}, BEAM_CREATOR_DELAY = {11, 9}, BEAM_CREATOR_DURATION = {15, 18}, BEAM_BEFORE_STRENGTHEN = {999999, 3};
	static final int[] CONFUSION_POISON_COOLDOWN = {60, 40}, CONFUSION_POISON_MAX = {3, 6};
	static final int TELEPORT_COOLDOWN = 1, TELEPORT_MAX = 1; //1 horizonal beam       0 vertical beam
	static int phase;
	
	private int lightPillar;
	private boolean firstTimeReconstruct;
	
	public BlockadeCreator blkcrt;
	public BeamCreator beamcrt;
	public PoisonCreator poisoncrt;
	public TeleportCreator telecrt;
	
	public Boss() {
		phase = 0;
		setFirstTimeReconstruct(true);
		lightPillar = 0;
		blkcrt = new BlockadeCreator(BLOCKADE_CREATOR_COOLDOWN[phase], BLOCKADE_CREATOR_DELAY[phase], 1);
		beamcrt = new BeamCreator(BEAM_CREATOR_COOLDOWN[phase], BEAM_CREATOR_DELAY[phase], BEAM_CREATOR_DURATION[phase], BEAM_BEFORE_STRENGTHEN[phase]);
		poisoncrt = new PoisonCreator(CONFUSION_POISON_COOLDOWN[phase], 1, 1, CONFUSION_POISON_MAX[phase]);
		telecrt = new TeleportCreator(TELEPORT_COOLDOWN, 1, 1, TELEPORT_MAX);
	}
	
	public void fieldReconstruct() {
		if(lightPillar == 35) {
			Food.setChangeFoodToConfusion(true);
			Food.createFood();
			if(beamcrt.beamCreate != null) beamcrt.beamCreate.cancel();
			if(beamcrt.beamLockOn != null) beamcrt.beamLockOn.cancel();
			blkcrt = new BlockadeCreator(BLOCKADE_CREATOR_COOLDOWN[phase], BLOCKADE_CREATOR_DELAY[phase], 2);
			beamcrt = new BeamCreator(BEAM_CREATOR_COOLDOWN[phase], BEAM_CREATOR_DELAY[phase], BEAM_CREATOR_DURATION[phase], BEAM_BEFORE_STRENGTHEN[0]);
			GameMain.psychoFusionStop = false;
			return;
		}
		if(isFirstTimeReconstruct()) {
			lightPillar = 17;
		}
		for(int j = 0; j < 20; j++)
				if(GameMain.field[j][lightPillar] != FieldObject.Boss) {
					if(isFirstTimeReconstruct())
						GameMain.field[j][lightPillar] = FieldObject.Blank;
					if(lightPillar < 35) {
						GameMain.field[j][lightPillar] = FieldObject.Light;;
						GameMain.field[j][34-lightPillar] = FieldObject.Light;
					}
					if(!isFirstTimeReconstruct()) {
						if(GameMain.field[j][lightPillar-1] != FieldObject.Boss) GameMain.field[j][lightPillar-1] = FieldObject.Blank;
						if(GameMain.field[j][35-lightPillar] != FieldObject.Boss) GameMain.field[j][35-lightPillar] = FieldObject.Blank;
						if((lightPillar-1) % 4 == ((j%8==0)?0:2) && j % 4 == 0 && !Snake.criticalArea(new Location(lightPillar-1, j))) {
							GameMain.field[j][lightPillar-1] = FieldObject.Obstacles;
							GameMain.tile[j][lightPillar-1].setIcon(Image.obstaclesImage[1]);
						}
						if((35-lightPillar) % 4 == ((j%8==0)?0:2) && j % 4 == 0 && !Snake.criticalArea(new Location(35-lightPillar, j))){
							GameMain.field[j][35-lightPillar] = FieldObject.Obstacles;
							GameMain.tile[j][35-lightPillar].setIcon(Image.obstaclesImage[1]);
						}
					}	
				}
			lightPillar++;
		if(lightPillar == 35) {
			for(int j = 0; j < 20; j++) {
				GameMain.field[j][lightPillar-1] = FieldObject.Blank;
				GameMain.field[j][35-lightPillar]= FieldObject.Blank;
				if((lightPillar-1) % 4 == ((j%8==0)?0:2) && j % 4 == 0 && !Snake.criticalArea(new Location(lightPillar-1, j))) {
					GameMain.field[j][lightPillar-1] = FieldObject.Obstacles;
					GameMain.tile[j][lightPillar-1].setIcon(Image.obstaclesImage[1]);
				}
				if((35-lightPillar) % 4 == ((j%8==0)?0:2) && j % 4 == 0 && !Snake.criticalArea(new Location(35-lightPillar, j))){
					GameMain.field[j][35-lightPillar] = FieldObject.Obstacles;
					GameMain.tile[j][35-lightPillar].setIcon(Image.obstaclesImage[1]);
				}
			}
		}
		for(Location s: Snake.snakeLocation)
			GameMain.field[s.getLocation()[1]][s.getLocation()[0]] = FieldObject.Snake;
		setFirstTimeReconstruct(false);
	}
	
	public int getPhase() {
		return phase;
	}
	
	public int getlightPillar() {
		return lightPillar;
	}
	
	public void phaseChange() {
        if(phase == 1)
        	return;
		if(beamcrt.beamLockOn != null) beamcrt.beamLockOn.cancel();
        if(beamcrt.beamCreate != null) beamcrt.beamCreate.cancel();
		phase = 1;
		blkcrt = new BlockadeCreator(BLOCKADE_CREATOR_COOLDOWN[phase], BLOCKADE_CREATOR_DELAY[phase], 1);
		beamcrt = new BeamCreator(BEAM_CREATOR_COOLDOWN[phase], BEAM_CREATOR_DELAY[phase], BEAM_CREATOR_DURATION[phase], BEAM_BEFORE_STRENGTHEN[phase]);
		poisoncrt = new PoisonCreator(CONFUSION_POISON_COOLDOWN[phase], 1, 1, CONFUSION_POISON_MAX[phase]);
    	for(int j = 0; j < 35; j++)
		for(int i = 0; i < 20; i++)
			if(GameMain.field[i][j] == FieldObject.Beam || GameMain.field[i][j] == FieldObject.LockOn || GameMain.field[i][j] == FieldObject.LockOnBonus) {
				GameMain.field[i][j] = FieldObject.Blank;
				GameMain.tile[i][j].setIcon(null);
			}
    }

	public boolean isFirstTimeReconstruct() {
		return firstTimeReconstruct;
	}
	
	public void setFirstTimeReconstruct(boolean firstTimeReconstruct) {
		this.firstTimeReconstruct = firstTimeReconstruct;
	}
}