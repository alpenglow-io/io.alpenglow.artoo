package oak.query.many;

import oak.func.Func;
import oak.query.Queryable;
import oak.type.As;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static oak.func.Func.identity;
import static oak.type.Nullability.nonNullable;

public interface Convertable<T> extends Queryable<T> {
  default @NotNull <K, E> Map<? extends K, ? extends E> asMap(final Func<? super T, ? extends K> key, final Func<? super T, ? extends E> element) {
    return new AsMap<>(this, nonNullable(key, "key"), nonNullable(element, "element")).eval();
  }

  default List<T> asList() {
    return new AsList<>(this).eval();
  }

/*  default T[] asArray(final IntFunction1<T[]> initializer) {
    return new AsArray<>(
      new Aggregate<>(
        this,
        tautology(),
        identity(),
        0,
        (acc, next) -> acc + 1
      ),
      this,
      nonNullable(initializer, "initializer")
    ).eval();
  }*/
}

final class AsMap<T, K, E> implements As<Map<K, E>> {
  private final Queryable<T> queryable;
  private final Func<? super T, ? extends K> key;
  private final Func<? super T, ? extends E> element;

  @Contract(pure = true)
  AsMap(final Queryable<T> queryable, final Func<? super T, ? extends K> key, final Func<? super T, ? extends E> element) {
    this.queryable = queryable;
    this.key = key;
    this.element = element;
  }

  @Override
  @NotNull
  public final Map<K, E> eval() {
    final var map = new ConcurrentHashMap<K, E>();
    for (final var value : queryable) {
      map.put(key.apply(value), element.apply(value));
    }
    return map;
  }
}

final class AsList<T> implements As<List<T>> {
  private final Queryable<T> queryable;

  @Contract(pure = true)
  AsList(final Queryable<T> queryable) {this.queryable = queryable;}

  @NotNull
  @Override
  public final List<T> eval() {
    final List<T> list = new ArrayList<>();
    for (final var value : queryable)
      list.add(value);
    return list;
  }
}
/*

final class AsArray<T> implements AsArray<T> {
  private final One<Integer> count;
  private final Queryable<T> queryable;
  private final IntFunction1<T[]> initializer;

  @Contract(pure = true)
  AsArray(final One<Integer> count, final Queryable<T> queryable, final IntFunction1<T[]> initializer) {
    this.count = count;
    this.queryable = queryable;
    this.initializer = initializer;
  }

  @Override
  public final T[] eval() {
    final var array = count.select(initializer).iterator().next();
    var index = 0;
    for (final var value : queryable) array[index++] = value;
    return array;
  }
}
*/
