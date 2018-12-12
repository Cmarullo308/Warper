package me.Warper.main;

import org.bukkit.Material;

public class Warp implements Comparable<Warp> {
	String warpName;
	String worldName;
	double x;
	double y;
	double z;
	float yaw;
	float pitch;
	Material icon;

	public Warp(String warpName, String worldName, double x, double y, double z, float yaw, float pitch,
			Material icon) {
		this.warpName = warpName;
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.icon = icon;
	}

	@Override
	public int compareTo(Warp anotherWarp) {
		return this.warpName.compareTo(anotherWarp.warpName);
	}

}
