package io.artoo.query.many.impl;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import io.artoo.func.Suppl;
import io.artoo.query.Many;

import java.util.ArrayList;
import java.util.Iterator;

public final class Repeat<T> implements Many<T> {
  private final Suppl<? extends T> supplier;
  private final int count;

  @Contract(pure = true)
  public Repeat(final Suppl<? extends T> supplier, final int count) {
    this.supplier = supplier;
    this.count = count;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var array = new ArrayList<T>();
    for (var index = 0; index < count; index++) {
      array.add(supplier.get());
    }
    return array.iterator();
  }
}

