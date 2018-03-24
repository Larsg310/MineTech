package wexalian.mods.minetech.util;

@FunctionalInterface
public interface ObjFloatConsumer<T>
{
    
    public void accept(T obj, float val);
    
}
