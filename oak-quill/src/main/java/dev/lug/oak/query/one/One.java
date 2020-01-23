package dev.lug.oak.query.one;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.func.con.Consumer1;
import dev.lug.oak.func.fun.Function1;
import dev.lug.oak.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static dev.lug.oak.type.Nullability.nonNullable;
import static dev.lug.oak.type.Nullability.nonNullableState;
import static java.util.Objects.isNull;

public interface One<T> extends Projectable<T>, Filterable<T>, Casing<T> {
  static <L> One<L> of(final L value) {
    return isNull(value) ? One.none() : One.just(value);
  }

  @NotNull
  @Contract("_ -> new")
  static <L> One<L> just(final L value) { return new Just<>(nonNullable(value, "value")); }

  default T asIs() { return null; }

  @NotNull
  @Contract(value = " -> new", pure = true)
  @SuppressWarnings("unchecked")
  static <L> One<L> none() {
    return (One<L>) DefaultOne.None;
  }

  default <R, Q extends Queryable<R>> Q as(final Function1<? super T, ? extends Q> asQueryable) {
    return asQueryable.apply(this.iterator().next());
  }
}

enum DefaultOne {
  ;
  static final One<?> None = new None<>();
}

final class None<T> implements One<T> {
  @NotNull
  @Contract(pure = true)
  @Override
  public final Iterator<T> iterator() {
    return Cursor.none();
  }
}

final class Just<T> implements One<T> {
  private final T value;

  @Contract(pure = true)
  Just(final T value) {
    this.value = value;
  }

  @NotNull
  @Contract(pure = true)
  @Override
  public final Iterator<T> iterator() {
    return Cursor.once(nonNullableState(this.value, "One"));
  }

  @Override
  public T asIs() {
    return value;
  }
}
