package com.example.jsonreading;

import java.util.List;

import com.shigeodayo.ardrone.command.DroneCommand;

public interface IFlightPlanReader {

	List<DroneCommand> getFlightPlan(String filename);

}
