package io.artoo.lance.query.oper;

import io.artoo.lance.func.Func;

public final class Select<T, R> implements Func.Uni<T, R> {
  private final Func.Bi<? super Integer, ? super T, ? extends R> select;
  private final Selected selected;

  public Select(final Func.Bi<? super Integer, ? super T, ? extends R> select) {
    assert select != null;
    this.select = select;
    this.selected = new Selected();
  }

  @Override
  public R tryApply(final T element) throws Throwable {
    return select.tryApply(selected.index++, element);
  }

  private final class Selected {
    private int index = 0;
  }
}
