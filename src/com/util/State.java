package com.util;

public class State {
	public static enum BossState {
		Descend, Idle, NormalBeam, PhaseChange, IdlePhase2, BossNP, SkillCasting; 
	}
	
	public static enum GameState {
		Play, Help, Lore, ReturnToTitle, Menu, Fame, AboutUs;
	}
	
	public static enum FieldObject {
		Obstacles, Blank, Boss, Beam, LockOn, Confusion, LockOnBonus, LockOnObstacles, Snake, Food, Tele, Spawn, Light;
	}
}
