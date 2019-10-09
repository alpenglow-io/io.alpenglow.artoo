package oak.type;

import oak.collect.cursor.Cursor;
import oak.func.Functional;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.IntSupplier;

@FunctionalInterface
public interface AsInt extends Iterable<Integer>, IntSupplier, Functional.Sup {
  int get();

  @Override
  default int getAsInt() { return get(); }

  @NotNull
  @Override
  default Iterator<Integer> iterator() { return Cursor.of(get()); }
}
