package io.alpenglow.artoo.lance.query.cursor;

import io.alpenglow.artoo.lance.query.FetchException;

import java.util.Iterator;

public interface Fetcher<T> extends Iterator<T> {
  T fetch() throws Throwable;

  @Override
  default T next() {
    try {
      return fetch();
    } catch (Throwable throwable) {
      throw FetchException.of(throwable);
    }
  }
}

