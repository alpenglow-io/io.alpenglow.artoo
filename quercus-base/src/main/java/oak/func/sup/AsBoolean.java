package oak.func.sup;

import oak.func.Functional;

import java.util.function.BooleanSupplier;

@FunctionalInterface
public interface AsBoolean extends BooleanSupplier, Functional.Sup {
  boolean get();

  @Override
  default boolean getAsBoolean() {
    return get();
  }
}
