package wexalian.mods.minetech.util;

@FunctionalInterface
public interface ObjFloatConsumer<T>
{
    void accept(T obj, float val);
}
