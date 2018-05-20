package wexalian.mods.minetech.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import wexalian.mods.minetech.lib.BlockNames;
import wexalian.mods.minetech.tileentity.TileEntityKineticGenerator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockKineticGenerator extends Block
{
    public BlockKineticGenerator()
    {
        super(Material.ROCK);
        setRegistryName(BlockNames.KINETIC_GENERATOR);
        setUnlocalizedName(BlockNames.KINETIC_GENERATOR);
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }
    
    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state)
    {
        return new TileEntityKineticGenerator();
    }
}
