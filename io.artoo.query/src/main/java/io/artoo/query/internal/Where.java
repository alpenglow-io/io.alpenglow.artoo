package io.artoo.query.internal;




import io.artoo.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public final class Where<T, R> implements Queryable<R> {
  private final Queryable<T> queryable;
  private final BiConsumer<? super Integer, ? super T> peek;
  private final BiPredicate<? super Integer, ? super T> where;
  private final BiFunction<? super Integer, ? super T, ? extends R> select;

  public Where(final Queryable<T> queryable, final BiPredicate<? super Integer, ? super T> where, final BiFunction<? super Integer, ? super T, ? extends R> select) {
    this(queryable, null, where, select);
  }
  @Contract(pure = true)
  public Where(final Queryable<T> queryable, final BiConsumer<? super Integer, ? super T> peek, final BiPredicate<? super Integer, ? super T> where, final BiFunction<? super Integer, ? super T, ? extends R> select) {
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
      peek.accept(index, it);
      if (it != null && where.test(index, it)) {
        result.add(select.apply(index, it));
      }
    }
    return result.iterator();
  }
}
