package com.wexalian.mods.minetech.multiblock;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.wexalian.mods.minetech.MineTechMod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import static com.wexalian.mods.minetech.multiblock.MultiblockCompiler.*;

public class MultiblockResourceManager implements ISelectiveResourceReloadListener
{
    public static final IResourceType MULTIBLOCK = new IResourceType() {};
    
    public static final String PATH = "multiblocks";
    public static final int PATH_PREFIX_LENGTH = (PATH + "/").length();
    public static final String EXTENSION = ".json";
    public static final int EXTENSION_LENGTH = EXTENSION.length();
    
    private MultiblockResourceManager() {}
    
    private static final Gson GSON = (new GsonBuilder()).registerTypeHierarchyAdapter(BlockPos.class, DESERIALIZER_BLOCKPOS)//
                                                        .registerTypeHierarchyAdapter(IBlockState.class, DESERIALIZER_STATE)//
                                                        .registerTypeHierarchyAdapter(Pair.class, DESERIALIZER_PAIR)//
                                                        .create();
    
    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager, @Nonnull Predicate<IResourceType> filter)
    {
        if (filter.test(MULTIBLOCK))
        {
            MultiblockManager.INSTANCE.clear();
            Map<ResourceLocation, MultiblockCompiler> multiblockBuilders = getMultiblockBuilders(resourceManager);
            Map<ResourceLocation, Multiblock> multiblocks = buildMultiblocks(multiblockBuilders);
            MultiblockManager.INSTANCE.addMultiblocks(multiblocks);
        }
    }
    
    private Map<ResourceLocation, Multiblock> buildMultiblocks(Map<ResourceLocation, MultiblockCompiler> multiblockBuilders)
    {
        Map<ResourceLocation, Multiblock> multiblocks = new HashMap<>();
        
        multiblockBuilders.forEach((r, b) -> {
            Multiblock multiblock = b.compileMultiblock(r);
            multiblocks.put(r, multiblock);
        });
        
        return multiblocks;
    }
    
    private Map<ResourceLocation, MultiblockCompiler> getMultiblockBuilders(IResourceManager resourceManager)
    {
        Map<ResourceLocation, MultiblockCompiler> compilers = new HashMap<>();
        
        for (ResourceLocation fileLocation : resourceManager.getAllResourceLocations(PATH, (n) -> n.endsWith(EXTENSION)))
        {
            String path = fileLocation.getPath();
            ResourceLocation location = new ResourceLocation(fileLocation.getNamespace(), path.substring(PATH_PREFIX_LENGTH, path.length() - EXTENSION_LENGTH));
            
            try (IResource resource = resourceManager.getResource(fileLocation))
            {
                MultiblockCompiler builder = JsonUtils.fromJson(GSON, IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8), MultiblockCompiler.class);
                if (builder == null)
                {
                    MineTechMod.LOGGER.error("Couldn't load multiblock {} from {} as it's empty or null", location, fileLocation);
                }
                else compilers.put(location, builder);
            }
            catch (JsonParseException e)
            {
                MineTechMod.LOGGER.error("Couldn't parse multiblock {} from {}", location, fileLocation, e);
            }
            catch (IOException e)
            {
                MineTechMod.LOGGER.error("Couldn't read multiblock {} from {}", location, fileLocation, e);
            }
        }
        return compilers;
    }
    
    @Nullable
    @Override
    public IResourceType getResourceType()
    {
        return MULTIBLOCK;
    }
    
    public static void register(MinecraftServer server)
    {
        server.getResourceManager().addReloadListener(new MultiblockResourceManager());
    }
}
