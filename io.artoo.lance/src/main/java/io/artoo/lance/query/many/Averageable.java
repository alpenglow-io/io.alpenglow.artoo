package io.artoo.lance.query.many;

import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Averageable<T> extends Queryable<T> {
  default <N extends Number> One<Double> average(final Func.Uni<? super T, ? extends N> select) {
    return new Average<>(this, (it) -> {}, nonNullable(select, "select"));
  }

  default One<Double> average() {
    return new Average<>(this, it -> {}, it -> it instanceof Number n ? n.doubleValue() : null);
  }
}

final class Average<T, N extends Number> implements One<Double> {
  private final Queryable<T> queryable;
  private final Cons.Uni<? super T> peek;
  private final Func.Uni<? super T, ? extends N> select;

  Average(final Queryable<T> queryable, final Cons.Uni<? super T> peek, final Func.Uni<? super T, ? extends N> select) {
    assert queryable != null && peek != null && select != null;
    this.queryable = queryable;
    this.peek = peek;
    this.select = select;
  }

  @NotNull
  @Override
  public final Iterator<Double> iterator() {
    var total = 0.0d;
    var count = 0;
    for (final var it : queryable) {
      if (it != null) {
        peek.accept(it);
        final var selected = select.apply(it);
        if (selected != null) {
          total = total + selected.doubleValue();
          count++;
        }
      }
    }

    return count == 0 ? Cursor.none() : Cursor.of(total / count);
  }
}



