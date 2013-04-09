package com.example.jsonreading;

public class MyDroneCommandMove extends MyDroneCommand {

	private DroneMovement targetLocation; // in the room

	MyDroneCommandMove(float x, float y, float z, float orientation, float timespan) {
		targetLocation = new DroneMovement(x, y, z, orientation, timespan);
	}

	public String toString() {
		return targetLocation.toString();
	}
}
