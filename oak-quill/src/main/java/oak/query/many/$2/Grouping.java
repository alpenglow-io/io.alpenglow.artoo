package oak.query.many.$2;

import oak.func.$3.Pred;
import oak.query.$3.Queryable;
import oak.query.many.Many;

public interface Grouping<T1, T2, R> extends oak.query.$3.Queryable<T1, T2, Many<R>> {
  default Queryable<T1, T2, Many<R>> having(final Pred<? super T1, ? super T2, ? super Many<R>> where) {
    return null;
  }
}

/*
final class GroupBy<M, T1, T2> implements Grouping<T1, T2, R, M> {
  private final Comparator<? super Tuple2<? extends T1, ? extends T2>> comparator = (first, second) -> compare(second.hashCode(), first.hashCode());

  private final Queryable<M> queryable;
  private final Func<? super M, ? extends T1> key1;
  private final Func<? super M, ? extends T2> key2;

  @Contract(pure = true)
  GroupBy2(final Queryable<M> queryable, final Func<? super M, ? extends T1> key1, final Func<? super M, ? extends T2> key2) {
    this.queryable = queryable;
    this.key1 = key1;
    this.key2 = key2;
  }

  @NotNull
  @Override
  public final Iterator<Tuple3<T1, T2, Collection<M>>> iterator() {
    final var map = new TreeMap<Tuple2<? extends T1, ? extends T2>, Collection<M>>(comparator);
    for (final var value : queryable) {
      final var key = Tuple.of(key1.apply(value), key2.apply(value));
      map.putIfAbsent(key, new ArrayList<>());
      map.get(key).add(value);
    }
    final var array = new ArrayList<Tuple3<T1, T2, Collection<M>>>();
    for (final var entry : map.entrySet()) {
      entry
        .getKey()
        .peek((k1, k2) -> array.add(Tuple.of(k1, k2, entry.getValue())));
    }
    return array.iterator();
  }
}

*/
