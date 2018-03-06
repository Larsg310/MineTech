package wexalian.mods.minetech.item;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import wexalian.mods.minetech.init.ModItems;
import wexalian.mods.minetech.lib.ItemNames;
import wexalian.mods.minetech.lib.Reference;
import wexalian.mods.minetech.metal.Metals;
import wexalian.mods.minetech.util.InventoryUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemPan extends ItemBase
{
    public static final String NBT_KEY_TYPE = Reference.MOD_ID + ":type";
    public static final String NBT_KEY_PROGRESS = Reference.MOD_ID + ":progress";
    
    public static final String TOOLTIP_KEY_PROGRESS = "tooltip.minetech:progress";
    public static final String TOOLTIP_KEY_PANNING = "tooltip.minetech:panning";
    
    public static int START_PROGRESS = 4;
    
    public ItemPan()
    {
        setCreativeTab(CreativeTabs.TOOLS);
        setRegistryName(ItemNames.PAN);
        setUnlocalizedName(ItemNames.PAN);
    }
    
    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand)
    {
        RayTraceResult trace = rayTrace(world, player, true);
        
        ItemStack heldStack = player.getHeldItem(hand);
        if (trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            IBlockState state = world.getBlockState(trace.getBlockPos());
            
            if ((state.getBlock() == Blocks.WATER || state.getBlock() == Blocks.FLOWING_WATER) && state.getValue(BlockLiquid.LEVEL) == 0)
            {
                NBTTagCompound tag = heldStack.getTagCompound();
                if (tag != null && tag.hasKey(NBT_KEY_PROGRESS) && tag.hasKey(NBT_KEY_TYPE))
                {
                    int progress = tag.getInteger(NBT_KEY_PROGRESS);
                    
                    if (progress > 0)
                    {
                        progress--;
                        if (progress == 0)
                        {
                            int type = tag.getInteger(NBT_KEY_TYPE);
                            Metals metal = Metals.values()[type];
                            if (!world.isRemote) ItemHandlerHelper.giveItemToPlayer(player, ItemOreDust.getFromMetal(metal));
                            tag.removeTag(NBT_KEY_TYPE);
                        }
                        tag.setInteger(NBT_KEY_PROGRESS, progress);
                        
                        heldStack.setTagCompound(tag);
                        return ActionResult.newResult(EnumActionResult.SUCCESS, heldStack);
                    }
                }
                else
                {
                    ItemStack stack = InventoryUtil.findItemStack(player.inventory, s -> s.getItem() == ModItems.DIRTY_ORE_DUST);
                    if (!stack.isEmpty())
                    {
                        if (tag == null) tag = new NBTTagCompound();
                        
                        Metals metal = ItemDirtyOreDust.getMetalFromStack(stack);
                        
                        tag.setInteger(NBT_KEY_TYPE, metal.ordinal());
                        tag.setInteger(NBT_KEY_PROGRESS, START_PROGRESS);
                        
                        if (!player.isCreative()) stack.shrink(1);
                        
                        heldStack.setTagCompound(tag);
                        
                        return ActionResult.newResult(EnumActionResult.SUCCESS, heldStack);
                    }
                }
            }
        }
        
        return ActionResult.newResult(EnumActionResult.PASS, heldStack);
    }
    
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
    {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null && tag.hasKey(NBT_KEY_TYPE) && tag.hasKey(NBT_KEY_PROGRESS))
        {
            int type = tag.getInteger(NBT_KEY_TYPE);
            tooltip.add(I18n.format(TOOLTIP_KEY_PANNING, ModItems.DIRTY_ORE_DUST.getItemStackDisplayName(ItemDirtyOreDust.getFromMetal(Metals.values()[type]))));
            
            int progress = tag.getInteger(NBT_KEY_PROGRESS);
            tooltip.add(I18n.format(TOOLTIP_KEY_PROGRESS, (START_PROGRESS - progress) * 100 / START_PROGRESS));
        }
    }
}
