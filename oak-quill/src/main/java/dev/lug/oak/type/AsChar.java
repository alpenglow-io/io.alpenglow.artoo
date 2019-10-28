package dev.lug.oak.type;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.func.Functional;
import dev.lug.oak.func.sup.CharSupplier;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

@FunctionalInterface
public interface AsChar extends Iterable<Character>, CharSupplier, Functional.Sup {
  char get();

  @Override
  default char getAsChar() { return get(); }

  @NotNull
  @Override
  default Iterator<Character> iterator() { return Cursor.of(get()); }
}
