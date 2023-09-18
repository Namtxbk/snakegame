package com.main;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.elements.GameMain;

public class SnakeMain {
	public static void main(String[] args) throws LineUnavailableException, IOException, UnsupportedAudioFileException, URISyntaxException {
		Thread thread = new Thread() {
			GameMain window = new GameMain();
		};
		thread.setPriority((int)(Thread.MAX_PRIORITY*0.8));
		thread.start();
	}
}
