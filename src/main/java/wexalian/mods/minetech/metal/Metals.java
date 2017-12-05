package wexalian.mods.minetech.metal;

public enum Metals
{
    IRON("iron"),
    GOLD("gold");
    
    private String name;
    
    Metals(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return name;
    }
}
