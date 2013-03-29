package com.shigeodayo.ardrone.navdata;

/**
 * @brief Values for the detection type on drone cameras.
 */
public enum VisionTagType {
	/* Deprecated */
	CAD_TYPE_HORIZONTAL,
	/* Deprecated */
	CAD_TYPE_VERTICAL,
	/* Detection of 2D horizontal tags on drone shells */
	CAD_TYPE_VISION,
	/* Detection disabled */
	CAD_TYPE_NONE,
	/* Detects a roundel under the drone */
	CAD_TYPE_COCARDE,
	/* Detects an oriented roundel under the drone */
	CAD_TYPE_ORIENTED_COCARDE,
	/* Detects a uniform stripe on the ground */
	CAD_TYPE_STRIPE,
	/* Detects a roundel in front of the drone */
	CAD_TYPE_H_COCARDE,
	/* Detects an oriented roundel in front of the drone */
	CAD_TYPE_H_ORIENTED_COCARDE,
	/* The drone uses several detections at the same time */
	CAD_TYPE_STRIPE_V,
	/* */
	CAD_TYPE_MULTIPLE_DETECTION_MODE,
	/* Detects a Cap orange and green in front of the drone */
	CAD_TYPE_CAP,
	/* Detects the black and white roundel */
	CAD_TYPE_ORIENTED_COCARDE_BW,
	/* Detects 2nd version of shell/tag in front of the drone */
	CAD_TYPE_VISION_V2,
	/*<! Detect a tower side with the front camera */
	CAD_TYPE_TOWER_SIDE,
	/* Number of possible values for CAD_TYPE */
	CAD_TYPE_NUM;

	public static VisionTagType fromInt(int v) {
		VisionTagType[] values = values();
		if (v < 0 || v > values.length) {
			return null;
		}
		return values[v];
	}
}