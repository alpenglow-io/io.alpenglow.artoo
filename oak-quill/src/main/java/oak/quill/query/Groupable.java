package oak.quill.query;

import oak.collect.Many;
import oak.func.fun.Function1;
import oak.func.pre.Predicate2;
import oak.quill.Structable;
import oak.quill.query.tuple.Queryable2;
import oak.quill.tuple.Tuple;
import oak.quill.tuple.Tuple2;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

import static java.lang.Integer.compare;
import static oak.func.fun.Function1.identity;

public interface Groupable<T> extends Structable<T> {
  default <K> Grouping<K, T> groupBy(final Function1<? super T, ? extends K> key) {
    return new GroupBy<>(this, key);
  }

  interface Grouping<K, T> extends Queryable2<K, Collection<T>> {
    default Queryable2<K, Collection<T>> having(final Predicate2<? super K, ? super Collection<T>> filter) {
      return where(filter);
    }
  }
}

final class GroupBy<T, K> implements Groupable.Grouping<K, T> {
  private final Comparator<? super K> comparator = (first, second) -> compare(second.hashCode(), first.hashCode());

  private final Structable<T> structable;
  private final Function1<? super T, ? extends K> key;

  @Contract(pure = true)
  GroupBy(final Structable<T> structable, final Function1<? super T, ? extends K> key) {
    this.structable = structable;
    this.key = key;
  }

  @NotNull
  @Override
  public final Iterator<Tuple2<K, Collection<T>>> iterator() {
    final var map = new TreeMap<K, Collection<T>>(comparator);
    for (final var value : structable) {
      final var k = key.apply(value);
      map.putIfAbsent(k, new ArrayList<>());
      map.get(k).add(value);
    }
    final var array = Many.<Tuple2<K, Collection<T>>>of();
    for (final var entry : map.entrySet()) array.add(Tuple.of(entry.getKey(), entry.getValue()));
    return array.iterator();
  }
}
