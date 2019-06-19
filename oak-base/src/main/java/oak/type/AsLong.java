package oak.type;

import oak.collect.cursor.Cursor;
import oak.func.Functional;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

@FunctionalInterface
public interface AsLong extends Iterable<Long>, LongSupplier, Functional.Sup {
  long get();

  @Override
  default long getAsLong() { return get(); }

  @NotNull
  @Override
  default Iterator<Long> iterator() { return Cursor.once(get()); }
}
