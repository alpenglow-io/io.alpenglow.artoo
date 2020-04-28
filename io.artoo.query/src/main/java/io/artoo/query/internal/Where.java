package io.artoo.query.internal;

import io.artoo.func.$2.ConsInt;
import io.artoo.func.$2.FuncInt;
import io.artoo.func.$2.PredInt;
import io.artoo.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

public final class Where<T, R> implements Queryable<R> {
  private final Queryable<T> queryable;
  private final ConsInt<? super T> peek;
  private final PredInt<? super T> where;
  private final FuncInt<? super T, ? extends R> select;

  public Where(final Queryable<T> queryable, final PredInt<? super T> where, final FuncInt<? super T, ? extends R> select) {
    this(queryable, null, where, select);
  }
  @Contract(pure = true)
  public Where(final Queryable<T> queryable, final ConsInt<? super T> peek, final PredInt<? super T> where, final FuncInt<? super T, ? extends R> select) {
    this.queryable = queryable;
    this.peek = peek;
    this.where = where;
    this.select = select;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var result = new ArrayList<R>();
    var index = 0;
    for (final var cursor = queryable.iterator(); cursor.hasNext(); index++) {
      final var it = cursor.next();
      peek.acceptInt(index, it);
      if (it != null && where.verify(index, it)) {
        result.add(select.applyInt(index, it));
      }
    }
    return result.iterator();
  }
}
