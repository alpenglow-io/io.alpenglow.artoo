package artoo.query.impl;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import artoo.func.$2.ConsInt;
import artoo.func.$2.FuncInt;
import artoo.query.Queryable;

import java.util.ArrayList;
import java.util.Iterator;

public final class SelectMany<T, R, Q extends Queryable<R>> implements Queryable<R> {
  private final Queryable<T> queryable;
  private final ConsInt<? super T> peek;
  private final FuncInt<? super T, ? extends Q> select;

  @Contract(pure = true)
  public SelectMany(final Queryable<T> queryable, final FuncInt<? super T, ? extends Q> select) {
    this(queryable, null, select);
  }

  @Contract(pure = true)
  public SelectMany(final Queryable<T> queryable, final ConsInt<? super T> peek, final FuncInt<? super T, ? extends Q> select) {
    this.queryable = queryable;
    this.peek = peek;
    this.select = select;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var array = new ArrayList<R>();
    var index = 0;
    for (var cursor = queryable.iterator(); cursor.hasNext(); index++) {
      var it = cursor.next();
      if (peek != null) peek.applyInt(index, it);
      if (it != null) {
        final var queried = select.applyInt(index, it);
        if (queried != null) {
          for (final var selected : queried) {
            if (selected != null)
              array.add(selected);
          }
        }
      }
    }
    return array.iterator();
  }
}
