package com.elements;


import java.util.Random;
import com.util.Location;
import com.util.State.FieldObject;

public class Food {
	private static boolean changeFoodToConfusion;
	private static int TeleCount, CFoodCount;
	static Location[] teleLocation;
	static Location foodLocation, confusionFoodLocation;	
	static Random rng;

	public static void foodReset() {
		TeleCount = 0; 
		CFoodCount = 0;
		changeFoodToConfusion = false;
		teleLocation = new Location[2];
		foodLocation = new Location();
		confusionFoodLocation = new Location();	
		rng = new Random();
	}
	
	public static void createFood() {
		do {
			foodLocation = new Location(rng.nextInt(35), rng.nextInt(20));
		} while(GameMain.field[foodLocation.getLocation()[1]][foodLocation.getLocation()[0]] != FieldObject.Blank || Snake.criticalArea(foodLocation));
		GameMain.field[foodLocation.getLocation()[1]][foodLocation.getLocation()[0]] = (changeFoodToConfusion)?FieldObject.Confusion:FieldObject.Food;		
	}
	
	public static void createTeleport() {
		for(int i = 0; i < 2; i++){
			do {
				teleLocation[i] = new Location(rng.nextInt(35), rng.nextInt(20));
			} while(GameMain.field[teleLocation[i].getLocation()[1]][teleLocation[i].getLocation()[0]] != FieldObject.Blank || Snake.criticalArea(teleLocation[i]));
			GameMain.field[teleLocation[i].getLocation()[1]][teleLocation[i].getLocation()[0]] = FieldObject.Tele;
		}
		TeleCount++;
	}
	
	public static void createConfusionFood() {
		do {
			confusionFoodLocation = new Location(rng.nextInt(35), rng.nextInt(20));
		} while(GameMain.field[confusionFoodLocation.getLocation()[1]][confusionFoodLocation.getLocation()[0]] != FieldObject.Blank || Snake.criticalArea(confusionFoodLocation));
		GameMain.field[confusionFoodLocation.getLocation()[1]][confusionFoodLocation.getLocation()[0]] = FieldObject.Confusion;
		CFoodCount++;
	}
	
	public static Location getFoodLocation() {
		return foodLocation;
	}
	
	public static Location getConfusionFoodLocation() {
		return confusionFoodLocation;
	}
	
	public static Location[] getTeleLocation() {
		return teleLocation;
	}
	
	public static void eatC() {
		CFoodCount--;
	}
	
	public static void eatTele() {
		TeleCount--;
	}
	
	public static int getCFoodCount() {
		return CFoodCount;
	}

	public static int getTeleCount() {
		return TeleCount;
	}
	
	public static boolean getChangeFoodToConfusion() {
		return changeFoodToConfusion;
	}
	
	public static void setChangeFoodToConfusion(boolean changeFoodToConfusion) {
		Food.changeFoodToConfusion = changeFoodToConfusion;
	}
	
	public static void deleteGate(FieldObject[][] field) {
		for(int i = 0; i < 2; i++) 
			field[teleLocation[i].getLocation()[1]][teleLocation[i].getLocation()[0]] = FieldObject.Blank;
	}	
}
