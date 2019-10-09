package oak.type;

import oak.collect.cursor.Cursor;
import oak.func.Functional;
import oak.func.sup.StringSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

@FunctionalInterface
public interface AsString extends Iterable<String>, StringSupplier, Functional.Sup {
  String get();

  @Override
  default String getAsString() { return get(); }

  @NotNull
  @Override
  default Iterator<String> iterator() { return Cursor.of(get()); }
}
