package io.artoo.query.one.impl;

import io.artoo.func.Cons;
import io.artoo.query.Queryable;
import io.artoo.query.one.Peekable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class Peek<T> implements Peekable<T> {
  private final Queryable<T> queryable;
  private final Cons<? super T> cons;

  public Peek(final Queryable<T> queryable, final Cons<? super T> cons) {
    this.queryable = queryable;
    this.cons = cons;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    for (var value : queryable) if (value != null) cons.apply(value);
    return this.queryable.iterator();
  }
}
