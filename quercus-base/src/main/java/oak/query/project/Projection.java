package oak.query.project;

import oak.func.con.Consumer1;
import oak.func.fun.Function1;
import oak.query.Functor;
import oak.query.Maybe;
import oak.query.Queryable;

import static java.util.Objects.requireNonNull;

public interface Projection<T, M extends Iterable<T>> extends Functor<T, M> {
  static <S, R> Queryable<R> select(final Queryable<S> some, final Function1<S, R> map) {
    return new SelectMany<>(
      requireNonNull(some, "Some is null"),
      requireNonNull(map, "Map is null")
    );
  }

  static <S, R> Queryable<R> selectMany(final Queryable<S> some, final IndexManyFunction1<S, R> indexMap) {
    return new SelectManyMany<>(
      new SelectIndex<>(
        requireNonNull(some, "Some is null"),
        requireNonNull(indexMap, "IndexMap is null")
      )
    );
  }

  static <S, R> Queryable<R> selectMany(final Queryable<S> some, final Queryable.ManyFunction<S, R> flatMap) {
    return new SelectManyMany<>(
      new SelectMany<>(
        requireNonNull(some, "Some is null"),
        requireNonNull(flatMap, "FlatMap is null")
      )
    );
  }

  static <S> Queryable<S> look(final Queryable<S> some, final Consumer1<S> peek) {
    return new LookMany<>(
      requireNonNull(some, "Some is null"),
      requireNonNull(peek, "Peek is null")
    );
  }

  static <S, R> Maybe<R> select(final Maybe<S> maybe, final Function1<S, R> map) {
    return new SelectMaybe<>(
      requireNonNull(maybe, "Maybe is null"),
      requireNonNull(map, "Map is null")
    );
  }

  static <S, R> Maybe<R> selectJust(final Maybe<S> maybe, final Maybe.MaybeFunction1<S, R> flatMap) {
    return new SelectJust<>(
      new SelectMaybe<>(
        requireNonNull(maybe, "Maybe is null"),
        requireNonNull(flatMap, "FlatMap is null")
      )
    );
  }

  static <S> Maybe<S> look(final Maybe<S> maybe, final Consumer1<S> peek) {
    return new LookMaybe<>(
      requireNonNull(maybe, "Maybe is null"),
      requireNonNull(peek, "Peek is null")
    );
  }

  static <S, R> Queryable<R> selectIndex(final Queryable<S> many, final IndexFunction1<S, R> indexMap) {
    return new SelectIndex<>(
      requireNonNull(many, "Some is null"),
      requireNonNull(indexMap, "IndexMap is null")
    );
  }

  @FunctionalInterface
  interface IndexFunction1<S, R> {
    R apply(int index, S value);
  }

  @FunctionalInterface
  interface IndexManyFunction1<S, R> extends IndexFunction1<S, Queryable<R>> {}
}
