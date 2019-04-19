package com.wexalian.mods.minetech.multiblock;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.state.IProperty;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class MultiblockCompiler
{
    public static final JsonDeserializer<BlockPos> DESERIALIZER_BLOCKPOS = (JsonElement json, Type type, JsonDeserializationContext context) -> {
        JsonArray arr = JsonUtils.getJsonArray(json, "size");
        return new BlockPos(arr.get(0).getAsInt(), arr.get(1).getAsInt(), arr.get(2).getAsInt());
    };
    public static final JsonDeserializer<IBlockState> DESERIALIZER_STATE = (JsonElement json, Type type, JsonDeserializationContext context) -> {
        String fullState = json.getAsString();
        String[] blockStateString = fullState.split(Pattern.quote("["));
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockStateString[0]));
        if (block != null)
        {
            IBlockState blockState = block.getDefaultState();
            if (blockStateString.length >= 2)
            {
                String stateString = blockStateString[1].replace("]", "");
                String[] states = stateString.split(",");
                for (String state : states)
                {
                    String[] keyValue = state.split("=");
                    IProperty<?> property = block.getStateContainer().getProperty(keyValue[0]);
                    if (property != null)
                    {
                        blockState = getBlockState(blockState, property, keyValue[1]);
                    }
                }
            }
            return blockState;
        }
        return null;
    };
    public static final JsonDeserializer<Pair<Block, Map<IProperty<?>, String>>> DESERIALIZER_PAIR = (JsonElement json, Type type, JsonDeserializationContext context) -> {
        String fullState = json.getAsString();
        String[] blockStateString = fullState.split(Pattern.quote("["));
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockStateString[0]));
        if (block != null)
        {
            if (blockStateString.length >= 2)
            {
                Map<IProperty<?>, String> propertyValueMap = new HashMap<>();
                
                String stateString = blockStateString[1].replace("]", "");
                String[] states = stateString.split(",");
                for (String state : states)
                {
                    String[] keyValue = state.split("=");
                    IProperty<?> property = block.getStateContainer().getProperty(keyValue[0]);
                    if (property != null)
                    {
                        propertyValueMap.put(property, keyValue[1]);
                    }
                }
                return Pair.of(block, propertyValueMap);
            }
            else return Pair.of(block, new HashMap<>());
        }
        return null;
    };
    
    // JSON IN
    private String name;
    private BlockPos size;
    private BlockPos center;
    private Pair<Block, Map<IProperty<?>, String>> trigger;
    private IBlockState block;
    private Map<Character, Pair<Block, Map<IProperty<?>, String>>> palette;
    private String[][] structure;
    //
    
    @Nullable
    @SuppressWarnings("unchecked")
    public Multiblock compileMultiblock(@Nonnull ResourceLocation id)
    {
        IBlockState[][][] blockStructure = new IBlockState[size.getX()][size.getY()][size.getZ()];
        Predicate<IBlockState>[][][] predicates = new Predicate[size.getX()][size.getY()][size.getZ()];
        Predicate<IBlockState> triggerPredicate = getBlockStatePredicate(this.trigger.getKey(), this.trigger.getValue());
        
        BlockPos triggerOffset = null;
        IBlockState triggerState = getBlockStateWithProperties(trigger.getKey(), trigger.getValue());
        
        for (int y = 0; y < size.getY(); y++)
        {
            String[] layer = structure[y];
            for (int z = 0; z < size.getZ(); z++)
            {
                String row = layer[z];
                for (int x = 0; x < size.getX(); x++)
                {
                    Character c = row.charAt(x);
                    
                    Pair<Block, Map<IProperty<?>, String>> blockPropertyPair = palette.get(c);
                    
                    Block block = blockPropertyPair.getLeft();
                    Map<IProperty<?>, String> propertyValueMap = blockPropertyPair.getRight();
                    
                    IBlockState state = getBlockStateWithProperties(block, propertyValueMap);
                    
                    if (triggerPredicate.test(state))
                    {
                        triggerOffset = new BlockPos(x, y, z);
                    }
                    
                    blockStructure[x][y][z] = state;
                    
                    predicates[x][y][z] = getBlockStatePredicate(block, propertyValueMap);
                }
            }
        }
        MultiblockValidator validator = new MultiblockValidator(predicates, triggerPredicate);
        
        if(triggerOffset == null) return null;
        
        return new Multiblock(id, name, size, triggerOffset, block, triggerState, blockStructure, center, validator);
    }
    
    @Nonnull
    private Predicate<IBlockState> getBlockStatePredicate(@Nonnull Block block, @Nonnull Map<IProperty<?>, String> propertyValueMap)
    {
        return (checkState) -> {
            if (checkState.getBlock() != block) return false;
            for (Map.Entry<IProperty<?>, String> entry : propertyValueMap.entrySet())
            {
                if (!checkState.has(entry.getKey())) return false;
                if (checkState.get(entry.getKey()) != entry.getKey().parseValue(entry.getValue()).orElse(null)) return false;
            }
            return true;
        };
    }
    
    @Nonnull
    private IBlockState getBlockStateWithProperties(@Nonnull Block block, @Nonnull Map<IProperty<?>, String> propertyValueMap)
    {
        IBlockState state = block.getDefaultState();
        for (Map.Entry<IProperty<?>, String> entry : propertyValueMap.entrySet())
        {
            state = getBlockState(state, entry.getKey(), entry.getValue());
        }
        return state;
    }
    
    @SuppressWarnings("ConstantConditions")
    private static <T extends Comparable<T>> IBlockState getBlockState(@Nonnull IBlockState state, @Nonnull IProperty<T> property, String value)
    {
        return state.with(property, property.parseValue(value).orElse(null));
    }
}
