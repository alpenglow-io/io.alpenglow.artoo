package io.artoo.query.many;

import io.artoo.query.Many;
import io.artoo.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static io.artoo.type.Nullability.nonNullable;
import static java.lang.Integer.compare;

public interface Groupable<T extends Record> extends Queryable<T> {
  default <K> Grouping<K, T> groupBy(final Function<? super T, ? extends K> key) {
    return new GroupBy<>(this, (i, it) -> {}, nonNullable(key, "key"));
  }
}

final class GroupBy<K, T> implements Grouping<K, T> {
  private final Comparator<? super K> comparator = (first, second) -> compare(second.hashCode(), first.hashCode());

  private final Queryable<T> queryable;
  private final BiConsumer<? super Integer, ? super T> peek;
  private final Function<? super T, ? extends K> key;

  @Contract(pure = true)
  public GroupBy(final Queryable<T> queryable, final BiConsumer<? super Integer, ? super T> peek, final Function<? super T, ? extends K> key) {
    this.queryable = queryable;
    this.peek = peek;
    this.key = key;
  }

  @NotNull
  @Override
  public final Iterator<Bag<K, T>> iterator() {
    final var map = new TreeMap<K, Many<T>>(comparator);
    var index = 0;
    for (var cursor = queryable.iterator(); cursor.hasNext(); index++) {
      var it = cursor.next();
      peek.accept(index, it);
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
  }
}

final class Having<K, T> implements Many<Grouping.Bag<K, T>> {
  private final Queryable<Grouping.Bag<K, T>> queryable;
  private final BiPredicate<? super K, ? super Many<T>> having;

  @Contract(pure = true)
  public Having(final Queryable<Grouping.Bag<K, T>> queryable, final BiPredicate<? super K, ? super Many<T>> having) {
    this.queryable = queryable;
    this.having = having;
  }

  @NotNull
  @Override
  public Iterator<Grouping.Bag<K, T>> iterator() {
    final var result = new ArrayList<Grouping.Bag<K, T>>();
    for (final var bag : queryable) {
      if (bag.as(having::test)) {
        result.add(bag);
      }
    }
    return result.iterator();
  }
}
