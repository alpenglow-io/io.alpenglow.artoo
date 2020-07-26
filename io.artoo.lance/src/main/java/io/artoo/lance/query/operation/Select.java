package io.artoo.lance.query.operation;

import io.artoo.lance.func.Func;

public interface Select {
  static <T, R> Func.Uni<T, R> as(final Index index, final Func.Bi<? super Integer, ? super T, ? extends R> select) {
    return it -> select.tryApply(index.value++, it);
  }

  static <T, R> Func.Uni<T, R> as(final Func.Uni<? super T, ? extends R> select) {
    return select::tryApply;
  }

  static <T, R> Func.Uni<T, R> as(final Class<? extends R> type) {
    return type::cast;
  }
}
