package io.artoo.query.many.internal;

import io.artoo.cursor.Cursor;
import io.artoo.func.$2.ConsInt;
import io.artoo.func.$2.PredInt;
import io.artoo.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class Quantify<T> implements Queryable<Boolean> {
  private final Queryable<T> queryable;
  private final ConsInt<? super T> peek;
  private final boolean once;
  private final PredInt<? super T> where;

  @Contract(pure = true)
  public Quantify(final Queryable<T> queryable, ConsInt<? super T> peek, final boolean once, final PredInt<? super T> where) {
    this.queryable = queryable;
    this.peek = peek;
    this.once = once;
    this.where = where;
  }

  @NotNull
  @Override
  public final Iterator<Boolean> iterator() {
    var all = !once;
    var any = once;
    var index = 0;
    for (final var iterator = queryable.iterator(); iterator.hasNext() && (all || !any); index++) {
      final var it = iterator.next();
      peek.acceptInt(index, it);
      all = any = it != null && where.test(index, it);
    }
    return Cursor.of(once || all);
  }
}
