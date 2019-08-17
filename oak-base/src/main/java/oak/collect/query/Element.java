package oak.collect.query;

import oak.collect.cursor.Cursor;
import oak.type.AsLong;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public interface Element<T, M extends Iterable<T>> extends Functor<T, M> {
  static <S> Maybe<S> at(final Queryable<S> some, final long index) {
    return new At<>(
      requireNonNull(some, "Some is null"),
      index
    );
  }

  static <S> Queryable<S> first(final Queryable<S> some) {
    return new First<>(requireNonNull(some, "Some is null"));
  }

  static <S> Maybe<S> single(final Queryable<S> some) {
    return new Single<>(requireNonNull(some, "Some is null"));
  }

  static <S> Queryable<S> last(final Queryable<S> some) {
    return new Last<>(requireNonNull(some, "Some is null"));
  }
}

final class At<T> implements Element<T, Maybe<T>>, Maybe<T> {
  private final Queryable<T> some;
  private final long index;

  @Contract(pure = true)
  At(final Queryable<T> some, final long index) {
    this.some = some;
    this.index = index;
  }

  @Override
  public final T get() {
    var i = index;
    T value = null;
    for (var iterator = some.iterator(); i >= 0; i--) value = iterator.hasNext() ? iterator.next() : null;
    return value;
  }
}

final class First<S> implements Element<S, Queryable<S>>, Queryable<S> {
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

  @Override
  public final long get() {
    return this.value;
  }

  static Maybe<Index> of(final long value) {
    return Maybe.just(value)
      .where(it -> it >= 0)
      .select(Index::new);
  }
}

final class Last<S> implements Element<S, Queryable<S>>, Queryable<S> {
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

final class Single<S> implements Element<S, Maybe<S>>, Maybe<S> {
  private final Queryable<S> some;

  Single(final Queryable<S> some) {
    this.some = some;
  }

  @Override
  public final S get() {
    final var iterator = some.iterator();
    return iterator.hasNext() ? iterator.next() : null;
  }
}
