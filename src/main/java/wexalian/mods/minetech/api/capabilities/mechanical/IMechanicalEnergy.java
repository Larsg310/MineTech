package wexalian.mods.minetech.api.capabilities.mechanical;

public interface IMechanicalEnergy
{
    void setRPM(int rpm);
    
    int getRPM();
    
    void setTorque(int torque);
    
    int getTorque();
}
