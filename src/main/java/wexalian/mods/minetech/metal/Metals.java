package wexalian.mods.minetech.metal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wexalian.mods.minetech.lib.Reference;

import javax.annotation.Nonnull;

public enum Metals
{
    IRON("iron"),
    GOLD("gold");

    private String name;
    private int color;

    Metals(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public int getColor()
    {
        return color;
    }

    @SideOnly(Side.CLIENT)
    public static class Reloader implements IResourceManagerReloadListener
    {
        public static void register()
        {
            ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(new Reloader());
        }

        @Override
        public void onResourceManagerReload(@Nonnull IResourceManager manager)
        {
            for (Metals metal : Metals.values())
            {
                String hexString = I18n.format(Reference.MOD_ID + ":metal." + metal.getName() + ".color");
                metal.color = Integer.decode(hexString);
            }
        }
    }
}
