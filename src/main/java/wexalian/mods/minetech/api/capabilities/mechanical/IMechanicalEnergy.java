package wexalian.mods.minetech.api.capabilities.mechanical;

public interface IMechanicalEnergy
{
    void setRMP(int rpm);
    
    int getRPM();
    
    void setTorque(int torque);
    
    int getTorque();
}
