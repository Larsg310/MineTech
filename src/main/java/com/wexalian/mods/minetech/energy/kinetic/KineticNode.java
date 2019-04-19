package com.wexalian.mods.minetech.energy.kinetic;

import com.wexalian.mods.minetech.api.energy.kinetic.IKineticNode;
import com.wexalian.mods.minetech.api.network.INetworkNodeHost;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class KineticNode implements IKineticNode
{
    @Nonnull
    private UUID uuid = UUID.randomUUID();
    
    @Nonnull
    private INetworkNodeHost<IKineticNode, Float> host;
    
    private float torque;
    private float velocity;
    private float power;
    private float angle;
    private float inertia;
    private float appliedPower;
    private float consumedPower;
    private float ratio;
    
    private boolean isRemote;
    private boolean visited;
    
    public KineticNode(@Nonnull INetworkNodeHost<IKineticNode, Float> host, float inertia)
    {
        this.host = host;
        this.inertia = inertia;
    }
    
    @Override
    public void update(float torque, float velocity, float power)
    {
        this.torque = torque;
        this.velocity = velocity;
        this.power = power;
        this.angle = angle + velocity;
    }
    
    @Override
    public float getAngle()
    {
        return angle;
    }
    
    @Override
    public float getVelocity()
    {
        return velocity;
    }
    
    @Override
    public float getConsumedPower()
    {
        return consumedPower;
    }
    
    public void setConsumedPower(float consumedPower)
    {
        this.consumedPower = consumedPower;
    }
    
    @Override
    public float getAppliedPower()
    {
        return appliedPower;
    }
    
    public void setAppliedPower(float appliedPower)
    {
        this.appliedPower = appliedPower;
    }
    
    @Override
    public float getInertia()
    {
        return inertia;
    }
    
    @Override
    public float getPower()
    {
        return power;
    }
    
    @Override
    public float getTorque()
    {
        return torque;
    }
    
    @Override
    public UUID getNodeId()
    {
        return uuid;
    }
    
    @Override
    public void setNodeId(UUID nodeId)
    {
        this.uuid = nodeId;
    }
    
    @Override
    public void markVisited()
    {
        this.visited = true;
    }
    
    @Override
    public void resetVisitState()
    {
        this.visited = false;
    }
    
    @Nonnull
    @Override
    public INetworkNodeHost<IKineticNode, Float> getHost()
    {
        return host;
    }
    
    @Override
    public Float getValue()
    {
        return ratio;
    }
    
    @Override
    public void setValue(@Nullable Float value)
    {
        this.ratio = value == null ? 0F : value;
    }
    
    @Override
    public boolean isRemote()
    {
        return isRemote;
    }
    
    @Override
    public void setRemote(boolean isRemote)
    {
        this.isRemote = isRemote;
    }
    
    @Override
    public boolean isVisited()
    {
        return visited;
    }
    
    @Override
    public String toString()
    {
        return "KineticNode{" + "torque=" + torque + ", velocity=" + velocity + ", power=" + power + ", angle=" + angle + ", ratio=" + ratio + '}';
    }
}
