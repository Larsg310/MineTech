package wexalian.mods.minetech.api.capabilities.mechanical;

public class MechanicalEnergyHandler implements IMechanicalEnergy
{
    private int rpm;
    private int torque;
    
    @Override
    public void setRMP(int rpm)
    {
        this.rpm = rpm;
    }
    
    @Override
    public int getRPM()
    {
        return rpm;
    }
    
    @Override
    public void setTorque(int torque)
    {
        this.torque = torque;
    }
    
    @Override
    public int getTorque()
    {
        return torque;
    }
}
