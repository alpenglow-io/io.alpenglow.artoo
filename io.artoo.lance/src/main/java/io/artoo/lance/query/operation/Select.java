package io.artoo.lance.query.operation;

import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.func.Func;
import io.artoo.lance.query.Queryable;

import static io.artoo.lance.query.operation.Index.indexed;

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
