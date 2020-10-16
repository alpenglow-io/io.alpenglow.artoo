package io.artoo.lance.fetcher.cursor;

import io.artoo.lance.fetcher.Cursor;
import io.artoo.lance.fetcher.Fetcher;
import io.artoo.lance.fetcher.routine.Routine;

public interface Invoker<T> extends Fetcher<T> {
  <R> Cursor<R> invoke(Routine<T, R> routine);
}

