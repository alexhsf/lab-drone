package com.shigeodayo.ardrone.command;

public class FlatTrimCommand extends ATCommand
{
    @Override
    protected String getID()
    {
        return "FTRIM";
    }

    @Override
    protected Object[] getParameters()
    {
        return new Object[] {};
    }
}
