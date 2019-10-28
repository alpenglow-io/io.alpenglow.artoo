package dev.lug.oak.type;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.func.Functional;
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
  default Iterator<Boolean> iterator() { return Cursor.of(get()); }
}
