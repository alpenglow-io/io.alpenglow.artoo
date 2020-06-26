package io.artoo.lance.query.one;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Projectable<T> extends Queryable<T> {
  default <R> One<R> select(final Func.Uni<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return new Select<T, R>(this, select);
  }

  default <R, O extends One<R>> One<R> selectOne(final Func.Uni<? super T, ? extends O> selectOne) {
    return new SelectOne<>(this, nonNullable(selectOne, "selectOne"));
  }
}

final class Select<T, R> implements One<R> {
  private final Queryable<T> queryable;
  private final Func.Uni<? super T, ? extends R> select;

  @Contract(pure = true)
  Select(final Queryable<T> queryable, final Func.Uni<? super T, ? extends R> select) {
    assert queryable != null && select != null;
    this.queryable = queryable;
    this.select = select;
  }

  @NotNull
  @Override
  public final Cursor<R> cursor() {
    R result = null;
    for (final var value : queryable) if (value != null) result = select.apply(value);
    return Cursor.of(result);
  }
}

final class SelectOne<T, R, O extends One<R>> implements One<R> {
  private final Queryable<T> queryable;
  private final Func.Uni<? super T, ? extends O> select;

  @Contract(pure = true)
  SelectOne(final Queryable<T> queryable, final Func.Uni<? super T, ? extends O> select) {
    this.queryable = queryable;
    this.select = select;
  }

  @NotNull
  @Override
  public final Cursor<R> cursor() {
    O selected = null;
    for (final var value : queryable) {
      if (value != null) {
        selected = select.apply(value);
      }
    }
    final var iterator = selected.iterator();
    return selected != null ? new Cursor<R>() {
      @Override
      public Cursor<R> append(final R element) {
        return null;
      }

      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public R next() {
        return iterator.next();
      }
    } : Cursor.empty();
  }
}


