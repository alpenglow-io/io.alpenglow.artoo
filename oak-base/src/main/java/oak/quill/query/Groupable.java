package oak.quill.query;

import oak.collect.Array;
import oak.func.fun.Function1;
import oak.func.fun.Function2;
import oak.quill.Structable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

import static java.lang.Integer.compare;
import static oak.func.fun.Function1.identity;
import static oak.type.Nullability.nonNullable;

public interface Groupable<T> extends Structable<T> {
  default <K, E, R> Queryable<R> groupByComparing(
    final Function1<? super T, ? extends K> key,
    final Function1<? super T, ? extends E> element,
    final Function2<? super K, ? super Collection<? extends E>, ? extends R> result,
    final Comparator<? super K> comparator
  ) {
    return new GroupBy<>(this,
      nonNullable(key, "key"),
      nonNullable(element, "element"),
      nonNullable(result, "result"),
      nonNullable(comparator, "comparator")
    );
  }

  default <K, E, R> Queryable<R> groupBy(
    final Function1<? super T, ? extends K> key,
    final Function1<? super T, ? extends E> element,
    final Function2<? super K, ? super Collection<? extends E>, ? extends R> result
  ) {
    return groupByComparing(key, element, result, (first, second) -> compare(second.hashCode(), first.hashCode()));
  }

  default <K, E> Queryable<Grouping<K, ? extends E>> groupBy(
    final Function1<? super T, ? extends K> key,
    final Function1<? super T, ? extends E> element
  ) {
    return this.<K, E, Grouping<K, ? extends E>>groupBy(key, element, Grouping::new);
  }

  default <K, E> Queryable<Grouping<K, ? extends E>> groupByComparing(
    final Function1<? super T, ? extends K> key,
    final Function1<? super T, ? extends E> element,
    final Comparator<? super K> comparator
  ) {
    return this.<K, E, Grouping<K, ? extends E>>groupByComparing(key, element, Grouping::new, nonNullable(comparator, "comparator"));
  }

  default <K> Queryable<Grouping<K, ? extends T>> groupByComparing(
    final Function1<? super T, ? extends K> key,
    final Comparator<? super K> comparator
  ) {
    return this.<K, T, Grouping<K, ? extends T>>groupByComparing(key, identity(), Grouping::new, nonNullable(comparator, "comparator"));
  }

  default <K> Queryable<Grouping<K, ? extends T>> groupBy(
    final Function1<? super T, ? extends K> key
  ) {
    return this.<K, T, Grouping<K, ? extends T>>groupByComparing(key, identity(), Grouping::new, (first, second) -> compare(second.hashCode(), first.hashCode()));
  }

  final class Grouping<K, E> {
    private final K key;
    private final Collection<? extends E> elements;

    @Contract(pure = true)
    private Grouping(final K key, final Collection<? extends E> elements) {
      this.key = key;
      this.elements = elements;
    }

    @Contract(pure = true)
    public final K key() { return this.key; }
    @Contract(pure = true)
    public final Collection<? extends E> elements() { return this.elements; }
  }
}

final class GroupBy<T, K, E, R> implements Queryable<R> {
  private final Structable<T> structable;
  private final Function1<? super T, ? extends K> key;
  private final Function1<? super T, ? extends E> element;
  private final Function2<? super K, ? super Collection<? extends E>, ? extends R> result;
  private final Comparator<? super K> comparator;

  @Contract(pure = true)
  GroupBy(
    final Structable<T> structable,
    final Function1<? super T, ? extends K> key,
    final Function1<? super T, ? extends E> element,
    final Function2<? super K, ? super Collection<? extends E>, ? extends R> result,
    final Comparator<? super K> comparator
  ) {
    this.structable = structable;
    this.key = key;
    this.element = element;
    this.result = result;
    this.comparator = comparator;
  }

  @NotNull
  @Override
  public final Iterator<R> iterator() {
    final var map = new TreeMap<K, Collection<E>>(comparator);
    for (final var value : structable) {
      final var k = key.apply(value);
      map.putIfAbsent(k, new ArrayList<>());
      map.get(k).add(element.apply(value));
    }
    final var array = Array.<R>of();
    for (final var entry : map.entrySet()) array.add(result.apply(entry.getKey(), entry.getValue()));
    return array.iterator();
  }
}
