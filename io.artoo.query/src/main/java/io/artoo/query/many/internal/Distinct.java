package io.artoo.query.many.internal;

import io.artoo.func.$2.ConsInt;
import io.artoo.func.Pred;
import io.artoo.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;

import static java.util.Objects.nonNull;

public final class Distinct<T> implements Queryable<T> {
  private final Queryable<T> queryable;
  private final ConsInt<? super T> peek;
  private final Pred<? super T> where;

  @Contract(pure = true)
  public Distinct(final Queryable<T> queryable, final ConsInt<? super T> peek, final Pred<? super T> where) {
    this.queryable = queryable;
    this.peek = peek;
    this.where = where;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    final var result = new ArrayList<T>();
    var index = 0;
    for (var iterator = queryable.iterator(); iterator.hasNext(); index++) {
      var it = iterator.next();
      peek.applyInt(index, it);
      if (nonNull(it) && where.test(it) && !result.contains(it) || !where.test(it))
        result.add(it);
    }
    return result.iterator();
  }
}
