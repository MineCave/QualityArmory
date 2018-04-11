package me.zombie_striker.qg.guns.utils;

public enum WeaponSounds {
	GUN_SMALL("bulletsmall"),GUN_MEDIUM("bulletmedium"),GUN_BIG("bulletbig"),GUN_AUTO("bulletauto"),
	RELOAD_BULLET("reloadbullet"),RELOAD_MAG_IN("reloadmagin"),RELOAD_MAG_OUT("reloadmagout"),RELOAD_BOLT("reloadbolt"),
	
	WARHEAD_EXPLODE("warheadexplode"),WARHEAD_LAUNCH("warheadlaunch"),LAZERSHOOT("bulletlazer"),
	
	DEFAULT("hurt");
	
	private String soundname;
	
	private WeaponSounds(String s) {
		this.soundname= s;
	}
	public String getName() {
		return soundname;
	}
	public static WeaponSounds getByName(String name) {
		for(WeaponSounds ws : WeaponSounds.values()) {
			if(ws.getName().equals(name))
				return ws;
		}
		return WeaponSounds.DEFAULT;
	}
}
