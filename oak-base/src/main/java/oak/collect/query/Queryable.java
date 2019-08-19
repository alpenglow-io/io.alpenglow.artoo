package oak.collect.query;

import oak.collect.cursor.Cursor;
import oak.collect.query.Projection.IndexFunction1;
import oak.collect.query.Projection.IndexManyFunction1;
import oak.collect.query.aggregate.Aggregation;
import oak.collect.query.concat.Concatenation;
import oak.collect.query.filter.Filtering;
import oak.func.con.Consumer1;
import oak.func.fun.Function1;
import oak.func.fun.Function2;
import oak.func.pre.Predicate1;
import oak.func.sup.Supplier1;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

import static java.util.Arrays.copyOf;

public interface Queryable<T> extends Iterable<T> {
  @SafeVarargs
  static <S> Queryable<S> asQueryable(final S... values) {
    return new Many<>(copyOf(values, values.length));
  }

  @SafeVarargs
  static <S> Queryable<S> from(final S... values) { return new Many<>(values); }

  static <S> Queryable<S> empty() { return new Empty<>(); }

  // projection
  default <S> Queryable<S> select(final Function1<T, S> map) {
    return Projection.select(this, map);
  }
  default <S> Queryable<S> select(final IndexFunction1<T, S> indexMap) { return Projection.selectIndex(this, indexMap); }
  default <S> Queryable<S> selectMany(final ManyFunction<T, S> flatMap) { return Projection.selectMany(this, flatMap); }
  default <S> Queryable<S> selectMany(final IndexManyFunction1<T, S> flatMap) { return Projection.selectMany(this, flatMap); }
  default Queryable<T> peek(final Consumer1<T> peek) { return Projection.peek(this, peek); }

  // filtering
  default Queryable<T> where(final Predicate1<T> filter) { return Filtering.where(this, filter); }
  default <C> Queryable<C> ofType(final Class<C> type) { return Filtering.ofType(this, type); }

  // element
  default Maybe<T> at(final int index) { return Element.at(this, index); }
  default Maybe<T> first() { return Element.first(this); }
  default Maybe<T> last() { return Element.last(this); }
  default T single() { return Element.single(this); }

  // aggregation
  default Maybe<T> aggregate(final Function2<T, T, T> reduce) { return Aggregation.accumulate(this, reduce); }
  default <S> Maybe<S> aggregate(final S seed, final Function2<S, T, S> reduce) { return Aggregation.seed(this, seed, reduce); }
  default <S> Maybe<S> aggregate(final S seed, final Predicate1<T> expression, Function2<S, T, S> reduce) {
    return Aggregation.expression(this, seed, expression, reduce);
  }
  default int count() { return Aggregation.count(this); }
  default long countAsLong() { return Aggregation.countAsLong(this); }
  default int count(final Predicate1<T> filter) { return Aggregation.count(this, filter); }
  default long countAsLong(final Predicate1<T> filter) { return Aggregation.countAsLong(this, filter); }

  // concatenation
  @SuppressWarnings("unchecked")
  default Queryable<T> concat(final T... values) { return Concatenation.concat(this, values); }
  default Queryable<T> merge(final Queryable<T> some) { return Concatenation.merge(this, some); }

  // partitioning
  default Queryable<T> skip(final int until) { return Partitioning.skip(this, until); }
  default Queryable<T> skipWhile(final Predicate1<T> expression) { return Partitioning.skipWhile(this, expression); }
  default Queryable<T> take(final int until) { return Partitioning.take(this, until); }
  default Queryable<T> takeWhile(final Predicate1<T> expression) { return Partitioning.takeWhile(this, expression); }

  @FunctionalInterface
  interface ManyFunction<T, R> extends Function1<T, Queryable<R>> {}

  @FunctionalInterface
  interface ManySupplier<T> extends Supplier1<Queryable<T>> {}
}

interface QueryableArray<Q> extends Queryable<Q>, Supplier1<Q[]> {
  @Override
  default Maybe<Q> at(final int index) {
    return null;
  }
}

final class Empty<Q> implements Queryable<Q> {
  @Override
  public final Iterator<Q> iterator() {
    return Cursor.none();
  }
}

final class Many<T> implements QueryableArray<T> {
  private final T[] values;

  @Contract(pure = true)
  Many(final T[] values) {
    this.values = values;
  }

  @Override
  @Contract(pure = true)
  public final T[] get() {
    return values;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    return Cursor.forward(values);
  }

  @Override
  public String toString() {
    return String.format("Many{values=%s}", Arrays.toString(values));
  }
}
