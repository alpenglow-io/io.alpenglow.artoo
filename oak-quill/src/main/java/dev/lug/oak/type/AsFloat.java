package dev.lug.oak.type;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.func.Functional;
import dev.lug.oak.func.sup.FloatSupplier;
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
