package io.artoo.query.many.impl;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import io.artoo.func.$2.ConsInt;
import io.artoo.func.Func;
import io.artoo.query.Many;
import io.artoo.query.Queryable;
import io.artoo.query.many.Grouping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

import static java.lang.Integer.compare;
import static io.artoo.type.Nullability.nonNullable;

public final class GroupBy<K, T> implements Grouping<K, T> {
  private final Comparator<? super K> comparator = (first, second) -> compare(second.hashCode(), first.hashCode());

  private final Queryable<T> queryable;
  private final ConsInt<? super T> peek;
  private final Func<? super T, ? extends K> key;

  @Contract(pure = true)
  public GroupBy(final Queryable<T> queryable, final ConsInt<? super T> peek, final Func<? super T, ? extends K> key) {
    this.queryable = queryable;
    this.peek = peek;
    this.key = key;
  }

  @NotNull
  @Override
  public final Iterator<Grouping.Bag<K, T>> iterator() {
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
    final var array = new ArrayList<Bag<K, T>>();
    for (final var entry : map.entrySet())
      array.add(new GroupingBag(entry.getKey(), entry.getValue()));
    return array.iterator();
  }

  final class GroupingBag implements Bag<K, T> {
    private final K value;
    private final Many<T> many;

    GroupingBag(final K value, final Many<T> many) {
      this.value = value;
      this.many = many;
    }

    @Override
    public <R> R as(io.artoo.func.$2.Func<K, Many<T>, R> as) {
      return nonNullable(as, "as").apply(this.value, this.many);
    }
  }
}
