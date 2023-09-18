package com.elements;

import java.io.IOException;
import java.util.*;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

import com.handler.AudioPlayer;
import com.util.Location;
import com.util.Sound;
import com.util.State.FieldObject;

public class Snake {
	private final int X_TILES = 35, Y_TILES = 20;
	private int convertX[] = {-1, 0, 1, 0}; 							// chieu di chuyen sang ngang 1 la sang phai -1 la sang trai
	private int convertY[] = {0, -1, 0, 1}; 							// chieu di chuyen sang phai 1 la xuong -1 la len 
	private int confusedDuration;
	private int direction = 5;
	static int preDirection; 
	private int alreadyAte; 										// so thuc an da an 
	public static Queue<Location> snakeLocation; 				//  hang doi luu vi tri con ran 
	public static Location head;		
	//  vi tri dau ran 
	
	AudioPlayer audioPlayer;
	
	public Snake(int alreadyAte, int currentLevel) {					//  khoi tao con ran
		snakeLocation = new LinkedList<Location>();
		if(currentLevel != 9) {
		snakeLocation.add(new Location(16,9)); 	
		snakeLocation.add(new Location(17,9));
		snakeLocation.add(new Location(18,9));
		head = new Location(16, 9);
		}else {
			snakeLocation.add(new Location(30,16)); 	
			snakeLocation.add(new Location(31,16));
			snakeLocation.add(new Location(32,16));
			head = new Location(30, 16);	
		}
		this.alreadyAte = alreadyAte;
		confusedDuration = 0;
		audioPlayer = new AudioPlayer(GameMain.muted);
		preDirection = 5;
		direction = 5;
	}
	
	public int runSnake(FieldObject[][] field, JLabel[][] tile, boolean isMoving, int currentLevel) throws LineUnavailableException, IOException, UnsupportedAudioFileException {	
		if(!isMoving)
			return 2;
		// khoi tao vi tri dau ran khi di chuyen va them vao hang doi

		if(confusedDuration != 0) confusedDuration--;
		
		setDirection();
		directionSetup();
		head = new Location(xSnakeLimit(head.getLocation()[0]+convertX[direction]),ySnakeLimit(head.getLocation()[1]+convertY[direction]));
		preDirection=direction;
		snakeLocation.add(head);
		
		GameMain.fieldDirection[snakeLocation.peek().getLocation()[1]][snakeLocation.peek().getLocation()[0]]=-1;
		
		if(field[head.getLocation()[1]][head.getLocation()[0]] == FieldObject.Food) {
			audioPlayer.playSE(Sound.MUNCH_SOUND);
			alreadyAte++; 
			Food.createFood();// neu an duoc qua thi khong cat duoi va tao qua moi
		}
		
		if(field[head.getLocation()[1]][head.getLocation()[0]] == FieldObject.Confusion) {
			audioPlayer.playSE(Sound.MUNCH_SOUND);												
			confusedDuration = 30;
			Food.eatC();
			
			if(Food.getChangeFoodToConfusion()) {
				Food.createFood();
				alreadyAte++;
			} else
				alreadyAte += 2;
			//field[head.getLocation()[1]][head.getLocation()[0]] = 0;
		}
		
		if(field[head.getLocation()[1]][head.getLocation()[0]] == FieldObject.Tele) {
			audioPlayer.playSE(Sound.PORTAL);
			int xOtherGate = -1, yOtherGate = -1;
			
			for(int i = 0; i < 2; i++) {
				if(Food.getTeleLocation()[i].getLocation()[0] != head.getLocation()[0])
					xOtherGate = Food.getTeleLocation()[i].getLocation()[0];
				if(Food.getTeleLocation()[i].getLocation()[1] != head.getLocation()[1])
					yOtherGate = Food.getTeleLocation()[i].getLocation()[1];
			}
			if(xOtherGate == -1) xOtherGate = Food.getTeleLocation()[0].getLocation()[0];
			if(yOtherGate == -1) yOtherGate = Food.getTeleLocation()[0].getLocation()[1];
			setDirection();
			directionSetup();
			head = new Location(xSnakeLimit(xOtherGate+convertX[direction]),ySnakeLimit(yOtherGate+convertY[direction]));
			preDirection=direction;
			snakeLocation.add(head);
		}
		
		//lose: 1:con ran 3:chuong ngai vat 
		if(field[head.getLocation()[1]][head.getLocation()[0]] == FieldObject.Snake || field[head.getLocation()[1]][head.getLocation()[0]] == FieldObject.Obstacles || field[head.getLocation()[1]][head.getLocation()[0]] == FieldObject.Beam || field[head.getLocation()[1]][head.getLocation()[0]] == FieldObject.Boss) {
			return 1; 													//lose: 1:con ran 3:chuong ngai vat 
		}
		
		if(field[head.getLocation()[1]][head.getLocation()[0]] != FieldObject.Food) {
			int[] last = snakeLocation.poll().getLocation();
			if(field[last[1]][last[0]] == FieldObject.Tele) {
				Food.deleteGate(field);
				Food.eatTele();
			}
			field[last[1]][last[0]] = FieldObject.Blank; 								// neu khong an qua thi cat duoi
		}
		
		field[head.getLocation()[1]][head.getLocation()[0]] = FieldObject.Snake;		// danh dau vi tri la than cua ran 
		return 0; //normal
	}
	
	public int xSnakeLimit(int xSnake) {					
		if(xSnake == X_TILES) return 0;
		if(xSnake < 0) return X_TILES - 1;
		return xSnake;
	}
	
	public int ySnakeLimit(int ySnake) {
		if(ySnake == Y_TILES) return 0;
		if(ySnake < 0) return Y_TILES - 1;
		return ySnake;													// chuyen toa do khi vuot qua ranh gioi
	}
	
	public int getDirection() {
		if(confusedDuration == 0)
			return direction;
		else
			return (direction > 1)? direction - 2 : direction + 2;
	}
	
	public void setDirection() {
		int newDirection;
		if (GameMain.directionQueue.size() != 0)
			newDirection = GameMain.directionQueue.poll();
		else
			newDirection = this.direction;
		
		if(confusedDuration == 0)
			this.direction = newDirection;
		else
			if(newDirection != this.direction) direction = (newDirection > 1)? newDirection - 2 : newDirection + 2;
	}

	public int getAlreadyAte() {
		return alreadyAte;
	}

	public int getConfusedDuration() {
		return confusedDuration;
	}
	
	public void directionSetup() {
		if (preDirection == direction) {
			GameMain.fieldDirection[head.getLocation()[1]][head.getLocation()[0]]=direction;
			GameMain.fieldDirectionTurnPoint[head.getLocation()[1]][head.getLocation()[0]]=direction;
		}
		else
		{
			if(preDirection == 0 && direction==1) GameMain.fieldDirection[head.getLocation()[1]][head.getLocation()[0]]=7;		//4: BottomLeft  5: BottomRight  6:TopLeft   7: TopRight 
			if(preDirection == 1 && direction==0) GameMain.fieldDirection[head.getLocation()[1]][head.getLocation()[0]]=4;		//Hang don vi la huong duoi
			if(preDirection == 0 && direction==3) GameMain.fieldDirection[head.getLocation()[1]][head.getLocation()[0]]=5;
			if(preDirection == 3 && direction==0) GameMain.fieldDirection[head.getLocation()[1]][head.getLocation()[0]]=6;  		
			if(preDirection == 2 && direction==1) GameMain.fieldDirection[head.getLocation()[1]][head.getLocation()[0]]=6;
			if(preDirection == 1 && direction==2) GameMain.fieldDirection[head.getLocation()[1]][head.getLocation()[0]]=5;		
			if(preDirection == 2 && direction==3) GameMain.fieldDirection[head.getLocation()[1]][head.getLocation()[0]]=4;		
			if(preDirection == 3 && direction==2) GameMain.fieldDirection[head.getLocation()[1]][head.getLocation()[0]]=7;
			GameMain.fieldDirectionTurnPoint[head.getLocation()[1]][head.getLocation()[0]]=direction;
		}
	}
	
	public static boolean criticalArea(Location location) {
		return head.distance(location)[0] <= 2 && head.distance(location)[1] <= 3;
	}
}
