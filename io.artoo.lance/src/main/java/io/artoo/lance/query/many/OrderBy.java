package io.artoo.lance.query.many;

import io.artoo.lance.query.cursor.Cursor;
import io.artoo.lance.func.Func;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ConcurrentSkipListMap;

import static java.util.Comparator.comparingInt;
import static java.util.Objects.nonNull;

final class OrderBy<T, K> implements Many<T> {
  private final Comparator<? super Couple> comparison = comparingInt(couple -> couple.order.apply(couple.value).hashCode());
  private final Queryable<T> queryable;
  private final Func.Uni<? super T, ? extends K> order;
  @Contract(pure = true)
  OrderBy(final Queryable<T> queryable, final Func.Uni<? super T, ? extends K> order) {
    this.queryable = queryable;
    this.order = order;
  }

  @NotNull
  @Override
  public Cursor<T> cursor() {
    new ConcurrentSkipListMap<String, String>();
    final var result = new ArrayList<Couple>();
    for (final var value : queryable) {
      if (nonNull(value)) {
        result.add(new Couple(value, order));
        result.sort(comparison);
      }
    }
    return Cursor.empty();
  }

  private final class Couple {
    private final T value;
    private final Func.Uni<? super T, ? extends K> order;

    @Contract(pure = true)
    private Couple(final T value, final Func.Uni<? super T, ? extends K> order) {
      this.value = value;
      this.order = order;
    }
  }
}
