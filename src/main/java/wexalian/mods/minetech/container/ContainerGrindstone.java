package wexalian.mods.minetech.container;

import wexalian.mods.minetech.tileentity.TileEntityGrindstone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

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
    
    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer playerIn)
    {
        return true;
    }
    
    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
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
    
    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        
        for (IContainerListener listener : listeners)
        {
            if (progress != tile.getProgress()) listener.sendWindowProperty(this, PROGRESS_ID, progress);
        }
        progress = tile.getProgress();
    }
    
    @Override
    public void updateProgressBar(int id, int data)
    {
        if (id == PROGRESS_ID) tile.setProgress(data);
    }
}
