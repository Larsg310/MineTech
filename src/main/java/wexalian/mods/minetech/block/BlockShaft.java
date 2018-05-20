package wexalian.mods.minetech.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wexalian.mods.minetech.lib.BlockNames;
import wexalian.mods.minetech.tileentity.TileEntityShaft;
import wexalian.mods.minetech.util.WorldUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockShaft extends Block
{
    private static final AxisAlignedBB[] BOXES = new AxisAlignedBB[]{new AxisAlignedBB(0, 6 / 16D, 6 / 16D, 1, 10 / 16D, 10 / 16D), new AxisAlignedBB(6 / 16D, 0, 6 / 16D, 10 / 16D, 1, 10 / 16D), new AxisAlignedBB(6 / 16D, 6 / 16D, 0, 10 / 16D, 10 / 16D, 1)};
    
    public BlockShaft()
    {
        super(Material.ROCK);
        setRegistryName(BlockNames.SHAFT);
        setUnlocalizedName(BlockNames.SHAFT);
        setDefaultState(getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y));
        
        MinecraftForge.EVENT_BUS.register(this);
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
        return new TileEntityShaft();
    }
    
    @Nonnull
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer.Builder(this).add(BlockRotatedPillar.AXIS).build();
    }
    
    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.values()[meta]);
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(BlockRotatedPillar.AXIS).ordinal();
    }
    
    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BOXES[state.getValue(BlockRotatedPillar.AXIS).ordinal()];
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullBlock(IBlockState state)
    {
        return false;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    
    @Nonnull
    @Override
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer, @Nullable EnumHand hand)
    {
        return getDefaultState().withProperty(BlockRotatedPillar.AXIS, facing.getAxis());
    }
    
    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onDrawBlockHighlight(DrawBlockHighlightEvent event)
    {
        RayTraceResult hit = event.getTarget();
        if (hit == null) return;
        if (hit.typeOfHit != RayTraceResult.Type.BLOCK) return;
        EntityPlayer player = event.getPlayer();
        IBlockState state = player.world.getBlockState(hit.getBlockPos());
        if (state.getBlock() != this) return;
        TileEntityShaft te = WorldUtil.getTileEntity(player.world, hit.getBlockPos());
        
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(2.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        
        float partialTicks = event.getPartialTicks();
        double offX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks - hit.getBlockPos().getX();
        double offY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks - hit.getBlockPos().getY();
        double offZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks - hit.getBlockPos().getZ();
        
        GlStateManager.translate(0.5 - offX, 0.5 - offY, 0.5 - offZ);
        Vec3i axis = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, te.getRotationAxis()).getDirectionVec();
        GlStateManager.rotate(te.getAngle(partialTicks), axis.getX(), axis.getY(), axis.getZ());
        GlStateManager.translate(-0.5, -0.5, -0.5);
        RenderGlobal.drawSelectionBoundingBox(BOXES[state.getValue(BlockRotatedPillar.AXIS).ordinal()].grow(0.002), 0.0F, 0.0F, 0.0F, 0.4F);
        
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        
        event.setCanceled(true);
    }
    
}
