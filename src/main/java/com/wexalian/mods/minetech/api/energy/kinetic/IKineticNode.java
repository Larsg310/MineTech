package com.wexalian.mods.minetech.api.energy.kinetic;

import com.wexalian.mods.minetech.api.network.INetworkNode;

public interface IKineticNode extends INetworkNode<IKineticNode, Float>
{
    void update(float torque, float velocity, float power);
    
    default float getAngle(float partialTicks)
    {
        return getAngle() + getVelocity() * partialTicks;
    }
    
    float getAngle();
    
    float getVelocity();
    
    float getAppliedPower();
    
    float getConsumedPower();
    
    float getInertia();
    
    float getPower();
    
    float getTorque();
}
