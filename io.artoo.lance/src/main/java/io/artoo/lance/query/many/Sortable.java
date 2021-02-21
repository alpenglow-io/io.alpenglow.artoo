package io.artoo.lance.query.many;

import io.artoo.lance.func.Func;
import io.artoo.lance.literator.cursor.routine.Routine;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;

public interface Sortable<T> extends Queryable<T> {
  default Many<T> orderByHashcode() {
    return () -> cursor().to(Routine.orderByHashcode());
  }

  default <R> Many<T> orderBy(Func.Uni<? super T, ? extends R> field) {
    return () -> cursor().to(Routine.orderBy(field));
  }
}
