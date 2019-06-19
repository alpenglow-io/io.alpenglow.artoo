package oak.type;

import oak.collect.cursor.Cursor;
import oak.func.Functional;
import oak.func.sup.FloatSupplier;
import oak.func.sup.Supplier1;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.DoubleSupplier;

@FunctionalInterface
public interface AsFloat extends Iterable<Float>, FloatSupplier, Functional.Sup {
  float get();

  @Override
  default float getAsFloat() { return get(); }

  @NotNull
  @Override
  default Iterator<Float> iterator() { return Cursor.once(get()); }
}
