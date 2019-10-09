package oak.type;

import oak.collect.cursor.Cursor;
import oak.func.Functional;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.LongSupplier;

@FunctionalInterface
public interface AsLong extends Iterable<Long>, LongSupplier, Functional.Sup {
  long get();

  @Override
  default long getAsLong() { return get(); }

  @NotNull
  @Override
  default Iterator<Long> iterator() { return Cursor.of(get()); }
}
