package io.artoo.query.one.impl;


import io.artoo.query.Queryable;
import io.artoo.query.one.Peekable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Consumer;

public final class Peek<T> implements Peekable<T> {
  private final Queryable<T> queryable;
  private final Consumer<? super T> cons;

  public Peek(final Queryable<T> queryable, final Consumer<? super T> cons) {
    this.queryable = queryable;
    this.cons = cons;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    for (var value : queryable) if (value != null) cons.accept(value);
    return this.queryable.iterator();
  }
}
