package io.artoo.lance.query.many;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.type.Eitherable;
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

@SuppressWarnings("StatementWithEmptyBody")
final class Average<T, N extends Number> implements One<Double>, Eitherable {
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


    final class Result {
      Double value = null;
      Cursor<Double> cursor = Cursor.empty();
    }
    final var result = new Result();

    final var cursor = queryable.cursor();
    while (
      cursor.next(element ->
        either(
          () -> select.tryApply(element),
          it -> result.value = result.value == null ? it.doubleValue() : result.value + it.doubleValue(),
          cause -> result.cursor = result.cursor.halt(cause)
        )
      )
    );

    return result.cursor.hasHalted() || cursor.size() == 0 ? result.cursor : result.cursor.append(result.value / cursor.size());
  }
}



