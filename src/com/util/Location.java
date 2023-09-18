package com.util;

public class Location {
	int xSnake;
	int ySnake;
	
	public Location() {
		
	}
	
	public Location(int xSnake, int ySnake) {
		this.xSnake = xSnake;
		this.ySnake = ySnake;
	}

	public int[] getLocation() {
		int[] a = new int[2];
		a[0]=xSnake;
		a[1]=ySnake;
		return a;
	}
	
	public int[] distance(Location location) {
		int[] a = new int[2];
		a[0] = Math.min(Math.abs(location.getLocation()[0] - this.getLocation()[0]), 35-Math.abs(location.getLocation()[0] - this.getLocation()[0]));
		a[1] = Math.min(Math.abs(location.getLocation()[1] - this.getLocation()[1]), 35-Math.abs(location.getLocation()[1] - this.getLocation()[1]));
		return a;
	}
}
