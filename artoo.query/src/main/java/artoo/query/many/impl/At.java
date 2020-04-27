package artoo.query.many.impl;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import artoo.cursor.Cursor;
import artoo.query.Queryable;

import java.util.Iterator;

public final class At<T> implements Queryable<T> {
  private final Queryable<T> queryable;
  private final int index;

  @Contract(pure = true)
  public At(final Queryable<T> queryable, final int index) {
    this.queryable = queryable;
    this.index = index;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    var count = 0;
    T returned = null;
    for (final var iterator = queryable.iterator(); iterator.hasNext() && count <= index; count++) {
      returned = iterator.next();
    }
    if (count < index)
      returned = null;
    return Cursor.of(returned);
  }
}
