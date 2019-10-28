package dev.lug.oak.type;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.func.Functional;
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
  default Iterator<Double> iterator() { return Cursor.of(get()); }
}
