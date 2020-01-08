package dev.lug.oak.query.many;

import dev.lug.oak.collect.Many;
import dev.lug.oak.func.fun.Function1;
import dev.lug.oak.func.pre.Predicate2;
import dev.lug.oak.func.pre.Predicate3;
import dev.lug.oak.query.Structable;
import dev.lug.oak.query.many.tuple.Queryable2;
import dev.lug.oak.query.many.tuple.Queryable3;
import dev.lug.oak.query.tuple.Tuple;
import dev.lug.oak.query.tuple.Tuple2;
import dev.lug.oak.query.tuple.Tuple3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

import static java.lang.Integer.compare;

public interface Groupable<T> extends Structable<T> {
  default <K> Grouping<K, T> groupBy(final Function1<? super T, ? extends K> key) {
    return new GroupBy<>(this, key);
  }

  default <K1, K2> Grouping2<K1, K2, T> groupBy(
    final Function1<? super T, ? extends K1> key1,
    final Function1<? super T, ? extends K2> key2
  ) {
    return new GroupBy2<>(this, key1, key2);
  }

  interface Grouping<K, T> extends Queryable2<K, Collection<T>> {
    default Queryable2<K, Collection<T>> having(final Predicate2<? super K, ? super Collection<T>> filter) {
      return where(filter);
    }
  }

  interface Grouping2<K1, K2, T> extends Queryable3<K1, K2, Collection<T>> {
    default Queryable3<K1, K2, Collection<T>> having(final Predicate3<? super K1, ? super K2, ? super Collection<T>> filter) {
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

final class GroupBy2<T, K1, K2> implements Groupable.Grouping2<K1, K2, T> {
  private final Comparator<? super Tuple2<? extends K1, ? extends K2>> comparator = (first, second) -> compare(second.hashCode(), first.hashCode());

  private final Structable<T> structable;
  private final Function1<? super T, ? extends K1> key1;
  private final Function1<? super T, ? extends K2> key2;

  @Contract(pure = true)
  GroupBy2(final Structable<T> structable, final Function1<? super T, ? extends K1> key1, final Function1<? super T, ? extends K2> key2) {
    this.structable = structable;
    this.key1 = key1;
    this.key2 = key2;
  }

  @NotNull
  @Override
  public final Iterator<Tuple3<K1, K2, Collection<T>>> iterator() {
    final var map = new TreeMap<Tuple2<? extends K1, ? extends K2>, Collection<T>>(comparator);
    for (final var value : structable) {
      final var key = Tuple.of(key1.apply(value), key2.apply(value));
      map.putIfAbsent(key, new ArrayList<>());
      map.get(key).add(value);
    }
    final var array = Many.<Tuple3<K1, K2, Collection<T>>>of();
    for (final var entry : map.entrySet()) {
      entry
        .getKey()
        .peek((k1, k2) -> array.add(Tuple.of(k1, k2, entry.getValue())));
    }
    return array.iterator();
  }
}
