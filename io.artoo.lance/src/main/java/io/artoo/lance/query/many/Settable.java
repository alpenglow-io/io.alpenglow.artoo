package io.artoo.lance.query.many;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Pred;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static io.artoo.lance.type.Nullability.nonNullable;
import static java.util.Objects.nonNull;

public interface Settable<T> extends Queryable<T> {
  default Many<T> distinct() {
    return distinct(it -> true);
  }

  default Many<T> distinct(final Pred.Uni<? super T> where) {
    return new Distinct<>(this, (i, it) -> {}, nonNullable(where, "where"));
  }
}

final class Distinct<T> implements Many<T> {
  private final Queryable<T> queryable;
  private final Cons.Bi<? super Integer, ? super T> peek;
  private final Pred.Uni<? super T> where;

  @Contract(pure = true)
  Distinct(final Queryable<T> queryable, final Cons.Bi<? super Integer, ? super T> peek, final Pred.Uni<? super T> where) {
    this.queryable = queryable;
    this.peek = peek;
    this.where = where;
  }

  @NotNull
  @Override
  public final Cursor<T> cursor() {
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

    final var iterator = result.iterator();
    return new Cursor<T>() {
      @Override
      public Cursor<T> append(final T element) {
        return null;
      }

      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public T next() {
        return iterator.next();
      }
    };
  }
}
