package com.example.jsonreading;

import java.util.List;

public interface IFlightPlanReader {

	List<DroneCommand> getFlightPlan(String filename);

}
