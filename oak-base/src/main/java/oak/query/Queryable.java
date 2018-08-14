package oak.query;

import oak.func.con.Consumer1;
import oak.func.fun.Function1;
import oak.func.fun.Function2;
import oak.func.pre.Predicate1;
import oak.func.sup.Supplier1;
import oak.query.aggregate.Aggregation;
import oak.query.concat.Concatenation;
import oak.query.element.Element;
import oak.query.filter.Filtering;
import oak.query.partition.Partitioning;
import oak.query.project.Projection;
import oak.query.project.Projection.IndexFunction1;
import oak.query.project.Projection.IndexManyFunction1;

public interface Queryable<T> extends Functor<T, Queryable<T>> {
  @SafeVarargs
  static <S> Queryable<S> Q(final S... values) {
    return new Many<>(values);
  }
  
  static <S> Queryable<S> empty() { return new Empty<>(); }

  // projection
  default <S> Queryable<S> select(final Function1<T, S> map) { return Projection.select(this, map); }
  default <S> Queryable<S> select(final IndexFunction1<T, S> indexMap) { return Projection.selectIndex(this, indexMap); }
  default <S> Queryable<S> selectMany(final ManyFunction<T, S> flatMap) { return Projection.selectMany(this, flatMap); }
  default <S> Queryable<S> selectMany(final IndexManyFunction1<T, S> flatMap) { return Projection.selectMany(this, flatMap); }
  default Queryable<T> look(final Consumer1<T> peek) { return Projection.look(this, peek); }

  // filtering
  default Queryable<T> where(final Predicate1<T> filter) { return Filtering.where(this, filter); }
  default <C> Queryable<C> ofType(final Class<C> type) { return Filtering.ofType(this, type); }

  // element
  default Maybe<T> at(final int index) { return Element.at(this, index); }
  default Queryable<T> first() { return Element.first(this); }
  default Queryable<T> last() { return Element.last(this); }
  default Maybe<T> single() { return Element.single(this); }

  // aggregation
  default <S> Maybe<S> aggregate(final S seed, final Function2<S, T, S> reduce) { return Aggregation.seed(this, seed, reduce); }
  default <S> Maybe<S> aggregate(final Function1<T, S> identity, final Function2<S, T, S> reduce) { return Aggregation.identity(this, identity, reduce); }
  default Maybe<Integer> count() { return Aggregation.count(this); }

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
