package oak.collect.query;

import oak.collect.cursor.Cursor;
import oak.type.As;
import oak.type.AsLong;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static java.util.Objects.requireNonNull;
import static oak.collect.query.Maybe.*;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public interface Element<T> extends Maybe<T> {
  static <S> Maybe<S> at(final Queryable<S> some, final int index) {
    return new At<>(
      requireNonNull(some, "Some is null"),
      index
    );
  }

  static <S> Maybe<S> first(final Queryable<S> some) {
    return new First<>(requireNonNull(some, "Some is null"));
  }

  static <S> S single(final Queryable<S> some) {
    return first(some).otherwise("Query can't be satisfied", NoSuchElementException::new);
  }

  static <S> Maybe<S> last(final Queryable<S> some) {
    return new Last<>(requireNonNull(some, "Some is null"));
  }
}

final class At<T> implements Element<T>, As<T> {
  private final Queryable<T> some;
  private final int index;

  @Contract(pure = true)
  At(final Queryable<T> some, final int index) {
    this.some = some;
    this.index = index;
  }

  @Override
  @Nullable
  public final T get() {
    var i = index;
    T value = null;
    for (var iterator = some.iterator(); i >= 0; i--) value = iterator.hasNext() ? iterator.next() : null;
    return value;

  }
}

final class First<S> implements Element<S> {
  private final Queryable<S> some;

  @Contract(pure = true)
  First(Queryable<S> some) {
    this.some = some;
  }

  @NotNull
  @Override
  public final Iterator<S> iterator() {
    return Cursor.maybe(some.iterator().next());
  }

  @Override
  public String toString() {
    return reflectionToString(this);
  }
}

final class Index implements AsLong {
  private final long value;

  private Index(long value) {
    this.value = value;
  }

  static Maybe<Index> of(final long value) {
    return just(value)
      .where(it -> it >= 0)
      .select(Index::new);
  }

  @Override
  public final long get() {
    return this.value;
  }
}

final class Last<S> implements Element<S> {
  private final Queryable<S> some;

  @Contract(pure = true)
  Last(final Queryable<S> some) {
    this.some = some;
  }

  @NotNull
  @Override
  public final Iterator<S> iterator() {
    S last = null;
    for (final var one : some) last = one;
    return Cursor.maybe(last);
  }
}
