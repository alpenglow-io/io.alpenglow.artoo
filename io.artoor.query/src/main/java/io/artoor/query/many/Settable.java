package io.artoor.query.many;

import io.artoor.query.Many;
import io.artoor.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static io.artoor.type.Nullability.nonNullable;
import static java.util.Objects.nonNull;

public interface Settable<T extends Record> extends Queryable<T> {
  default Many<T> distinct() {
    return distinct(it -> true);
  }

  default Many<T> distinct(final Predicate<? super T> where) {
    return new Distinct<>(this, (i, it) -> {}, nonNullable(where, "where"))::iterator;
  }
}

final class Distinct<T extends Record> implements Settable<T> {
  private final Queryable<T> queryable;
  private final BiConsumer<? super Integer, ? super T> peek;
  private final Predicate<? super T> where;

  @Contract(pure = true)
  Distinct(final Queryable<T> queryable, final BiConsumer<? super Integer, ? super T> peek, final Predicate<? super T> where) {
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
      if (it != null)
        peek.accept(index, it);
      if (nonNull(it) && where.test(it) && !result.contains(it) || !where.test(it)) {
        result.add(it);
      }
    }
    return result.iterator();
  }
}
