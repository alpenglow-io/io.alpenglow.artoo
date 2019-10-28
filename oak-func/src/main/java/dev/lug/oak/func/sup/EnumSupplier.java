package dev.lug.oak.func.sup;

@FunctionalInterface
public interface EnumSupplier<E extends Enum> {
  E get();
}
