package oak.query.many;

import oak.func.$2.Pred;
import oak.func.Func;
import oak.query.Queryable;
import oak.query.many.$2.Many;
import oak.union.$2.Union;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeMap;

import static java.lang.Integer.compare;

public interface Groupable<T> extends Queryable<T> {
  default <K> Grouping<K, T> groupBy(final Func<? super T, ? extends K> key) {
    return () -> {
      final var map = new TreeMap<K, Collection<T>>((Comparator<? super K>) (first, second) -> compare(second.hashCode(), first.hashCode()));
      for (final var value : this) {
        final var k = key.apply(value);
        map.putIfAbsent(k, new ArrayList<>());
        map.get(k).add(value);
      }
      final var array = new ArrayList<Union<K, Collection<T>>>();
      for (final var entry : map.entrySet())
        array.add(Union.of(entry.getKey(), entry.getValue()));
      return array.iterator();
    };
  }
/*

  default <K1, K2> Grouping2<K1, K2, T> groupBy(
    final Func<? super T, ? extends K1> key1,
    final Func<? super T, ? extends K2> key2
  ) {
    return new GroupBy2<>(this, key1, key2);
  }
*/

  @FunctionalInterface
  interface Grouping<K, T> extends oak.collect.$2.Iterable<K, Collection<T>> {
    default Many<K, Collection<T>> having(final Pred<? super K, ? super Collection<T>> having) {
      return () -> {
        final var result = new ArrayList<Union<K, Collection<T>>>();
        for (final var union : this) {
          if (union.as(having::test)) {
            result.add(union);
          }
        }
        return result.iterator();
      };
    }
  }

/*  interface Grouping2<K1, K2, T> extends Queryable3<K1, K2, Collection<T>> {
    default Queryable3<K1, K2, Collection<T>> having(final Pre<? super K1, ? super K2, ? super Collection<T>> filter) {
      return where(filter);
    }
  }*/
}
/*

final class GroupBy2<T, K1, K2> implements Groupable.Grouping2<K1, K2, T> {
  private final Comparator<? super Tuple2<? extends K1, ? extends K2>> comparator = (first, second) -> compare(second.hashCode(), first.hashCode());

  private final Queryable<T> queryable;
  private final Func<? super T, ? extends K1> key1;
  private final Func<? super T, ? extends K2> key2;

  @Contract(pure = true)
  GroupBy2(final Queryable<T> queryable, final Func<? super T, ? extends K1> key1, final Func<? super T, ? extends K2> key2) {
    this.queryable = queryable;
    this.key1 = key1;
    this.key2 = key2;
  }

  @NotNull
  @Override
  public final Iterator<Tuple3<K1, K2, Collection<T>>> iterator() {
    final var map = new TreeMap<Tuple2<? extends K1, ? extends K2>, Collection<T>>(comparator);
    for (final var value : queryable) {
      final var key = Tuple.of(key1.apply(value), key2.apply(value));
      map.putIfAbsent(key, new ArrayList<>());
      map.get(key).add(value);
    }
    final var array = new ArrayList<Tuple3<K1, K2, Collection<T>>>();
    for (final var entry : map.entrySet()) {
      entry
        .getKey()
        .peek((k1, k2) -> array.add(Tuple.of(k1, k2, entry.getValue())));
    }
    return array.iterator();
  }
}
*/
