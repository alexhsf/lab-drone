package com.shigeodayo.ardrone.command;


/* 
 * Very little is documented about the CTRL command
 * From https://github.com/bklang/ARbDrone/wiki/UndocumentedCommands
 * 
 * NO_CONTROL_MODE = 0,        - Doing nothing
 * ARDRONE_UPDATE_CONTROL_MODE -  Deprecated - Ardrone software update reception (update is done next run)
 *                                After event completion, card should power off
 * PIC_UPDATE_CONTROL_MODE,     - Ardrone PIC software update reception (update is done next run)
 *                              - After event completion, card should power off
 * LOGS_GET_CONTROL_MODE,       - Send previous run's logs
 * CFG_GET_CONTROL_MODE,        - Send active configuration file to a client through the 'control' socket UDP 5559
 * ACK_CONTROL_MODE,            - Reset command mask in navdata
 * CUSTOM_CFG_GET_CONTROL_MODE  - Requests the list of custom configuration IDs
 */

public class ControlCommand extends ATCommand
{
    protected int arg1;
    protected int arg2;

    public ControlCommand(int arg1, int arg2)
    {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    protected String getID()
    {
        return "CTRL";
    }

    @Override
    protected Object[] getParameters()
    {
        return new Object[] { arg1, arg2 };
    }
}
