package io.artoo.lance.query.many;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.cursor.Cursor;
import org.jetbrains.annotations.NotNull;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Averageable<T> extends Queryable<T> {
  default <N extends Number> One<Double> average(final Func.Uni<? super T, ? extends N> select) {
    return new Average<>(this, nonNullable(select, "select"));
  }

  default One<Double> average() {
    return new Average<>(this, it -> it instanceof Number n ? n.doubleValue() : null);
  }
}

final class Average<T, N extends Number> implements One<Double> {
  private final Queryable<T> queryable;
  private final Func.Uni<? super T, ? extends N> select;

  Average(final Queryable<T> queryable, final Func.Uni<? super T, ? extends N> select) {
    assert queryable != null && select != null;
    this.queryable = queryable;
    this.select = select;
  }

  @NotNull
  @Override
  public final Cursor<Double> cursor() {
    final var res = Cursor.<Double>local();
    var count = 0;

    var cursor = queryable.cursor();
    while (cursor.hasNext()) {
      try {
        final var selected = cursor.fetch(t -> select.tryApply(t).doubleValue());
        res.set(res.hasNext() ? res.next() + selected : selected);
        count++;
      } catch (Throwable cause) {
        res.grab(cause);
      }
    }

    return res.size() == 0 ? res : res.set(res.next() / count);
  }
}



