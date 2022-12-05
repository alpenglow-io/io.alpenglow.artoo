package io.alpenglow.artoo.lance.query.many;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.cursor.routine.sort.Sort;
import io.alpenglow.artoo.lance.query.Many;
import io.alpenglow.artoo.lance.Queryable;

import java.util.Arrays;

import static io.alpenglow.artoo.lance.query.cursor.routine.sort.Sort.By;
import static io.alpenglow.artoo.lance.query.cursor.routine.sort.Sort.byDefault;
import static io.alpenglow.artoo.lance.query.many.Ordering.Arrange.asc;

public sealed interface Ordering<T> extends Many<T> {
  enum Arrange {asc, desc}

  default <R> Ordering<T> by(TryFunction1<? super T, ? extends R> field) {
    return by(field, asc);
  }

  default <R> Ordering<T> by(TryFunction1<? super T, ? extends R> field, Arrange arrange) {
    return new OrderBy<>(this, By.with(field, arrange));
  }
}

final class Order<T> implements Ordering<T> {
  private final Queryable<T> queryable;

  Order(final Queryable<T> queryable) {this.queryable = queryable;}

  @Override
  public Cursor<T> cursor() {
    return queryable.cursor().to(byDefault());
  }

  @Override
  public <R> Ordering<T> by(final TryFunction1<? super T, ? extends R> field, final Arrange arrange) {
    return new OrderBy<>(queryable, By.with(field, arrange));
  }
}

final class OrderBy<T> implements Ordering<T> {
  private final Queryable<T> queryable;
  private final By<T, Object>[] bys;

  @SafeVarargs
  OrderBy(final Queryable<T> queryable, final By<T, Object>... bys) {
    this.queryable = queryable;
    this.bys = bys;
  }

  @Override
  public Cursor<T> cursor() {
    return queryable.cursor().to(Sort.arranged(bys));
  }

  @Override
  public <R> Ordering<T> by(final TryFunction1<? super T, ? extends R> field, final Arrange arrange) {
    final var copied = Arrays.copyOf(bys, bys.length + 1);
    copied[bys.length] = By.with(field, arrange);
    return new OrderBy<>(queryable, copied);
  }
}

