package oak.collect.query.sort;

import oak.func.fun.Function1;
import oak.collect.query.Queryable;

import java.util.Comparator;

import static java.util.Objects.requireNonNull;

public interface Sorting<T> extends Queryable<T> {
  static Sorting<Integer> orderBy(final Queryable<Integer> source, final Function1<Integer, Integer> key) {
    return orderBy(source, key, comparison);
  }

  static <S, K> Sorting<S> orderBy(final Queryable<S> source, final Function1<S, K> key, final Comparator<K> compare) {
    return new OrderBy<>(
      requireNonNull(source, "Source is null"),
      requireNonNull(key, "Key is null"),
      requireNonNull(compare, "Compare is null")
    );
  }

  Comparator<Integer> comparison = (v1, v2) -> v1 < v2
    ? -1
    : v1.equals(v2)
    ? 0
    : 1;
}
