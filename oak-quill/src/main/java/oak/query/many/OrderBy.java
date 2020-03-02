package oak.query.many;

import oak.func.Func;
import oak.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListMap;

import static oak.query.Queryable.P.as;
import static oak.query.many.Many.from;
import static java.util.Comparator.comparingInt;
import static java.util.Objects.nonNull;

final class OrderBy<T, K> implements Many<T> {
  private final class Couple {
    private final T value;
    private final Func<? super T, ? extends K> order;

    @Contract(pure = true)
    private Couple(final T value, final Func<? super T, ? extends K> order) {
      this.value = value;
      this.order = order;
    }
  }

  private final Comparator<? super Couple> comparison = comparingInt(couple -> couple.order.apply(couple.value).hashCode());

  private final Queryable<T> queryable;
  private final Func<? super T, ? extends K> order;

  @Contract(pure = true)
  OrderBy(final Queryable<T> queryable, final Func<? super T, ? extends K> order) {
    this.queryable = queryable;
    this.order = order;
  }

  @NotNull
  @Override
  public Iterator<T> iterator() {
    new ConcurrentSkipListMap<String, String>();
    final var result = new ArrayList<Couple>();
    for (final var value : queryable) {
      if (nonNull(value)) {
        result.add(new Couple(value, order));
        result.sort(comparison);
      }
    }
    return from(result).select(P.as(it -> it.value)).iterator();
  }
}
