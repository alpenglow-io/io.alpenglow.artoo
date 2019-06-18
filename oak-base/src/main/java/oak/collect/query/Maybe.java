package oak.collect.query;

import oak.func.con.Consumer1;
import oak.func.fun.Function1;
import oak.func.pre.Predicate1;
import oak.func.sup.Supplier1;
import oak.collect.query.filter.Filtering;
import oak.collect.query.project.Projection;
import oak.type.Value;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

public interface Maybe<T> extends Functor<T, Maybe<T>>, Value<T> {
  static <L> Maybe<L> of(final L value) {
    return isNull(value) ? Maybe.none() : Maybe.just(value);
  }
  static <L> Maybe<L> maybe(final L value) {
    return Maybe.of(value);
  }
  static <L> Maybe<L> none() {
    return new None<>();
  }
  static <L> Maybe<L> just(final L value) {
    return new Just<>(requireNonNull(value, "Value is null"));
  }

  default <M> Maybe<M> select(final Function1<T, M> map) { return Projection.select(this, map); }
  default <M> Maybe<M> selectJust(final MaybeFunction1<T, M> flatMap) { return Projection.selectJust(this, flatMap); }
  default Maybe<T> look(Consumer1<T> peek) {
    return Projection.look(this, peek);
  }

  default Maybe<T> where(Predicate1<T> filter) {
    return Filtering.where(this, filter);
  }

  default Maybe<T> or(Supplier1<T> or) {
    return Maybe.of(requireNonNull(or, "Or is null").get());
  }
  default Maybe<T> or(MaybeSupplier1<T> or) {
    return requireNonNull(or, "Or is null").get();
  }
  default T otherwise(Supplier1<T> another) {
    return requireNonNull(another, "Another is null").get();
  }
  default <E extends Throwable> T otherwise(final String message, final Function1<String, E> exception) throws E {
    for (T value : this) return value;
    throw requireNonNull(exception, "Exception is null").apply(requireNonNull(message, "Message is null"));
  }

  @FunctionalInterface
  interface MaybeSupplier1<T> extends Supplier1<Maybe<T>> {}

  @FunctionalInterface
  interface MaybeFunction1<T, R> extends Function1<T, Maybe<R>> {}
}
