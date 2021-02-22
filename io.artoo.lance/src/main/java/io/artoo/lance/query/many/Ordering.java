package io.artoo.lance.query.many;

import io.artoo.lance.func.Func;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.literator.cursor.routine.sort.Sort;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;

import java.util.Arrays;

import static io.artoo.lance.literator.cursor.routine.sort.Sort.By;
import static io.artoo.lance.literator.cursor.routine.sort.Sort.byDefault;
import static io.artoo.lance.query.many.Ordering.Arrange.asc;

public sealed interface Ordering<T> extends Many<T> {
  enum Arrange {asc, desc}

  default <R> Ordering<T> by(Func.Uni<? super T, ? extends R> field) {
    return by(field, asc);
  }

  default <R> Ordering<T> by(Func.Uni<? super T, ? extends R> field, Arrange arrange) {
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
  public <R> Ordering<T> by(final Func.Uni<? super T, ? extends R> field, final Arrange arrange) {
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
  public <R> Ordering<T> by(final Func.Uni<? super T, ? extends R> field, final Arrange arrange) {
    final var copied = Arrays.copyOf(bys, bys.length + 1);
    copied[bys.length] = By.with(field, arrange);
    return new OrderBy<>(queryable, copied);
  }
}

