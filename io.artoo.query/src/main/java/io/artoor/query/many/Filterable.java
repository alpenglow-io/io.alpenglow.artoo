package io.artoo.query.many;

import io.artoo.query.Many;
import io.artoo.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static io.artoo.type.Nullability.nonNullable;

public interface Filterable<T extends Record> extends Queryable<T> {
  default Many<T> where(final Predicate<? super T> where) {
    nonNullable(where, "where");
    return where((index, param) -> where.test(param));
  }

  default Many<T> where(final BiPredicate<? super Integer, ? super T> where) {
    return where(where, (i, it) -> it);
  }

  default <C extends Record> Many<C> ofType(final Class<? extends C> type) {
    nonNullable(type, "type");
    return where((index, it) -> type.isInstance(it), (index, it) -> type.cast(it));
  }

  default <R extends Record> Many<R> where(final BiPredicate<? super Integer, ? super T> where, final BiFunction<? super Integer, ? super T, ? extends R> select) {
    nonNullable(where, "where");
    nonNullable(select, "select");
    return new Where<T, R>(this, (i, it) -> {}, where, select)::iterator;
  }
}

final class Where<T extends Record, R extends Record> implements Filterable<R> {
  private final Queryable<T> queryable;
  private final BiConsumer<? super Integer, ? super T> peek;
  private final BiPredicate<? super Integer, ? super T> where;
  private final BiFunction<? super Integer, ? super T, ? extends R> select;

  @Contract(pure = true)
  Where(final Queryable<T> queryable, final BiConsumer<? super Integer, ? super T> peek, final BiPredicate<? super Integer, ? super T> where, final BiFunction<? super Integer, ? super T, ? extends R> select) {
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

