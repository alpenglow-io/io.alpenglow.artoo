package io.artoo.lance.query.operation;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;

public interface Where {
  static <T> Func.Uni<T, T> on(final Index index, final Pred.Bi<? super Integer, ? super T> where) {
    return it -> where.tryTest(index.value++, it) ? it : null;
  }

  static <T> Func.Uni<T, T> on(final Pred.Uni<? super T> where) {
    return it -> where.tryTest(it) ? it : null;
  }

  static <T, R> Func.Uni<T, T> on(final Class<? extends R> type) {
    return it -> type.isInstance(it) ? it : null;
  }
}
