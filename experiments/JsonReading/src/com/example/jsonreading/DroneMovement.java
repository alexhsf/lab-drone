package com.example.jsonreading;

public class DroneMovement {
	
	public float x;
	public float y;
	public float z;
	public float r;
	public float t;
	
	public DroneMovement() {
		x = 0;
		y = 0;
		z = 0;
		r = 0;
		t = 0;
	}

	public DroneMovement(float x, float y, float z, float rotation, float timestamp) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = rotation;
		this.t = timestamp;
	}
	
	public String toString() {
		return String.format("x=%1$5.1f y=%2$5.1f z=%3$5.1f r=%4$5.1f t=%5$5.1f\n", x, y, z, r, t);
	}

}
