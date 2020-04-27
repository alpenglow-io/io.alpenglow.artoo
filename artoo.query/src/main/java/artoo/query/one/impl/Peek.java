package artoo.query.one.impl;

import org.jetbrains.annotations.NotNull;
import artoo.func.Cons;
import artoo.query.Queryable;
import artoo.query.one.Peekable;

import java.util.Iterator;

public final class Peek<T> implements Peekable<T> {
  private final Queryable<T> queryable;
  private final Cons<T> cons;

  public Peek(final Queryable<T> queryable, final Cons<T> cons) {
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
