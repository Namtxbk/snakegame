package com.boss;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import com.audioplayer.AudioPlayer;
import com.elements.Food;
import com.elements.GameMain;
import com.util.Image;
import com.util.Sound;
import com.util.State.BossState;
import com.util.State.FieldObject;

public class BeamCreator extends MoveSetController {
	private Random rng;
	private boolean foodDeleted;
	private int beamSpriteCount;
	private int x, y;
	private int xBonus, yBonus;
	public int beamDirection;
	private int beamBeforeStrengthen;
	private final int BEAM_BEFORE_STRENGTHEN;
	private AudioPlayer audioPlayer;
	public Timer beamLockOn, beamCreate;
	
	protected BeamCreator(int cd, int delay, int duration, int beamBeforeStrengthen) {
		super(cd, delay, duration);
		audioPlayer = new AudioPlayer(GameMain.muted);
		this.BEAM_BEFORE_STRENGTHEN = beamBeforeStrengthen;
		this.beamBeforeStrengthen = beamBeforeStrengthen;
		rng = new Random();
		this.cd+=25;
		if(beamBeforeStrengthen == 3)
			this.beamBeforeStrengthen = 0;
	}
	
	public void cast() {
		super.cast();
	}

	@Override
	protected void choose() {
		if(delay != DELAY)
			return;
		delay--;
		x = rng.nextInt(35);
		y = rng.nextInt(20);
		xBonus = rng.nextInt(35);
		yBonus = rng.nextInt(20);
		beamDirection = rng.nextInt(2);
		beamLockOn = new Timer();
		beamLockOn.scheduleAtFixedRate(new TimerTask(){
			public void run(){
				if(beamDirection == 0)
					createVLockOn(x, FieldObject.LockOn);
				else
					createHLockOn(y, FieldObject.LockOn);
				if(Boss.phase == 1 && beamBeforeStrengthen != 0) {
					if(beamDirection == 0) {
						createVLockOn(xBonus, FieldObject.LockOn);
						createHLockOn(yBonus, FieldObject.LockOnBonus);
					} else {
						createHLockOn(yBonus, FieldObject.LockOn);
						createVLockOn(xBonus, FieldObject.LockOnBonus);
					}
				}
				if(delay <= 0)
					beamLockOn.cancel();
			}
		},0,60);
		if (Boss.phase==0){
			GameMain.bossState = BossState.NormalBeam;
			if (GameMain.spriteTransition) {
				try {
					audioPlayer.playSE(Sound.CAST_BEAM);
				} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
					e.printStackTrace();
				} 
				GameMain.bossSpriteCount=1;
				GameMain.spriteTransition=false;
			}
		}else{
			if (beamBeforeStrengthen == 0) {
				GameMain.bossState = BossState.BossNP;
				if (GameMain.spriteTransition) {
					try {
						audioPlayer.playSE(Sound.BOSS_NP);
					} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
						e.printStackTrace();
					} 
				GameMain.bossSpriteCount=2;
				GameMain.spriteTransition=false;
				} 
			}else{
				GameMain.bossState = BossState.NormalBeam;
				if (GameMain.spriteTransition){
					try {
						audioPlayer.playSE(Sound.CAST_BEAM);
					} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
						e.printStackTrace();
					} 
					GameMain.bossSpriteCount=13;
					GameMain.spriteTransition=false;
				}
			}			
		}
	}

	@Override
	protected void create() {
		if(duration != DURATION)
			return;
		duration--;
		try {
			audioPlayer.playSE(Sound.BEAM_SOUND);
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	
	beamCreate = new Timer();
	beamCreate.scheduleAtFixedRate(new TimerTask(){
		public void run() {
			if(beamDirection == 0) {
				if(beamBeforeStrengthen == 0)
					createStrengthenedVBeam();
				else 
					createNormalVBeam(x);
			} else {
				if(beamBeforeStrengthen == 0)
					createStrengthenedHBeam();
				else 
					createNormalHBeam(y);		
			}
			if(Boss.phase == 1 && beamBeforeStrengthen != 0) {
				if(beamDirection == 0) {
					createNormalVBeam(xBonus);
					createNormalHBeam(yBonus);
				} else {
					createNormalHBeam(yBonus);
					createNormalVBeam(xBonus);
				}
			}
			beamSpriteCount++;
			if(beamSpriteCount > 2) beamSpriteCount = 1;
			if(duration <= 0)
				beamCreate.cancel();
		}	
	},0,60);
	}

	@Override
	protected void cleanup() {
		if(beamCreate != null) beamCreate.cancel();
		if(beamLockOn != null) beamLockOn.cancel();
		for(int j = 0; j < 35; j++)
			for(int i = 0; i < 20; i++)
				if(GameMain.field[i][j] == FieldObject.Beam) {
					GameMain.field[i][j] = FieldObject.Blank;
					GameMain.tile[i][j].setIcon(null);
				}
					
			if(foodDeleted) {
				Food.createFood();
				foodDeleted = false;
			}
			
			if(beamBeforeStrengthen == 0)
				beamBeforeStrengthen = BEAM_BEFORE_STRENGTHEN;
			else
				beamBeforeStrengthen--;
		cd = CD;
		delay = DELAY;
		duration = DURATION;
		beamSpriteCount = 0;
	}

	public void createHLockOn(int y, FieldObject object) {
		if(beamBeforeStrengthen == 0) {
			for(int j = 0; j < 20; j+=4)
			for(int i = 0; i < 35; i++)
				if(GameMain.field[j][i] == FieldObject.Blank)
					GameMain.field[j][i] = object;
			}
			else
				for(int i = 0; i < 35; i++)
					if(GameMain.field[y][i] == FieldObject.Blank)
						GameMain.field[y][i] = object;
	}
	
	public void createVLockOn(int x, FieldObject object) {
		if(beamBeforeStrengthen == 0) {
			for(int j = 0; j < 35; j+=4)
			for(int i = 0; i < 20; i++)
				if(GameMain.field[i][j] == FieldObject.Blank) 
					GameMain.field[i][j] = object;
		}	
		else 
			for(int i = 0; i < 20; i++)
				if(GameMain.field[i][x] == FieldObject.Blank) 
					GameMain.field[i][x] = object;
	}
	
	public void createNormalVBeam(int x) {
		for(int i = 0; i < 20; i++) {
			if(GameMain.field[i][x] != FieldObject.Snake && GameMain.field[i][x] != FieldObject.Boss) {
				if(GameMain.field[i][x] == FieldObject.Food || (GameMain.field[i][x] == FieldObject.Confusion && Food.getChangeFoodToConfusion())) foodDeleted = true;
				if(GameMain.field[i][x] == FieldObject.Confusion) Food.eatC();
				if(GameMain.field[i][x] == FieldObject.Tele) {
					Food.deleteGate(GameMain.field);
					Food.eatTele();
				}
				GameMain.field[i][x] = FieldObject.Beam;
				GameMain.tile[i][x].setIcon(Image.verticalBeamImage[(beamSpriteCount+4)%7]);
			}
		}
	}
	
	public void createNormalHBeam(int y) {
		for(int i = 0; i < 35; i++)
			if(GameMain.field[y][i] != FieldObject.Snake && GameMain.field[y][i] != FieldObject.Boss) {
				if(GameMain.field[y][i] == FieldObject.Food || (GameMain.field[y][i] == FieldObject.Confusion && Food.getChangeFoodToConfusion())) foodDeleted = true;
				if(GameMain.field[y][i] == FieldObject.Confusion) Food.eatC();
				if(GameMain.field[y][i] == FieldObject.Tele) {
					Food.deleteGate(GameMain.field);
					Food.eatTele();
				}
				GameMain.field[y][i] = FieldObject.Beam;
				GameMain.tile[y][i].setIcon(Image.horizonalBeamImage[(beamSpriteCount+4)%7]);
			}
	}
	
	public void createStrengthenedVBeam() {
			for(int j = 0; j < 35; j+=4)
			for(int i = 0; i < 20; i++) {
				if(GameMain.field[i][j] != FieldObject.Snake && GameMain.field[i][j] != FieldObject.Boss) {
					if(GameMain.field[i][j] == FieldObject.Food || (GameMain.field[i][j] == FieldObject.Confusion && Food.getChangeFoodToConfusion())) foodDeleted = true;
					if(GameMain.field[i][j] == FieldObject.Confusion) Food.eatC();
					if(GameMain.field[i][j] == FieldObject.Tele) {
						Food.deleteGate(GameMain.field);
						Food.eatTele();
					}
					GameMain.field[i][j] = FieldObject.Beam;
					GameMain.tile[i][j].setIcon(Image.verticalBeamImage[(beamSpriteCount+4)%7]);
				}
			}
	}
	
	public void createStrengthenedHBeam() {
		for(int j = 0; j < 20; j+=4)
		for(int i = 0; i < 35; i++)
				if(GameMain.field[j][i] != FieldObject.Snake && GameMain.field[j][i] != FieldObject.Boss) {
					if(GameMain.field[j][i] == FieldObject.Food || (GameMain.field[j][i] == FieldObject.Confusion && Food.getChangeFoodToConfusion())) foodDeleted = true;
					if(GameMain.field[j][i] == FieldObject.Confusion) Food.eatC();
					if(GameMain.field[j][i] == FieldObject.Tele) {
						Food.deleteGate(GameMain.field);
						Food.eatTele();
					}
					GameMain.field[j][i] = FieldObject.Beam;
					GameMain.tile[j][i].setIcon(Image.horizonalBeamImage[(beamSpriteCount+4)%7]);
				}
	}
}
