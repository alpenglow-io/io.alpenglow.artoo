package dev.lug.oak.type;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.func.Functional;
import dev.lug.oak.func.sup.StringSupplier;
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
