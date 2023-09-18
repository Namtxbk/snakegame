package com.elements;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.*;
import javax.swing.*;
import com.boss.Boss;
import com.handler.*;
import com.util.State.*;
import com.util.Image;
import com.util.Sound;

public class GameMain extends JFrame implements ActionListener, KeyListener{

	public static int bossSpriteCount, spriteCount, obstacleSpriteCount, skillCutInCount;
	public static BossState bossState;
	public static GameState gameState;
	public static FieldObject[][] field = new FieldObject[20][35];
	public static boolean spriteTransition;
	public static int[][] fieldDirection = new int[20][35];
	public static int[][] fieldDirectionTurnPoint = new int[20][35];
	public static Queue<Integer> directionQueue = new LinkedList<>();
	public static boolean muted, psychoFusionStop;
	public static JLabel[][] tile = new JLabel[20][35];
	
	private String player_name;
	private boolean isTimer, isTimerFilled, isLoopTimer, s, filled, keyPressed, foodBlinking, isMoving;
	private final int[] PHASE_1_HP = {5, 10, 10, 10}, PHASE_2_HP = {10, 15, 20, 15};
	private Snake snake;
	private Boss boss;
	private int currentLevel, updateCount = 0, hpFill, difficulty;
	private Timer timer, timer2, loopTimer, HPFillTimer;     //timer for boss sprite         timer2 for general sprite
	private ExcelReader excelHandler;
	private TxtReader reader;
	private AudioPlayer audioPlayer;
	private CustomTextField txtNhpTn;
		// khoi tao man hinh ban dau
	public GameMain() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
		isTimer = false; filled=true; isTimerFilled = false; keyPressed = false; foodBlinking = true; isMoving = false; 
		updateCount = 0;
		bossSpriteCount = 1; s=true;skillCutInCount=11; difficulty = 3;
		muted = false;
		spriteCount = 0;
		spriteTransition = true;
		bossState = BossState.Descend;
		gameState = GameState.Menu;
		obstacleSpriteCount = 1;
		reader = new TxtReader();
		audioPlayer = new AudioPlayer(muted);
		timer = new Timer();
		timer2 = new Timer();
		loopTimer = new Timer();
		HPFillTimer = new Timer();
		isLoopTimer = false;
		psychoFusionStop = false;
	
		//creates excel handler
		excelHandler = new ExcelReader();
		
		GUI.guiInit();
		GUI.addListener(this);
		
		//WINDOW
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		audioPlayer.playBGM(Sound.MENU_THEME);
		int x = (int) (dimension.getWidth()/2 - (35*30+15)/2 );
		int y = (int) (dimension.getHeight()/2 - (20*30+80)/2);
		this.setLocation(x, y);
		this.setVisible(true);
		this.setResizable(false);
		this.setIconImage(Image.logoImage.getImage());
		this.setTitle("Snake Game");
		this.setSize(35*30+15, 20*30+80);
		getContentPane().setLayout(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);	
		getContentPane().add(GUI.menuPanel);
		
		txtNhpTn = new CustomTextField(1);
		GUI.menuLabel.add(txtNhpTn);
		txtNhpTn.setColumns(10);
		txtNhpTn.setBounds(429, 165, 207, 41);
		txtNhpTn.setPlaceholder("Enter Your Name Here");
		txtNhpTn.setHorizontalAlignment(JTextField.CENTER);
		this.addKeyListener(this);
		this.setFocusable(true);
		this.isAutoRequestFocus();
		
		//waits for button-click
		while(gameState == GameState.Menu)
			System.out.println(gameState);
	
		if(gameState == GameState.Play) {
			if(currentLevel == 9) {
			if (!txtNhpTn.getText().equals("Enter Your Name Here"))
				player_name = txtNhpTn.getText();
			else player_name = "Nameless";
			System.out.println(player_name);
			GUI.menuLabel.removeAll();
			GUI.menuLabel.add(GUI.casualButton);
			GUI.menuLabel.add(GUI.normalButton);
			GUI.menuLabel.add(GUI.soulsButton);
			GUI.menuLabel.add(GUI.returnToTitleButton);
			GUI.returnToTitleButton.setBounds(430, 370, 205, 40);
			GUI.menuLabel.repaint();	
			
			while(difficulty == 3) {
				System.out.println("Waiting for difficulty choosing");
			}
			}
			
			this.remove(GUI.menuPanel);
			getContentPane().add(GUI.fieldPanel);
			getContentPane().add(GUI.scoreDisplayContainer);
			GUI.fieldPanel.setVisible(true);
			GUI.scoreDisplayContainer.setVisible(true);
			init(currentLevel, 0);
			while(!isMoving) { //waits for moving
				System.out.println("Looping");
				if(gameState == GameState.ReturnToTitle) {
					isLoopTimer = true;
					break;
				}
			}
				if(!isLoopTimer)
				loopTimer.scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {			
						isLoopTimer = true;		
						if(currentLevel < 9) {
							GUI.changeLevel.setEnabled(true);
							if(currentLevel != GUI.changeLevel.getSelectedIndex()) {
								currentLevel = GUI.changeLevel.getSelectedIndex();
								resetField(GUI.changeLevel.getSelectedIndex(), 0);
								requestFocusInWindow();
							}
						}
						if(!isMoving) return;
						if(currentLevel == 9)
						if(psychoFusionStop) {
							draw();
							return;
						}
						try {
							update(snake);
						} catch (LineUnavailableException | IOException | UnsupportedAudioFileException | URISyntaxException e) {
							e.printStackTrace();
						} 
						updateCount++;
						draw();
						if(updateCount < 5) { //spawn point display
							if(currentLevel == 9)
								tile[16][30].setIcon(Image.spawnHole[spriteCount%3+1]);
							else
								tile[9][16].setIcon(Image.spawnHole[spriteCount%3+1]);
						}
					}
				}, 0, 110);
		}

		if(gameState == GameState.Help) {
			getContentPane().add(GUI.helpPanel);
			this.remove(GUI.menuPanel);
			GUI.helpPanel.add(GUI.returnToTitleButton);
			reader.helpReader(GUI.helpDisplay);
			GUI.helpPanel.setVisible(true);
			while(gameState != GameState.ReturnToTitle) {
				System.out.println(gameState);
			}
		}
		
		if(gameState == GameState.Fame) {
			getContentPane().add(GUI.famePanel);
			this.remove(GUI.menuPanel);
			String[] columnName = {"Player Name", "Number Of Atempts"};
			GUI.scoreTable = new JTable(excelHandler.getData(), columnName);
			GUI.scoreTable.setBounds(75, 100, 200, 200);
			GUI.scoreTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 20));
			GUI.scoreTable.setRowHeight(40);
			GUI.scoreTable.getTableHeader().setBackground(new Color(0, 129, 176));
			GUI.scoreTable.getTableHeader().setForeground(Color.WHITE);
			GUI.scoreTable.setAlignmentX(JLabel.CENTER);
			GUI.scoreTable.setFont(new Font("Arial", Font.PLAIN, 18));
			JScrollPane sp = new JScrollPane(GUI.scoreTable);
			sp.setBounds(125, 200, 800, 300);
			GUI.famePanel.add(GUI.returnToTitleButton);
			GUI.famePanel.add(sp);
			GUI.famePanel.setVisible(true);
			while(gameState != GameState.ReturnToTitle) {
				System.out.println("Score");
			}
		}
		
		if(gameState == GameState.Lore) {
			getContentPane().add(GUI.lorePanel);
			this.remove(GUI.menuPanel);
			GUI.lorePanel.add(GUI.returnToTitleButton);
			reader.loreReader(GUI.loreDisplay);
			GUI.lorePanel.setVisible(true);
			while(gameState != GameState.ReturnToTitle) {
				System.out.println(gameState);
			}
		}
		
		if(gameState == GameState.AboutUs) {
			getContentPane().add(GUI.aboutUsPanel);
			this.remove(GUI.menuPanel);
			GUI.aboutUsPanel.add(GUI.returnToTitleButton);
			reader.aboutUsReader(GUI.aboutUsDisplay);
			GUI.aboutUsPanel.setVisible(true);
			while(gameState != GameState.ReturnToTitle) {
				System.out.println(gameState);
			}
		}
		
		if(gameState == GameState.ReturnToTitle) {
			GameMain window = new GameMain();
		}
	}
	public void init(int level, int alreadyAte) { //gameinits all elements
		if(level == 9) {
			Field.createBossField(field, GUI.bossTile, tile);
			GUI.progressBar.setVisible(false);
			boss = new Boss();
			GUI.bossHP.setMaximum(PHASE_1_HP[difficulty]);
		}
		else
			Field.createField(field, level, tile);
		Image.initializeImage();
		Food.foodReset();
		snake = new Snake(alreadyAte, level);
		isMoving = false;
		updateCount = 0;
		GUI.scoreDisplay.setText(String.valueOf(snake.getAlreadyAte()));
		GUI.progressBar.setValue(0); 
		if(currentLevel == 9)
			GUI.changeLevel.setVisible(false);
		else
			GUI.changeLevel.setSelectedIndex(currentLevel);
		if(currentLevel == 9) {
			tile[16][30].setIcon(Image.spawnHole[spriteCount%3+1]);
			field[16][30] = FieldObject.Spawn;
		}
		else {
			tile[9][16].setIcon(Image.spawnHole[spriteCount%3+1]);
			field[9][16] = FieldObject.Spawn;
		}
	}
	
	public void update(Snake snake) throws LineUnavailableException, IOException, UnsupportedAudioFileException, URISyntaxException {
		if(updateCount == 5)
			Food.createFood();
		int check = snake.runSnake(field, tile, isMoving, currentLevel);
		if(currentLevel == 9) {
			if(GUI.bossHP.getValue() == 0 && !filled && boss.getPhase() == 0) {
				if(GUI.bossHP.getValue() <= 2 && boss.getPhase()==0) filled = true;
				boss.phaseChange();
				GUI.bossHP.setMaximum(PHASE_2_HP[difficulty]);
				bossState = BossState.PhaseChange;
				HPFillTimer = new Timer();
				hpFill = 0;
				HPFillTimer.scheduleAtFixedRate(new TimerTask() {
					  @Override
					  public void run() {
						  filled = true;
						  hpFill++;
						  GUI.bossHP.setValue(hpFill);
						if(hpFill == GUI.bossHP.getMaximum())
						{
							filled = false;
							HPFillTimer.cancel();
						}
					  }
					}, 50, 50);
				try {
					audioPlayer.playSE(Sound.PHASE_CHANGE);
				} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
					e.printStackTrace();
				} 
				bossSpriteCount=1;
			}
			if(GUI.bossHP.getValue() <= 5 && boss.getPhase() == 1 && boss.isFirstTimeReconstruct() && !filled) {
					bossSpriteCount=1;
					psychoFusionStop = true;
					bossState = BossState.SkillCasting;
					System.out.println("Psycho");
					if(boss.beamcrt.beamCreate != null) boss.beamcrt.beamCreate.cancel();
					if(boss.beamcrt.beamLockOn != null) boss.beamcrt.beamLockOn.cancel();	
			}
			
			if(GUI.bossHP.getValue() > 5 || boss.getPhase() != 1) {
				boss.blkcrt.cast();
				boss.beamcrt.cast();
				boss.poisoncrt.cast();
				boss.telecrt.cast();
			}
			
			if(GUI.bossHP.getValue() <= 5 && boss.getPhase() == 1) {
				boss.blkcrt.cast();
				boss.beamcrt.cast();
			}
		
		if(snake.getConfusedDuration() != 0) {
			GUI.confusionBar.setVisible(true);
			GUI.confusionBar.setValue(snake.getConfusedDuration());
		} else
			GUI.confusionBar.setVisible(false);
		}
		if(check == 1) {
			JOptionPane.showMessageDialog(null, "You lost!");
			excelHandler.updateExcel(player_name);
			audioPlayer.fadeTheme();
			timer.cancel();
			timer2.cancel();
			HPFillTimer.cancel();
			loopTimer.cancel();
			if(boss.beamcrt.beamCreate != null) boss.beamcrt.beamCreate.cancel();
			if(boss.beamcrt.beamLockOn != null) boss.beamcrt.beamLockOn.cancel();
			this.dispose();
			System.gc();
			GameMain newWindow = new GameMain();
		}
		
		
			if(currentLevel < 8) {
				if(GUI.progressBar.getMaximum() == GUI.progressBar.getValue()) {
				currentLevel++;
				System.out.println("Level " + currentLevel + " reached!");
				resetField(currentLevel, 0);
				GUI.changeLevel.setSelectedIndex(currentLevel);
				isMoving = false;
				}
			}
			else if(currentLevel == 8 && GUI.progressBar.getMaximum() == GUI.progressBar.getValue()) {
				timer.cancel();
				timer2.cancel();
				HPFillTimer.cancel();
				loopTimer.cancel();
				JOptionPane.showMessageDialog(null, "You win!");
				this.dispose();
				System.gc();
				GameMain newWindow = new GameMain();
			}
			else if(snake.getAlreadyAte() == PHASE_1_HP[difficulty] + PHASE_2_HP[difficulty]){
				timer.cancel();
				timer2.cancel();
				HPFillTimer.cancel();
				loopTimer.cancel();
				boss.beamcrt.beamCreate.cancel();
				boss.beamcrt.beamLockOn.cancel();
				JOptionPane.showMessageDialog(null, "You win!");
				this.dispose();
				System.gc();
				GameMain newWindow = new GameMain();
			}
		keyPressed = false;
	}
	
	public void draw() {
		if (!isTimer && currentLevel == 9)
		{		
			try {
				audioPlayer.playSE(Sound.BOSS_DESCEND);
				audioPlayer.playBGM(Sound.PHASE1_THEME);
			} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
				e.printStackTrace();
			} 
			timer.scheduleAtFixedRate(new TimerTask() {
				  @Override
				  public void run() {
					  if(currentLevel == 9) {
						  if (!filled)
							  if(boss.getPhase()==0) 
								  GUI.bossHP.setValue(PHASE_1_HP[difficulty] - snake.getAlreadyAte());
							  else
								  GUI.bossHP.setValue(PHASE_2_HP[difficulty] - snake.getAlreadyAte() + PHASE_1_HP[difficulty]);
						  if (bossState == BossState.Descend) {
								GUI.bossTile.setIcon(Image.bossDescend[bossSpriteCount%17]);
								if (bossSpriteCount == 17) {
									bossState = BossState.Idle;
									bossSpriteCount = 1;
								}
							}
							if (bossState == BossState.Idle)
							{
								if(Snake.head.getLocation()[0] >= 17) GUI.bossTile.setIcon(Image.bossIdle[bossSpriteCount%12+1]);
								else GUI.bossTile.setIcon(Image.bossIdleL[bossSpriteCount%12+1]);
								
								if (!isTimerFilled) {
									GUI.bossTile.add(GUI.bossHP);
									hpFill = 0;
									HPFillTimer.scheduleAtFixedRate(new TimerTask() {
										  @Override
										  public void run() {
											  filled = true;
											  hpFill++;
											  GUI.bossHP.setValue(hpFill);
											if(hpFill == PHASE_1_HP[difficulty])
											{
												filled = false;
												HPFillTimer.cancel();
											}
										  }
										}, 300, 50);
									isTimerFilled = true;
								}
							}
							if (bossState == BossState.NormalBeam){
								if (boss.getPhase()==0)
									if(Snake.head.getLocation()[0] >= 17) GUI.bossTile.setIcon(Image.bossNormalBeam[bossSpriteCount%9+1]);
									else GUI.bossTile.setIcon(Image.bossNormalBeamL[bossSpriteCount%9+1]);
								else 
									GUI.bossTile.setIcon(Image.bossNP[bossSpriteCount%3+1]);
							
								if (bossSpriteCount == 9){
									if (boss.getPhase()==0) 
									{
										bossState=BossState.Idle;
										bossSpriteCount=1;
									} else
									{
										bossState=BossState.IdlePhase2;
										bossSpriteCount=15;
									}
									
									spriteTransition=true;
								}
							}
							if (bossState == BossState.PhaseChange)
							{
								GUI.bossTile.setIcon(Image.bossIdle2nd[bossSpriteCount%20]);
								
								if (bossSpriteCount == 6)
									try {
										audioPlayer.playBGM(Sound.PHASE2_THEME);
									} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
										e.printStackTrace();
									}
								
								if (bossSpriteCount == 13){
									bossState=BossState.IdlePhase2;
								}
							}
							if (bossState == BossState.IdlePhase2)
							{
								GUI.bossTile.setIcon(Image.bossIdle2nd[bossSpriteCount%20]);
								if (bossSpriteCount >= 19)
									bossSpriteCount=13;
							}
							if (bossState == BossState.BossNP){
								GUI.bossTile.setIcon(Image.bossNP[bossSpriteCount%15]);
							
								if (bossSpriteCount > 13){
									bossState=BossState.IdlePhase2;
									bossSpriteCount=16;
									spriteTransition=true;
								}
							}
							if (bossState == BossState.SkillCasting){
								GUI.bossTile.setIcon(Image.skillCasting[bossSpriteCount]);
								if (bossSpriteCount == 1) 
									
									try {
										audioPlayer.playSE(Sound.SKILL_CASTING);
									} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
										e.printStackTrace();
									}
								
								if (bossSpriteCount ==6){
									Timer reconstructTimer = new Timer();
									Timer skillCutInTimer = new Timer();
									skillCutInCount = 11;
									
									bossState=BossState.IdlePhase2;
									bossSpriteCount=16;
									spriteTransition=true;
									
									GUI.skillCutInLabel = new JLabel();
									GUI.skillCutInLabel.setBounds(0, 0, 35 * 35, 20 * 35);
									GUI.skillCutInLabel.setVisible(true);
									GUI.fieldPanel.add(GUI.skillCutInLabel);
									try {
										audioPlayer.playSE(Sound.PSYCHO_FUSION);
									} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
										e.printStackTrace();
									}
									
									skillCutInTimer.scheduleAtFixedRate(new TimerTask() {
										public void run() {
											GUI.fieldLabel.setVisible(false);
											GUI.bossTile.setVisible(false);
											GUI.skillCutInLabel.setHorizontalAlignment(SwingConstants.CENTER);
											GUI.skillCutInLabel.setVerticalAlignment(SwingConstants.CENTER);
											GUI.skillCutInLabel.setIcon(Image.skillCutIn[skillCutInCount]);
											
											skillCutInCount++;
											if(skillCutInCount >= 33) 
											{
												skillCutInTimer.cancel();
												GUI.skillCutInLabel.setVisible(false);
												GUI.fieldLabel.setVisible(true);
												GUI.bossTile.setVisible(true);
												
												reconstructTimer.scheduleAtFixedRate(new TimerTask() {
													public void run() {
														boss.fieldReconstruct();
														if(!GameMain.psychoFusionStop) reconstructTimer.cancel();
													}
												}, 0, 100);
											}
										}
									}, 0, 50);
								}
							}
								
						}
						bossSpriteCount++;
						if (bossSpriteCount > 20) bossSpriteCount = 1;
				  }
				}, 140, 140);
			
			timer2.scheduleAtFixedRate(new TimerTask() {
				  @Override
				  public void run() {
						spriteCount++;
				  }
				}, 100, 100);
			
				isTimer = true;
		}
		
		for(int i = 0; i < 20; i++)
			for(int j = 0; j < 35; j++){
				if(field[i][j] == FieldObject.LockOn)
					if (boss.beamcrt.beamDirection==1)
						tile[i][j].setIcon(Image.horizonalBeamImage[spriteCount%2+1]);
					else
						tile[i][j].setIcon(Image.verticalBeamImage[spriteCount%2+1]);
				if(field[i][j] == FieldObject.LockOnBonus)
					if (boss.beamcrt.beamDirection==0)
						tile[i][j].setIcon(Image.horizonalBeamImage[spriteCount%2+1]);
					else
						tile[i][j].setIcon(Image.verticalBeamImage[spriteCount%2+1]);
				if(field[i][j] == FieldObject.LockOnObstacles)
					tile[i][j].setIcon(Image.obstaclesImage[obstacleSpriteCount]);
				if(field[i][j] == FieldObject.Blank)
					tile[i][j].setIcon(null);
				if(field[i][j] == FieldObject.Snake) {
					if (fieldDirection[i][j] >=0) {
						tile[i][j].setIcon(Image.snakeBodyImage[fieldDirection[i][j]]);
						int x = Snake.snakeLocation.peek().getLocation()[0], y = Snake.snakeLocation.peek().getLocation()[1];
						tile[y][x].setIcon(Image.snakeTailImage[((fieldDirectionTurnPoint[y][x]>=0)?GameMain.fieldDirectionTurnPoint[y][x]:0)]);
					}
				}
				if(field[i][j] == FieldObject.Food) {
					if(foodBlinking) tile[i][j].setIcon(Image.foodImage);
					else tile[i][j].setIcon(Image.foodImage2);
				}
				if (field[i][j] == FieldObject.Obstacles)
					tile[i][j].setIcon(Image.obstaclesImage[1]);
				if(field[i][j] == FieldObject.Confusion) {
					//System.out.println("Confusion location:"+i+" "+j);
					tile[i][j].setIcon(Image.poisonImage[spriteCount%5]);
				}
				if(field[i][j] == FieldObject.Tele) {
					//System.out.println("Tele location:"+i+" "+j);
					tile[i][j].setIcon(Image.telePortal[spriteCount%7]);
				}
				if(field[i][j] == FieldObject.Light) {
					tile[i][j].setIcon(Image.lightPillar);
				}
			}
		if(updateCount % 5 == 0)
			foodBlinking = !foodBlinking; //food blinking
		if(updateCount == 5) {
			if(currentLevel == 9) {
				tile[16][30].setIcon(null);
				field[16][30] = FieldObject.Blank;
			} else {
				tile[9][16].setIcon(null);
				field[9][16] = FieldObject.Blank;
			}
		}
		if(snake.getConfusedDuration() == 0)
			tile[Snake.head.getLocation()[1]][Snake.head.getLocation()[0]].setIcon(Image.snakeHeadImage[snake.getDirection()%5]);
		else
			tile[Snake.head.getLocation()[1]][Snake.head.getLocation()[0]].setIcon(Image.snakeHeadImage[(snake.getDirection() > 1)?snake.getDirection() - 2 : snake.getDirection() + 2]);
		GUI.scoreDisplay.setText(String.valueOf(snake.getAlreadyAte()));
		if(currentLevel != 9) {
			GUI.progressBar.setValue(snake.getAlreadyAte());
			GUI.progressBar.setString(snake.getAlreadyAte()+"/21");
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	// Thiet lap phim dieu khien cho game 
    public void keyPressed(KeyEvent event) {
		char ch = event.getKeyChar();
		if(keyPressed || psychoFusionStop)
			return;
		if ((ch == 'w' || event.getKeyCode() == KeyEvent.VK_UP)&& snake.getDirection() != 3) {
			directionQueue.add(1);
			isMoving = true;
		}
		if ((ch == 'a' || event.getKeyCode() == KeyEvent.VK_LEFT) && snake.getDirection() != 2) {
			directionQueue.add(0);
			isMoving = true;
		}			
		if ((ch == 's' || event.getKeyCode() == KeyEvent.VK_DOWN) && snake.getDirection() != 1) {
			directionQueue.add(3);
			isMoving = true;
		}
		if ((ch == 'd' || event.getKeyCode() == KeyEvent.VK_RIGHT) && snake.getDirection() != 0) {
			directionQueue.add(2);
			isMoving = true;
		}
		
		if(s) {
			snake.setDirection();
			s=false;
		}
		keyPressed = true;
    }

	@Override
	public void keyReleased(KeyEvent event) {
		
	}
	
	// 
	@Override
	public void actionPerformed(ActionEvent e) {	
		if(e.getSource() == GUI.newNormalGameButton) {
			gameState = GameState.Play;
			currentLevel = 0;
		}
		if(e.getSource() == GUI.helpButton) {
			gameState = GameState.Help;
		}
		if(e.getSource() == GUI.loreButton) {
			gameState = GameState.Lore;
		}
		if(e.getSource() == GUI.returnToTitleButton) {
			difficulty = 2;
			gameState = GameState.ReturnToTitle;
			this.dispose();
			System.gc();
			timer.cancel();
			timer2.cancel();
			try {
				audioPlayer.mute_BGM(GUI.soundButton);
				audioPlayer.mute_SE(GUI.soundButton);
			} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e1) {
				e1.printStackTrace();
			}
			
		}
		if(e.getSource() == GUI.newBossGameButton) {
			gameState = GameState.Play;
			currentLevel = 9;
		}
		if(e.getSource() == GUI.fameButton)
			gameState = GameState.Fame;
		if(e.getSource() == GUI.aboutUsButton)
			gameState = GameState.AboutUs;
		if(e.getSource() == GUI.soundButton)
			try {
				audioPlayer.mute_SE(GUI.soundButton);
			} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e1) {
				e1.printStackTrace();
			}
		if(e.getSource() == GUI.casualButton)
			difficulty = 0;
		if(e.getSource() == GUI.normalButton)
			difficulty = 1;
		if(e.getSource() == GUI.soulsButton)
			difficulty = 2;
	}
	
	public void resetField(int level, int alreadyAte) {
		init(level, alreadyAte);
		draw();
		tile[9][16].setIcon(Image.spawnHole[1]);
	}
	
}
