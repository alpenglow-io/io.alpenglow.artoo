package io.artoo.lance.query.operation;

import io.artoo.lance.func.Func;

public enum Select {;
  public static <T, R> Func.Uni<T, R> as(final Index index, final Func.Bi<? super Integer, ? super T, ? extends R> select) {
    assert select != null;
    return it -> select.tryApply(index.value++, it);
  }

  public static <T, R> Func.Uni<T, R> as(final Func.Uni<? super T, ? extends R> select) {
    assert select != null;
    return select::tryApply;
  }

  public static <T, R> Func.Uni<T, R> as(final Class<? extends R> type) {
    assert type != null;
    return type::cast;
  }
}
