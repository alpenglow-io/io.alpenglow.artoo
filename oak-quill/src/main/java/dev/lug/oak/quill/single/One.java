package dev.lug.oak.quill.single;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.func.con.Consumer1;
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

  @NotNull
  @Contract(value = " -> new", pure = true)
  static <L> One<L> none() {
    return new None<>();
  }

  default void eventually(@NotNull final Consumer1<T> consumer) {
    for (final var value : this) nonNullable(consumer, "consumer").accept(value);
  }
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
    return Cursor.of(nonNullableState(this.value, "One"));
  }
}
