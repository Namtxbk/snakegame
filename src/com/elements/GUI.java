package com.elements;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.handler.ExcelReader;
import com.util.Image;

public class GUI {
	public static JButton returnToTitleButton, fameButton, scoreDisplay ,newNormalGameButton, newBossGameButton, helpButton, loreButton, soundButton, aboutUsButton, casualButton, normalButton, soulsButton;
	public static JPanel fieldPanel, menuPanel, helpPanel, lorePanel, aboutUsPanel, famePanel, scoreDisplayContainer;
	public static JLabel bossTile, menuLabel, fieldLabel, helpDisplay, aboutUsDisplay, loreDisplay, skillCutInLabel;
	public static JProgressBar progressBar, confusionBar, bossHP; // Them thanh trang thai 
	public static JComboBox<String> changeLevel = new JComboBox<String>();
	public static JScrollPane helpScrollPane, loreScrollPane, aboutUsScrollPane; 
	public static JTable scoreTable;
	
	public static void guiInit() {
		//FIELD
		//creates boss's GameMain.tile
		bossTile = new JLabel();
		bossTile.setOpaque(true);
		bossTile.setVisible(false);
		bossTile.setHorizontalAlignment(JLabel.CENTER);
		bossTile.setVerticalAlignment(JLabel.CENTER);
		bossTile.setBounds(15*30,8*30,30*5,30*5);
		bossTile.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
		//creates field's background
		fieldLabel = new JLabel(new ImageIcon(GUI.class.getResource("/image/Other/fieldBackground.png")));
		fieldLabel.setBounds(0, 0, 35 * 30, 20 * 30);
		//creates field's container
		fieldPanel = new JPanel();
		fieldPanel.setLayout(null);
		fieldPanel.setBounds(0, 0, 35 * 30, 20 * 30);
		fieldPanel.setVisible(false);
		fieldPanel.add(fieldLabel);
		fieldPanel.add(bossTile);
				
		//creates field's GameMain.tiles
		for(int i = 0; i < 20; i++)
			for(int j = 0; j < 35; j++) {
				GameMain.fieldDirection[i][j]=-1;
				GameMain.fieldDirectionTurnPoint[i][j]=-1;
				GameMain.tile[i][j] = new JLabel("");
				GameMain.tile[i][j].setBounds(j*30, i*30, 30, 30);
				GameMain.tile[i][j].setBackground(Color.LIGHT_GRAY);
				GameMain.tile[i][j].setOpaque(false);
				GameMain.tile[i][j].setHorizontalAlignment(JLabel.CENTER);
				GameMain.tile[i][j].setVerticalAlignment(JLabel.CENTER);
				fieldLabel.add(GameMain.tile[i][j]);	
			}
				 
		//SCORE AND STATUS
		//creates score-display button
		scoreDisplay = new JButton();
		scoreDisplay.setFont(new Font("Arial", 1, 15));
		scoreDisplay.setBackground(Color.white);
		scoreDisplay.setEnabled(false);
		//creates progress bar
		progressBar = new JProgressBar(0, 21);
		progressBar.setStringPainted(true);
		bossHP = new JProgressBar(0, 0);
		bossHP.setForeground(Color.RED);
		bossHP.setBounds(20, 20, 110, 15);
		//creates confusion status bar
		confusionBar = new JProgressBar(0, 30);
		confusionBar.setString("Confusion");
		confusionBar.setStringPainted(true);
		confusionBar.setVisible(false);
		//creates level-change box
		for(int i = 0; i < 9; i++)
			changeLevel.addItem("Level " + Integer.toString(i+1));
		changeLevel.setEnabled(false);
		//creates score and status' container
		scoreDisplayContainer = new JPanel();
		scoreDisplayContainer.setBackground(Color.WHITE);
		scoreDisplayContainer.add(scoreDisplay);
		scoreDisplayContainer.add(progressBar);
		scoreDisplayContainer.add(changeLevel);
		scoreDisplayContainer.add(confusionBar);
		scoreDisplayContainer.setBounds(0, 600, 35*30+15, 70);
		scoreDisplayContainer.setVisible(false);
		scoreDisplayContainer.setOpaque(true);
				
		//HELP-DISPLAY
		//creates help-display label
		helpDisplay = new JLabel();
		helpDisplay.setVerticalAlignment(JLabel.TOP);
		//creates scroll bar of help-display window
		helpScrollPane = new JScrollPane(helpDisplay, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		helpScrollPane.setPreferredSize(new Dimension(35*30+15-150, 20*30-20));
		helpScrollPane.setBounds(75, 50, 35*30+15-150, 20*30-20);
		//creates help-display container
		helpPanel = new JPanel();
		helpPanel.setVisible(false);
		helpPanel.setLayout(null);
		helpPanel.setBounds(0, 0, 35*30+15, 20*30+80);
		helpPanel.add(helpScrollPane);
		famePanel = new JPanel();
		famePanel.setVisible(false);
		famePanel.setLayout(null);
		famePanel.setBounds(0, 0, 35*30+15, 20*30+80);
		//LORE-DISPLAY
		//creates lore-display label
		loreDisplay = new JLabel();
		loreDisplay.setVerticalAlignment(JLabel.TOP);
		//creates scroll bar of lore-display window
		loreScrollPane = new JScrollPane(loreDisplay, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		loreScrollPane.setPreferredSize(new Dimension(35*30+15-150, 20*30-20));
		loreScrollPane.setBounds(75, 50, 35*30+15-150, 20*30-20);
		//creates lore-display container
		lorePanel = new JPanel();
		lorePanel.setVisible(false);
		lorePanel.setLayout(null);
		lorePanel.setBounds(0, 0, 35*30+15, 20*30+80);
		lorePanel.add(loreScrollPane);
		
		//ABOUT-US-DISPLAY
		//creates about-us-display label
		aboutUsDisplay = new JLabel();
		aboutUsDisplay.setVerticalAlignment(JLabel.TOP);
		//creates scroll bar of about-us-display window
		aboutUsScrollPane = new JScrollPane(aboutUsDisplay, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		aboutUsScrollPane.setPreferredSize(new Dimension(35*30+15-150, 20*30-20));
		aboutUsScrollPane.setBounds(75, 50, 35*30+15-150, 20*30-20);
		//creates about-us-display container
		aboutUsPanel = new JPanel();
		aboutUsPanel.setVisible(false);
		aboutUsPanel.setLayout(null);
		aboutUsPanel.setBounds(0, 0, 35*30+15, 20*30+80);
		aboutUsPanel.add(aboutUsScrollPane);
				
		//MAIN-MENU
		//creates return-to-title button
		returnToTitleButton = new JButton("Return To Title");
		returnToTitleButton.setFont(new Font("Arial", 1, 15));
		returnToTitleButton.setBackground(Color.white);
		returnToTitleButton.setBounds(450, 20, 165, 30);
		//creates new-normal-game button
		newNormalGameButton = new JButton("New Normal Game");
		newNormalGameButton.setFont(new Font("Arial", 1, 15));
		newNormalGameButton.setBackground(Color.white);
		newNormalGameButton.setBounds(430, 220, 205, 40);
		//creates new-boss-game button
		newBossGameButton = new JButton("New Boss Game");
		newBossGameButton.setFont(new Font("Arial", 1, 15));
		newBossGameButton.setBackground(Color.white);
		newBossGameButton.setBounds(430, 270, 205, 40);
		//creates mute button
		soundButton = new JButton(Image.unMuteButton);
		soundButton.setOpaque(false);
		soundButton.setBounds(0,0,40,40);
		//creates help button
		helpButton = new JButton("Help");
		helpButton.setFont(new Font("Arial", 1, 15));
		helpButton.setBackground(Color.white);
		helpButton.setBounds(430, 320, 205, 40);
		//creates lore button
		loreButton = new JButton("Lore");
		loreButton.setFont(new Font("Arial", 1, 15));
		loreButton.setBackground(Color.white);
		loreButton.setBounds(430, 370, 205, 40);
		//creates fame button
		fameButton = new JButton("House Of Fame");
		fameButton.setFont(new Font("Arial", 1, 15));
		fameButton.setBackground(Color.white);
		fameButton.setBounds(430, 420, 205, 40);
		//create about us button
		aboutUsButton = new JButton("About Us");
		aboutUsButton.setFont(new Font("Arial", 1, 15));
		aboutUsButton.setBackground(Color.white);
		aboutUsButton.setBounds(430, 470, 205, 40);
		//creates casual button
		casualButton = new JButton("Casual");
		casualButton.setFont(new Font("Arial", 1, 15));
		casualButton.setBackground(Color.white);
		casualButton.setBounds(430, 220, 205, 40);
		//creates normal button
		normalButton = new JButton("Normal");
		normalButton.setFont(new Font("Arial", 1, 15));
		normalButton.setBackground(Color.white);
		normalButton.setBounds(430, 270, 205, 40);
		//creates help button
		soulsButton = new JButton("Souls");
		soulsButton.setFont(new Font("Arial", 1, 15));
		soulsButton.setBackground(Color.white);
		soulsButton.setBounds(430, 320, 205, 40);
		//creates main menu's container
		menuLabel = new JLabel(new ImageIcon(GUI.class.getResource("/image/Other/menuBackground.png")));
		menuLabel.setForeground(Color.WHITE);
		menuLabel.setBackground(Color.WHITE);
		menuLabel.setLayout(null);
		menuLabel.setBounds(0, 0, 35*30+15, 20*30+80);
		menuLabel.add(newNormalGameButton);
		menuLabel.add(newBossGameButton);
		menuLabel.add(helpButton);
		menuLabel.add(loreButton);
		menuLabel.add(aboutUsButton);
		menuLabel.add(fameButton);
		menuPanel = new JPanel();
		menuPanel.add(menuLabel);
		menuPanel.setLayout(null);
		menuPanel.setBounds(0, 0, 35*30+15, 20*30+80);
	}
	
	public static void addListener(GameMain window) {
		returnToTitleButton.addActionListener(window);
		newNormalGameButton.addActionListener(window);
		soulsButton.addActionListener(window);
		normalButton.addActionListener(window);
		casualButton.addActionListener(window);
		aboutUsButton.addActionListener(window);
		soundButton.addActionListener(window);
		helpButton.addActionListener(window);
		fameButton.addActionListener(window);
		loreButton.addActionListener(window);
		newBossGameButton.addActionListener(window);
	}
}
