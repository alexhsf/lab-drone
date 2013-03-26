package de.yadrone.android;

import com.shigeodayo.ardrone.ARDrone;

public class DroneCommandScheduler {
	
	private ARDrone drone;
	private DroneMovement previousLocation;
	private float verticalSpeed = 3; // in m/s
	private float yawSpeed = 1;      // in m/s
	private float rollSpeed = 2;     // in m/s
	private float spinSpeed = 1.5f;  // degrees/s
	
	public DroneCommandScheduler(ARDrone drone) {
		this.setDrone(drone);
		previousLocation = new DroneMovement();
	}

	public ARDrone getDrone() {
		return drone;
	}

	public void setDrone(ARDrone drone) {
		this.drone = drone;
	}

	// inputs x,y,z,orientation are in ROOM coordinates
	// The output DroneSpeed is in relative speeds (0.01 - 1) in respective direction of the DRONE
	public DroneMovement determineSpeedVector(DroneMovement targetLocation) {
		float deltaX = targetLocation.x - previousLocation.x;
		float deltaY = targetLocation.y - previousLocation.y;
		float deltaZ = targetLocation.z - previousLocation.z;
		float deltaR = targetLocation.r - previousLocation.r;
		float timespan = targetLocation.t;
		
		float speedX = deltaX / timespan;
		float speedY = deltaY / timespan;
		float speedZ = deltaZ / timespan;
		float speedR = deltaR / timespan;
		
		float ratioX = speedX / yawSpeed;
		float ratioY = speedY / rollSpeed;
		float ratioZ = speedZ / verticalSpeed;
		float ratioR = speedR / spinSpeed;
		
		// If the speed in any direction is larger than configured,
		// increase the time span
		if (ratioX > 1 || ratioY > 1 || ratioZ > 1 || ratioR > 1)
		{
			float maxRatio = ratioX;
			if (maxRatio < ratioY) {
				maxRatio = ratioY;
			}
			if (maxRatio < ratioZ) {
				maxRatio = ratioZ;
			}
			if (maxRatio < ratioR) {
				maxRatio = ratioR;
			}
			timespan *= maxRatio;
			
			// And recalculate the speeds
			speedX = deltaX / timespan;
			speedY = deltaY / timespan;
			speedZ = deltaZ / timespan;
			speedR = deltaR / timespan;
		}
		int relativeSpeedX = Math.round(100 * speedX / yawSpeed);
		int relativeSpeedY = Math.round(100 * speedY / rollSpeed);
		int relativeSpeedZ = Math.round(100 * speedZ / verticalSpeed);
		int relativeSpeedR = Math.round(100 * speedR / spinSpeed);
		
		return new DroneMovement(relativeSpeedX, relativeSpeedY, relativeSpeedZ, relativeSpeedR, timespan);
	}

}
