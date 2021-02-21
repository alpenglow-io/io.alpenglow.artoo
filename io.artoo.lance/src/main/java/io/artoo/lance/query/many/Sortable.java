package io.artoo.lance.query.many;

import io.artoo.lance.literator.cursor.routine.Routine;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.Queryable;

public interface Sortable<T> extends Queryable<T> {
  default Many<T> orderByHashcode() {
    return () -> cursor().to(Routine.<T>orderByHashcode());
  }
}
