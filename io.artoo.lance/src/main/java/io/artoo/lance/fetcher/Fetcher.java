package io.artoo.lance.fetcher;

import java.util.Iterator;

public interface Fetcher<T> extends Iterator<T> {
  T fetch() throws Throwable;

  default Fetcher<T> fetcher() { return null; }
  default Index index() { return fetcher() != null ? fetcher().index() : null; }
  default T[] elements() { return fetcher() != null ? fetcher().elements() : null; }

  @Override
  default boolean hasNext() {
    return fetcher() != null ? fetcher().hasNext() : index().value() < elements().length;
  }

  @Override
  default T next() {
    try {
      return fetch();
    } catch (Throwable throwable) {
      throw new CursorException(throwable);
    }
  }
}
