package wexalian.mods.minetech.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.SlotItemHandler;
import wexalian.mods.minetech.tileentity.TileEntityGrindstone;

import javax.annotation.Nonnull;

public class ContainerGrindstone extends Container
{
    public static final int GRINDSTONE_INV_X = 62;
    public static final int GRINDSTONE_INV_Y = 18;
    public static final int PLAYER_INV_X = 8;
    public static final int PLAYER_INV_Y = 84;
    public static final int PLAYER_HOTBAR_X = 8;
    public static final int PLAYER_HOTBAR_Y = 142;
    public static final int SLOT_SIZE = 18;
    
    public static final int START_GRINDSTONE_INDEX = 0;
    public static final int END_GRINDSTONE_INDEX = 7;
    public static final int START_PLAYER_INV = 8;
    public static final int END_PLAYER_INV = 43;
    
    public static final int PROGRESS_ID = 0;
    
    private TileEntityGrindstone tile;
    private int progress;
    
    public ContainerGrindstone(TileEntityGrindstone tileEntity, EntityPlayer player)
    {
        tile = tileEntity;
        // @formatter:off
        addSlotToContainer(new SlotItemHandler(tile.inventory, 0, GRINDSTONE_INV_X,                 GRINDSTONE_INV_Y));
        addSlotToContainer(new SlotItemHandler(tile.inventory, 1, GRINDSTONE_INV_X + SLOT_SIZE,     GRINDSTONE_INV_Y));
        addSlotToContainer(new SlotItemHandler(tile.inventory, 2, GRINDSTONE_INV_X + SLOT_SIZE * 2, GRINDSTONE_INV_Y));
        addSlotToContainer(new SlotItemHandler(tile.inventory, 3, GRINDSTONE_INV_X,                 GRINDSTONE_INV_Y + SLOT_SIZE));
        addSlotToContainer(new SlotItemHandler(tile.inventory, 4, GRINDSTONE_INV_X + SLOT_SIZE * 2, GRINDSTONE_INV_Y + SLOT_SIZE));
        addSlotToContainer(new SlotItemHandler(tile.inventory, 5, GRINDSTONE_INV_X,                 GRINDSTONE_INV_Y + SLOT_SIZE * 2));
        addSlotToContainer(new SlotItemHandler(tile.inventory, 6, GRINDSTONE_INV_X + SLOT_SIZE,     GRINDSTONE_INV_Y + SLOT_SIZE * 2));
        addSlotToContainer(new SlotItemHandler(tile.inventory, 7, GRINDSTONE_INV_X + SLOT_SIZE * 2, GRINDSTONE_INV_Y + SLOT_SIZE * 2));
        // @formatter:on
        
        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 9; x++)
            {
                addSlotToContainer(new Slot(player.inventory, x + y * 9 + 9, PLAYER_INV_X + x * SLOT_SIZE, PLAYER_INV_Y + y * SLOT_SIZE));
            }
        }
        
        for (int x = 0; x < 9; x++)
        {
            addSlotToContainer(new Slot(player.inventory, x, PLAYER_HOTBAR_X + x * SLOT_SIZE, PLAYER_HOTBAR_Y));
        }
    }
    
    /**
     * Determines whether the player can use this container
     *
     * @param player The player trying to interact with this container
     * @return True if the player can use the container, false if not
     */
    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer player)
    {
        return true;
    }
    
    /**
     * Handle when the stack in slot (index) is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     *
     * @param player The player who shift-clicked the slot
     * @param index  The index of the shift-clicked slot
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
     * Merges provided {@link ItemStack ItemStack} with the first avaliable one in the container/player inventory between startIndex
     * (included) and endIndex (excluded).
     *
     * @param stackToMerge Tthe {@link ItemStack ItemStack} which you want to (try to) merge
     * @param startIndex   The start index of the inventory (included)
     * @param endIndex     The end index of the inventory (excluded)
     * @param reverse      Traverse the inventory from end to start, instead of start to end
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
                
                if (!stackInSlot.isEmpty() && stackInSlot.getItem() == stackToMerge.getItem() && (!stackToMerge.getHasSubtypes() || stackToMerge.getMetadata() == stackInSlot.getMetadata()) && ItemStack.areItemStackTagsEqual(stackToMerge, stackInSlot))
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
                    if (stackToMerge.getCount() > slot.getSlotStackLimit()) slot.putStack(stackToMerge.splitStack(slot.getSlotStackLimit()));
                    else slot.putStack(stackToMerge.splitStack(stackToMerge.getCount()));
                    slot.onSlotChanged();
                    success = true;
                    break;
                }
                
                i += reverse ? -1 : 1;
            }
        }
        return success;
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
            if (progress != tile.getProgress()) listener.sendWindowProperty(this, PROGRESS_ID, tile.getProgress());
        }
        progress = tile.getProgress();
    }
    
    /**
     * Update client-side values send from the server using {@link IContainerListener IContainerListener}s
     *
     * @param id   The property ID
     * @param data The data of the property
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        if (id == PROGRESS_ID) tile.setProgress(data);
    }
}
