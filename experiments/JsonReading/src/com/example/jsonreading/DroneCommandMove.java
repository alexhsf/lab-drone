package com.example.jsonreading;

public class DroneCommandMove extends DroneCommand {

	private DroneMovement targetLocation; // in the room

	DroneCommandMove(float x, float y, float z, float orientation, float timespan) {
		targetLocation = new DroneMovement(x, y, z, orientation, timespan);
	}

	public String toString() {
		return targetLocation.toString();
	}
}
