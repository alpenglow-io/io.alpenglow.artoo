package oak.query.many.internal;

import oak.func.$2.IntCons;
import oak.func.Func;
import oak.query.Queryable;
import oak.query.many.Grouping;
import oak.query.Many;
import oak.union.$2.Union;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

import static java.lang.Integer.compare;

public final class GroupBy<K, T> implements Grouping<K, T> {
  private final Comparator<? super K> comparator = (first, second) -> compare(second.hashCode(), first.hashCode());

  private final Queryable<T> queryable;
  private final IntCons<? super T> peek;
  private final Func<? super T, ? extends K> key;

  @Contract(pure = true)
  public GroupBy(final Queryable<T> queryable, final IntCons<? super T> peek, final Func<? super T, ? extends K> key) {
    this.queryable = queryable;
    this.peek = peek;
    this.key = key;
  }

  @NotNull
  @Override
  public final Iterator<Union<K, Many<T>>> iterator() {
    final var map = new TreeMap<K, Many<T>>(comparator);
    var index = 0;
    for (var cursor = queryable.iterator(); cursor.hasNext(); index++) {
      var it = cursor.next();
      peek.acceptInt(index, it);
      if (it != null) {
        final var k = key.apply(it);
        map.putIfAbsent(k, Many.none());
        //noinspection unchecked
        map.put(k, map.get(k).insert(it));
      }
    }
    final var array = new ArrayList<Union<K, Many<T>>>();
    for (final var entry : map.entrySet())
      array.add(Union.of(entry.getKey(), entry.getValue()));
    return array.iterator();
  }
}
