package io.artoo.lance.query.many;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.func.Func;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

import static io.artoo.lance.query.many.Index.index;
import static io.artoo.lance.type.Nullability.nonNullable;

public interface Projectable<T> extends Queryable<T> {
  default <R> Many<R> select(Func.Bi<? super Integer, ? super T, ? extends R> select) {
    nonNullable(select, "select");
    return new Select<>(this, select);
  }

  default <R> Many<R> select(Function<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return select((index, value) -> select.apply(value));
  }

  default <R, M extends Many<R>> Many<R> selectMany(Func.Bi<? super Integer, ? super T, ? extends M> select) {
    nonNullable(select, "select");
    return new SelectMany<>(this, select);
  }

  default <R, M extends Many<R>> Many<R> selectMany(Function<? super T, ? extends M> select) {
    nonNullable(select, "select");
    return new SelectMany<>(this, (index, it) -> select.apply(it));
  }
}

final class Select<T, R> implements Many<R> {
  private final Queryable<T> queryable;
  private final Func.Bi<? super Integer, ? super T, ? extends R> select;

  @Contract(pure = true)
  public Select(final Queryable<T> queryable, final Func.Bi<? super Integer, ? super T, ? extends R> select) {
    assert queryable != null && select != null;
    this.queryable = queryable;
    this.select = select;
  }

  @NotNull
  @Override
  public final Cursor<R> cursor() {
    final var projected = Cursor.<R>local();

    final var cursor = queryable.cursor();
    try {
      for (var index = index(); cursor.hasNext(); index.value++) {
        projected.append(
          cursor.fetch(it -> select.tryApply(index.value, it))
        );
      }
    } catch (Throwable cause) {
      projected.grab(cause);
    }

    return projected;
  }
}

final class SelectMany<T, R, Q extends Queryable<R>> implements Many<R> {
  private final Queryable<T> queryable;
  private final Func.Bi<? super Integer, ? super T, ? extends Q> select;

  @Contract(pure = true)
  public SelectMany(final Queryable<T> queryable, final Func.Bi<? super Integer, ? super T, ? extends Q> select) {
    this.queryable = queryable;
    this.select = select;
  }

  @NotNull
  @Override
  public final Cursor<R> cursor() {
    final var projectedMany = Cursor.<R>local();

    final var cursor = queryable.cursor();
    try {
      for (var index = index(); cursor.hasNext(); index.value++) {
        for (final var element : cursor.fetch(it -> select.tryApply(index.value, it))) {
          projectedMany.append(element);
        }
      }
    } catch (Throwable throwable) {
      projectedMany.grab(throwable);
    }

    return projectedMany;
  }
}

