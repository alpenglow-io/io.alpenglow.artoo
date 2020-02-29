package oak.query.many;

import oak.NotImplementedYetException;
import oak.func.$2.IntCons;
import oak.func.$2.Pred;
import oak.func.Func;
import oak.query.Queryable;
import oak.query.many.$2.Many;
import oak.union.$2.Union;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

import static java.lang.Integer.compare;
import static oak.type.Nullability.nonNullable;

public interface Groupable<T> extends Queryable<T> {
  default <K> Grouping<K, T> groupBy(final Func<? super T, ? extends K> key) {
    return new GroupBy<>(this, IntCons.nothing(), nonNullable(key, "key"));
  }

  default <K1, K2> oak.query.many.$2.Grouping<K1, K2, T> groupBy(
    final Func<? super T, ? extends K1> key1,
    final Func<? super T, ? extends K2> key2
  ) {
    throw new NotImplementedYetException("groupBy two keys");
    //return new GroupBy2<>(this, key1, key2);
  }


  @FunctionalInterface
  interface Grouping<K, T> extends oak.collect.$2.Iterable<K, oak.query.Many<T>> {
    default Many<K, oak.query.Many<T>> having(final Pred<? super K, ? super oak.query.Many<T>> having) {
      return () -> {
        final var result = new ArrayList<Union<K, oak.query.Many<T>>>();
        for (final var union : this) {
          if (union.as(having::test)) {
            result.add(union);
          }
        }
        return result.iterator();
      };
    }
  }
}

final class GroupBy<K, T> implements Groupable.Grouping<K, T> {
  private final Comparator<? super K> comparator = (first, second) -> compare(second.hashCode(), first.hashCode());

  private final Queryable<T> queryable;
  private final IntCons<? super T> peek;
  private final Func<? super T, ? extends K> key;

  @Contract(pure = true)
  GroupBy(final Queryable<T> queryable, final IntCons<? super T> peek, final Func<? super T, ? extends K> key) {
    this.queryable = queryable;
    this.peek = peek;
    this.key = key;
  }

  @NotNull
  @Override
  public final Iterator<Union<K, oak.query.Many<T>>> iterator() {
    final var map = new TreeMap<K, oak.query.Many<T>>(comparator);
    var index = 0;
    for (var cursor = queryable.iterator(); cursor.hasNext(); index++) {
      var it = cursor.next();
      peek.acceptInt(index, it);
      if (it != null) {
        final var k = key.apply(it);
        map.putIfAbsent(k, oak.query.Many.none());
        //noinspection unchecked
        map.put(k, map.get(k).insert(it));
      }
    }
    final var array = new ArrayList<Union<K, oak.query.Many<T>>>();
    for (final var entry : map.entrySet())
      array.add(Union.of(entry.getKey(), entry.getValue()));
    return array.iterator();
  }
}
