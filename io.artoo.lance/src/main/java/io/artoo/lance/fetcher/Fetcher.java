package io.artoo.lance.fetcher;

import java.util.Iterator;

public interface Fetcher<T> extends Iterator<T> {
  T fetch() throws Throwable;

  @Override
  default T next() {
    try {
      return fetch();
    } catch (Throwable throwable) {
      throw FetchException.wrapping(throwable);
    }
  }
}

