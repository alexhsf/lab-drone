package com.shigeodayo.ardrone.command;

public class ConfigureCommand extends ATCommand {
	protected String name;
	protected String value;

	public ConfigureCommand(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public ConfigureCommand(String name, int value) {
		this(name, String.valueOf(value));
	}

	public ConfigureCommand(String name, long l) {
		this(name, String.valueOf(l));
	}
	
	public ConfigureCommand(String name, double d) {
		this(name, Double.doubleToLongBits(d));
	}
	
	public ConfigureCommand(String name, boolean b) {
		this(name, String.valueOf(b));
	}

	@Override
	protected String getID() {
		return "CONFIG";
	}

	@Override
	protected Object[] getParameters() {
		return new Object[] { name, value };
	}
}
