package io.artoo.lance.query.internal;

import io.artoo.lance.func.Func;

import java.util.concurrent.atomic.AtomicReference;

public final class Select<T, R> implements Func.Uni<T, Select.Selected<T, R, Select<T, R>>> {
  public record Selected<T, R, F extends Func.Uni<T, Selected<T, R, F>>>(F select, R value) {}
  private final Func.Bi<? super Integer, ? super T, ? extends R> select;

  private final int index;
  private Select(final Func.Bi<? super Integer, ? super T, ? extends R> select) {
    this(0, select);
  }

  private Select(final int index, final Func.Bi<? super Integer, ? super T, ? extends R> select) {
    assert select != null;
    this.index = index;
    this.select = select;
  }

  @Override
  public Selected<T, R, Select<T, R>> tryApply(final T t) throws Throwable {
    return
      new Selected<>(
        new Select<>(index + 1, select),
        select.tryApply(index, t)
      );
  }

  public static <T, R> AtomicReference<Select<T, R>> reference(final Func.Uni<? super T, ? extends R> select) {
    return reference((i, it) -> select.tryApply(it));
  }

  public static <T, R> AtomicReference<Select<T, R>> reference(final Func.Bi<? super Integer, ? super T, ? extends R> select) {
    return new AtomicReference<>(new Select<>(select));
  }
}
