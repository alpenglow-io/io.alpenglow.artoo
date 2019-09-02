package oak.type;

import oak.collect.cursor.Cursor;
import oak.func.Functional;
import oak.func.sup.FloatSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

@FunctionalInterface
public interface AsFloat extends Iterable<Float>, FloatSupplier, Functional.Sup {
  float get();

  @Override
  default float getAsFloat() { return get(); }

  @NotNull
  @Override
  default Iterator<Float> iterator() { return Cursor.of(get()); }
}
