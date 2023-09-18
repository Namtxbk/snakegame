package com.util;

import java.io.File;

import javax.swing.ImageIcon;

import com.elements.Field;

public class Image {
	public static ImageIcon muteButton = new ImageIcon(new File("").getAbsolutePath().concat("\\image\\muteButton.png"));
	public static ImageIcon unMuteButton = new ImageIcon(new File("").getAbsolutePath().concat("\\image\\unMute.jpg"));
	public static ImageIcon foodImage = new ImageIcon(Field.class.getResource("/image/Food/food.png"));
	public static ImageIcon foodImage2 = new ImageIcon(Field.class.getResource("/image/Food/food2.png"));
	public static ImageIcon logoImage = new ImageIcon(Field.class.getResource("/image/Other/logo.png"));
	public static ImageIcon[] poisonImage = new ImageIcon[5];
	public static ImageIcon lightPillar = new ImageIcon(Field.class.getResource("/image/Obstacles/lightPillar.png"));
	public static ImageIcon[] bossIdle = new ImageIcon[13];
	public static ImageIcon[] bossIdleL = new ImageIcon[13];
	public static ImageIcon[] bossIdle2nd = new ImageIcon[20];
	public static ImageIcon[] bossDescend = new ImageIcon[17];
	public static ImageIcon[] bossNormalBeam = new ImageIcon[10];
	public static ImageIcon[] bossNormalBeamL = new ImageIcon[10];
	public static ImageIcon[] bossNP = new ImageIcon[15];
	public static ImageIcon[] skillCutIn = new ImageIcon[40];
	public static ImageIcon[] skillCasting = new ImageIcon[40];
	public static ImageIcon[] snakeBodyImage = new ImageIcon[8];
	public static ImageIcon[] snakeHeadImage = new ImageIcon[4];
	public static ImageIcon[] snakeTailImage = new ImageIcon[4];
	public static ImageIcon[] horizonalBeamImage = new ImageIcon[7];
	public static ImageIcon[] verticalBeamImage = new ImageIcon[7];
	public static ImageIcon[] obstaclesImage = new ImageIcon[6];
	public static ImageIcon[] spawnHole = new ImageIcon[4];
	public static ImageIcon[] telePortal = new ImageIcon[8];
	
	public static void initializeImage() {
		for(int i=0;i<=7;i++)
		{
			snakeBodyImage[i] = new ImageIcon(Field.class.getResource("/image/Snake/body".concat(Integer.toString(i)).concat(".png")));
		}
		for(int i=0;i<=3;i++)
		{
			snakeHeadImage[i] = new ImageIcon(Field.class.getResource("/image/Snake/head".concat(Integer.toString(i)).concat(".png")));
			snakeTailImage[i] = new ImageIcon(Field.class.getResource("/image/Snake/tail".concat(Integer.toString(i)).concat(".png")));
		}
		for(int i = 1; i <= 6; i++) {
			horizonalBeamImage[i] = new ImageIcon(Field.class.getResource("/image/Beam/Hfirep".concat(Integer.toString(i)).concat(".png")));
			verticalBeamImage[i] = new ImageIcon(Field.class.getResource("/image/Beam/Vfirep".concat(Integer.toString(i)).concat(".png")));
		}
		for(int i=1;i<=12;i++)
		{
			bossIdle[i] = new ImageIcon(Field.class.getResource("/image/Boss/bossIdleCasting".concat(Integer.toString(i)).concat(".png")));
			bossIdleL[i] = new ImageIcon(Field.class.getResource("/image/Boss/bossIdleCastingL".concat(Integer.toString(i)).concat(".png")));
		}
		for(int i=1;i<=16;i++)
		{
			bossDescend[i] = new ImageIcon(Field.class.getResource("/image/Boss/bossDescend".concat(Integer.toString(i)).concat(".png")));
		}
		for(int i=1;i<=9;i++)
		{
			bossNormalBeam[i] = new ImageIcon(Field.class.getResource("/image/Boss/boss1stskill".concat(Integer.toString(i)).concat(".png")));
			bossNormalBeamL[i] = new ImageIcon(Field.class.getResource("/image/Boss/boss1stskillL".concat(Integer.toString(i)).concat(".png")));
		}
		for(int i=1;i<=19;i++) {	
			bossIdle2nd[i] = new ImageIcon(Field.class.getResource("/image/Boss/bossphase2nd".concat(Integer.toString(i)).concat(".png")));
		}
		for(int i=1 ; i<=3 ; i++) {
			spawnHole[i] = new ImageIcon(Field.class.getResource("/image/SpawnHole/spawnHole".concat(Integer.toString(i)).concat(".png")));
		}
		for(int i=1; i <= 5; i++)
			obstaclesImage[i] = new ImageIcon(Field.class.getResource("/image/Obstacles/block".concat(Integer.toString(i)).concat(".png")));
		for(int i=1; i<=14 ; i++)
			bossNP[i] = new ImageIcon(Field.class.getResource("/image/Boss/bossNoblePhantasm".concat(Integer.toString(i)).concat(".png")));
		for(int i=1; i<=33 ; i++)
			skillCutIn[i] = new ImageIcon(Field.class.getResource("/image/Boss/SkillCutInBig".concat(Integer.toString(i)).concat(".png")));
		for(int i=1;i<=6;i++)
			skillCasting[i] = new ImageIcon(Field.class.getResource("/image/Boss/bossSkillCasting".concat(Integer.toString(i)).concat(".png")));
		for(int i = 1; i <= 5; i++)
			poisonImage[i-1] = new ImageIcon(Field.class.getResource("/image/Food/poison".concat(Integer.toString(i)).concat(".png")));
		
		for(int i = 1; i <= 8; i++)
			telePortal[i-1] = new ImageIcon(Field.class.getResource("/image/Other/portal".concat(Integer.toString(i)).concat(".png")));
	}
}
