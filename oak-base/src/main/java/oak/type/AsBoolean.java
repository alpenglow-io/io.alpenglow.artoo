package oak.type;

import oak.collect.cursor.Cursor;
import oak.func.Functional;
import oak.func.sup.Supplier1;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.BooleanSupplier;

@FunctionalInterface
public interface AsBoolean extends Iterable<Boolean>, BooleanSupplier, Functional.Sup {
  boolean get();

  @Override
  default boolean getAsBoolean() { return get(); }

  @NotNull
  @Override
  default Iterator<Boolean> iterator() { return Cursor.once(get()); }
}
