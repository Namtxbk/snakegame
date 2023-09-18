package com.elements;

import javax.swing.JLabel;
import com.util.Image;
import com.util.State.FieldObject;

public class Field {
	
	public static void createField(FieldObject[][] field, int fieldID, JLabel[][] tile) {
		
		if(fieldID == 0) 
		for(int i = 0; i < 20; i++)
			for(int j = 0; j < 35; j++) {
				field[i][j] = FieldObject.Blank;
				tile[i][j].setIcon(null);
			}
		if(fieldID == 1) { 
			for(int i = 0; i < 20; i++)                        // i tung do j hoanh do 
				for(int j = 0; j < 35; j++)
					if(i == 0 || j == 0 || i == 19 || j == 34) {
						tile[i][j].setIcon(Image.obstaclesImage[0]);
						field[i][j] = FieldObject.Obstacles;
					}else {
						field[i][j] = FieldObject.Blank;
						tile[i][j].setIcon(null);
					}			
		}
		if(fieldID == 2) {
			for(int i = 0; i < 20; i++)
				for(int j = 0; j < 35; j++)
					if(((i == 5 || i == 14) && (j >= 7 && j <= 27)) || ((i == 0 || i == 19) && (j <= 2 || j >= 32)) || ((j == 0 || j == 34) && (i <= 2 || i >= 17))) {
						field[i][j] = FieldObject.Obstacles;
						tile[i][j].setIcon(Image.obstaclesImage[0]);
					}else {
						field[i][j] = FieldObject.Blank;
						tile[i][j].setIcon(null);
					}	
		}
		if(fieldID == 3) {
			for(int i = 0; i < 20; i++)
				for(int j = 0; j < 35; j++)
					if((j == 10 && i <= 12) || (j == 24 && i >= 7) || (i == 16 && j <= 10) || (j >= 24 && i == 3)) {
						field[i][j] = FieldObject.Obstacles;
						tile[i][j].setIcon(Image.obstaclesImage[0]);
					}else {
						field[i][j] = FieldObject.Blank;
						tile[i][j].setIcon(null);
					}
						
		}
		if(fieldID == 4) {
			for(int i = 0; i < 20; i++)
				for(int j = 0; j < 35; j++)
				{
					if ( i == 0 && ( j > 4 && j < 29) || i == 4 && ( j > 4 && j < 29) || i == 9 && ( j > 4 && j < 14) || i == 10 && ( j > 4 && j < 14) || i == 15&& ( j > 4 && j < 29) || i == 19 && ( j > 4 && j < 29) || j ==0 || j == 34 ||i == 9 && ( j > 19 && j < 29) ||i == 10 && ( j > 19 && j < 29) ) {
						field[i][j] = FieldObject.Obstacles;
						tile[i][j].setIcon(Image.obstaclesImage[0]);
					}else{
						field[i][j] = FieldObject.Blank;
						tile[i][j].setIcon(null);
					}
				}
					
		}
		if(fieldID == 5) {
			for(int i = 0; i < 20; i++)
				for(int j = 0; j < 35; j++)
					if(((i == 3 || i == 16) && (j >= 7 && j <= 27))  || j == 0 && ( i <= 7 || i >=11 )|| ((j == 6 || j == 28) && (i >= 3 && i <= 7))||((j == 6 || j == 28) && (i >= 11 && i <= 16))||j == 34 && ( i <= 7 || i >=11 )|| i == 0 && (j <= 13 || j >=21)||i == 19 && (j <= 13 || j >=21) ) 
					{
						field[i][j] = FieldObject.Obstacles;
						tile[i][j].setIcon(Image.obstaclesImage[0]);
					}else {
						field[i][j] = FieldObject.Blank;
						tile[i][j].setIcon(null);
					}
						
		}
		if(fieldID == 6) { 
			for(int i = 0; i < 20; i++)                        // i tung do j hoanh do 
				for(int j = 0; j < 35; j++)
					if(i == 0 || j == 0 || i == 19 || j == 34||(j==3||j==6||j==9)&&(i<=8)|| (j==31 || j==28||j==25) &&(i>=11)|| (i==3 || i==6) &&(j>=26)||(i==13 || i==16) &&(j<=8)) 
					{
						tile[i][j].setIcon(Image.obstaclesImage[0]);
						field[i][j] = FieldObject.Obstacles;
					}else {
						field[i][j] = FieldObject.Blank;
						tile[i][j].setIcon(null);
					}			
		}
		
		
		if(fieldID == 7) {
			for(int i = 0; i < 20; i++)
				for(int j = 0; j < 35; j++)
					if((i != 9 && j != 16)&&((i==3||(i==15))&&(j>=13&&j<=19))||(i==4&&(j==12||j==20)||(i==5)&&(j==11||j==21)||(i==14&&(j==12||j==20))||(i==13)&&(j==11||j==21)||(j==10||j==22)&&(i>=6&&i<=12)&&i!=9)||(j ==6||j==26) &&(i>=0 && i <=6) || i == 3 && (j >=3 && j <=9)|| i == 3 && (j >=23 && j <=29) || j ==6 &&(i>=12 && i <=18)|| j == 26 &&(i>=12 && i <= 18) || i == 15 && (j >=3 && j <=9)|| i == 15 && (j >=23 && j <=29)  )  
					{
						field[i][j] = FieldObject.Obstacles;
						tile[i][j].setIcon(Image.obstaclesImage[0]);
					}else {
						field[i][j] = FieldObject.Blank;
						tile[i][j].setIcon(null);
					}
						
		}
		
		if(fieldID == 8) {
			for(int i = 0; i < 20; i++)
				for(int j = 0; j < 35; j++)
					if(i == 13 || j==19 && i>=13||i==6 &&(j<=15 || j >= 19)|| j==15 && i <=6|| i ==0 &&(j <= 28&&j >= 9)||i == 0 &&j <=2|| j ==0&& i <=2) 
					{
						field[i][j] = FieldObject.Obstacles;
						tile[i][j].setIcon(Image.obstaclesImage[0]);
					}else {
						field[i][j] = FieldObject.Blank;
						tile[i][j].setIcon(null);
					}
						
		}
		tile[9][16].setIcon(Image.spawnHole[1]);
	}
	
	public static void createBossField(FieldObject[][] field, JLabel bossTile, JLabel[][] tile) {
		for(int i = 0; i < 20; i++)
			for(int j = 0; j < 35; j++)
				if((7 < i && i < 13) && (j > 14 && j < 20))  
				{
					field[i][j] = FieldObject.Boss;
					tile[i][j].setVisible(false);
				}else {
					field[i][j] = FieldObject.Blank;
					tile[i][j].setIcon(null);
				}
	
		bossTile.setVisible(true);
		bossTile.setIcon(Image.bossIdle[0]);
		tile[16][30].setIcon(Image.spawnHole[1]);
	}
}
