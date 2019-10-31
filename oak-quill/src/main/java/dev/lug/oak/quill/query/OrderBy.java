package dev.lug.oak.quill.query;

import dev.lug.oak.func.fun.Function1;
import dev.lug.oak.quill.Structable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import static dev.lug.oak.quill.Q.P.just;
import static dev.lug.oak.quill.query.Queryable.from;
import static java.util.Comparator.comparingInt;
import static java.util.Objects.nonNull;

final class OrderBy<T, K> implements Queryable<T> {
  private final class Couple {
    private final T value;
    private final Function1<? super T, ? extends K> order;

    @Contract(pure = true)
    private Couple(final T value, final Function1<? super T, ? extends K> order) {
      this.value = value;
      this.order = order;
    }
  }

  private final Comparator<? super Couple> comparison = (Comparator<Couple>) (first, second) -> {
    final var f = first.order.apply(first.value).hashCode();
    final var s = second.order.apply(second.value).hashCode();
    return f > s ? 1 : f == s ? 0 : -1;
  };

  private final Structable<T> structable;
  private final Function1<? super T, ? extends K> order;

  @Contract(pure = true)
  OrderBy(final Structable<T> structable, final Function1<? super T, ? extends K> order) {
    this.structable = structable;
    this.order = order;
  }

  @NotNull
  @Override
  public Iterator<T> iterator() {
    final var result = new ArrayList<Couple>();
    for (final var value : structable) {
      if (nonNull(value)) {
        result.add(new Couple(value, order));
        result.sort(comparison);
      }
    }
    return from(result).select(just(it -> it.value)).iterator();
  }
}
