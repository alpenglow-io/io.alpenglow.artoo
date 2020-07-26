package io.artoo.lance.query;

import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.cursor.Hand;

public interface Taskable<T> extends Queryable<T> {
/*  Hand<T> hand() throws Throwable;

  @Override
  default Cursor<T> cursor() throws Throwable {
    return hand();
  }*/
}
