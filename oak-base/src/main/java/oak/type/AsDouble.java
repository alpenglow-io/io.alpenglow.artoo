package oak.type;

import oak.collect.cursor.Cursor;
import oak.func.Functional;
import oak.func.sup.Supplier1;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.DoubleSupplier;

@FunctionalInterface
public interface AsDouble extends Iterable<Double>, DoubleSupplier, Functional.Sup {
  double get();

  @Override
  default double getAsDouble() { return get(); }

  @NotNull
  @Override
  default Iterator<Double> iterator() { return Cursor.once(get()); }
}
