package io.artoo.lance.query.one;

import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Projectable<T> extends Queryable<T> {
  default <R> One<R> select(final Function<? super T, ? extends R> select) {
    nonNullable(select, "select");
    return new Select<T, R>(this, select)::iterator;
  }

  @SuppressWarnings("unchecked")
  default <R, O extends One<R>> O selectOne(final Function<? super T, ? extends O> selectOne) {
    final var so = nonNullable(selectOne, "selectOne");
    return this.iterator().hasNext() ? so.apply(this.iterator().next()) : (O) One.none();
  }
}

final class Select<T, R> implements Projectable<R> {
  private final Queryable<T> queryable;
  private final Function<? super T, ? extends R> select;

  @Contract(pure = true)
  Select(final Queryable<T> queryable, final Function<? super T, ? extends R> select) {
    this.queryable = queryable;
    this.select = select;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    R result = null;
    for (final var value : queryable) if (value != null) result = select.apply(value);
    return Cursor.of(result);
  }
}

final class SelectOne<T, R, O extends One<R>> implements Projectable<R> {
  private final Queryable<T> queryable;
  private final Function<? super T, ? extends O> select;

  @Contract(pure = true)
  SelectOne(final Queryable<T> queryable, final Function<? super T, ? extends O> select) {
    this.queryable = queryable;
    this.select = select;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    O result = null;
    for (final var value : queryable) {
      if (value != null) {
        result = select.apply(value);
      }
    }
    return result != null ? result.iterator() : Cursor.none();
  }
}


