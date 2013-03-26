package de.yadrone.android;

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
}
