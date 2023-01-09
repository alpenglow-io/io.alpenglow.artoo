package re.artoo.lance.query;

import re.artoo.lance.func.TryIntFunction1;

public interface IntClosure<S, T> extends TryIntFunction1<S, T> {
  T invoke(int integer, S element) throws Throwable;

  default <R> IntClosure<S, R> then(IntClosure<? super T, ? extends R> closure) {
    return (index, it) -> coalesce(index, coalesce(index, it, this), closure);
  }

  private <SOURCE, TARGET> TARGET coalesce(int index, SOURCE source, IntClosure<? super SOURCE, ? extends TARGET> closure) throws Throwable {
    return source == null ? null : closure.invoke(index, source);
  }
}

