package com.wexalian.mods.minetech.container;

import com.wexalian.mods.minetech.container.slot.SlotFilter;
import com.wexalian.mods.minetech.crafting.GrindingRecipe;
import com.wexalian.mods.minetech.tileentity.TileEntityGrindstone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ContainerGrindstone extends Container
{
    private static final int GRINDSTONE_INV_X = 62;
    private static final int GRINDSTONE_INV_Y = 18;
    private static final int PLAYER_INV_X = 8;
    private static final int PLAYER_INV_Y = 84;
    private static final int PLAYER_HOTBAR_X = 8;
    private static final int PLAYER_HOTBAR_Y = 142;
    private static final int SLOT_SIZE = 18;
    
    private static final int START_GRINDSTONE_INDEX = 0;
    private static final int END_GRINDSTONE_INDEX = 7;
    private static final int START_PLAYER_INV = 8;
    private static final int END_PLAYER_INV = 43;
    
    private static final int PROGRESS_ID = 0;
    
    @Nonnull
    private final TileEntityGrindstone tile;
    private int progress;
    
    public ContainerGrindstone(World world, BlockPos pos, EntityPlayer player)
    {
        tile = Objects.requireNonNull((TileEntityGrindstone) world.getTileEntity(pos));
        // @formatter:off
        addSlot(new SlotFilter(tile.getInventory(), 0, GRINDSTONE_INV_X,                 GRINDSTONE_INV_Y                , GrindingRecipe.IS_INGREDIENT));
        addSlot(new SlotFilter(tile.getInventory(), 1, GRINDSTONE_INV_X + SLOT_SIZE,     GRINDSTONE_INV_Y                , GrindingRecipe.IS_INGREDIENT));
        addSlot(new SlotFilter(tile.getInventory(), 2, GRINDSTONE_INV_X + SLOT_SIZE * 2, GRINDSTONE_INV_Y                , GrindingRecipe.IS_INGREDIENT));
        addSlot(new SlotFilter(tile.getInventory(), 3, GRINDSTONE_INV_X,                 GRINDSTONE_INV_Y + SLOT_SIZE    , GrindingRecipe.IS_INGREDIENT));
        addSlot(new SlotFilter(tile.getInventory(), 4, GRINDSTONE_INV_X + SLOT_SIZE * 2, GRINDSTONE_INV_Y + SLOT_SIZE    , GrindingRecipe.IS_INGREDIENT));
        addSlot(new SlotFilter(tile.getInventory(), 5, GRINDSTONE_INV_X,                 GRINDSTONE_INV_Y + SLOT_SIZE * 2, GrindingRecipe.IS_INGREDIENT));
        addSlot(new SlotFilter(tile.getInventory(), 6, GRINDSTONE_INV_X + SLOT_SIZE,     GRINDSTONE_INV_Y + SLOT_SIZE * 2, GrindingRecipe.IS_INGREDIENT));
        addSlot(new SlotFilter(tile.getInventory(), 7, GRINDSTONE_INV_X + SLOT_SIZE * 2, GRINDSTONE_INV_Y + SLOT_SIZE * 2, GrindingRecipe.IS_INGREDIENT));
        // @formatter:on
        
        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 9; x++)
            {
                addSlot(new Slot(player.inventory, x + y * 9 + 9, PLAYER_INV_X + x * SLOT_SIZE, PLAYER_INV_Y + y * SLOT_SIZE));
            }
        }
        
        for (int x = 0; x < 9; x++)
        {
            addSlot(new Slot(player.inventory, x, PLAYER_HOTBAR_X + x * SLOT_SIZE, PLAYER_HOTBAR_Y));
        }
    }
    
    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        
        for (IContainerListener listener : listeners)
        {
            if (progress != tile.getWheelProgress()) listener.sendWindowProperty(this, PROGRESS_ID, tile.getWheelProgress());
        }
        progress = tile.getWheelProgress();
    }
    
    /**
     * Handle when the stack in slot (index) is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     *
     * @param player The player who shift-clicked the slot
     * @param index  The index of the shift-clicked slot
     *
     * @return The (part of the) {@link ItemStack ItemStack} which was moved
     */
    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index)
    {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);
        
        if (slot != null && slot.getHasStack())
        {
            ItemStack stackInSlot = slot.getStack();
            stack = stackInSlot.copy();
            
            if (index <= END_GRINDSTONE_INDEX)
            {
                if (!mergeItemStack(stackInSlot, START_PLAYER_INV, END_PLAYER_INV + 1, true)) return ItemStack.EMPTY;
            }
            else if (index <= END_PLAYER_INV)
            {
                if (!mergeItemStack(stackInSlot, START_GRINDSTONE_INDEX, END_GRINDSTONE_INDEX + 1, false)) return ItemStack.EMPTY;
            }
        }
        return stack;
    }
    
    /**
     * Update client-side values send from the server using {@link IContainerListener IContainerListener}s
     *
     * @param id   The property ID
     * @param data The data of the property
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        if (id == PROGRESS_ID) tile.setProgress(data);
    }
    
    /**
     * Determines whether the player can use this container
     *
     * @param player The player trying to interact with this container
     *
     * @return True if the player can use the container, false if not
     */
    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer player)
    {
        return true;
    }
    
    /**
     * Merges provided {@link ItemStack ItemStack} with the first avaliable one in the container/player inventory between startIndex
     * (included) and endIndex (excluded).
     *
     * @param stackToMerge Tthe {@link ItemStack ItemStack} which you want to (try to) merge
     * @param startIndex   The start index of the inventory (included)
     * @param endIndex     The end index of the inventory (excluded)
     * @param reverse      Traverse the inventory from end to start, instead of start to end
     *
     * @return True if the merge was (partially) successful, false otherwise
     */
    @Override
    protected boolean mergeItemStack(@Nonnull ItemStack stackToMerge, int startIndex, int endIndex, boolean reverse)
    {
        boolean success = false;
        int i = reverse ? endIndex - 1 : startIndex;
        
        if (stackToMerge.isStackable())
        {
            while (!stackToMerge.isEmpty())
            {
                if (reverse ? i < startIndex : i >= endIndex) break;
                
                Slot slot = inventorySlots.get(i);
                ItemStack stackInSlot = slot.getStack();
                
                if (!stackInSlot.isEmpty() && stackInSlot.getItem() == stackToMerge.getItem() && ItemStack.areItemStackTagsEqual(stackToMerge, stackInSlot))
                {
                    int stackSize = stackInSlot.getCount() + stackToMerge.getCount();
                    int maxSize = Math.min(slot.getSlotStackLimit(), stackToMerge.getMaxStackSize());
                    
                    if (stackSize <= maxSize)
                    {
                        stackToMerge.setCount(0);
                        stackInSlot.setCount(stackSize);
                        slot.onSlotChanged();
                        success = true;
                    }
                    else if (stackInSlot.getCount() < maxSize)
                    {
                        stackToMerge.shrink(maxSize - stackInSlot.getCount());
                        stackInSlot.setCount(maxSize);
                        slot.onSlotChanged();
                        success = true;
                    }
                }
                
                i += reverse ? -1 : 1;
            }
        }
        
        if (!stackToMerge.isEmpty())
        {
            i = reverse ? endIndex - 1 : startIndex;
            
            while (true)
            {
                if (reverse ? i < startIndex : i >= endIndex) break;
                
                Slot slot = inventorySlots.get(i);
                ItemStack stackInSlot = slot.getStack();
                
                if (stackInSlot.isEmpty() && slot.isItemValid(stackToMerge))
                {
                    if (stackToMerge.getCount() > slot.getSlotStackLimit()) slot.putStack(stackToMerge.split(slot.getSlotStackLimit()));
                    else slot.putStack(stackToMerge.split(stackToMerge.getCount()));
                    slot.onSlotChanged();
                    success = true;
                    break;
                }
                
                i += reverse ? -1 : 1;
            }
        }
        return success;
    }
}
