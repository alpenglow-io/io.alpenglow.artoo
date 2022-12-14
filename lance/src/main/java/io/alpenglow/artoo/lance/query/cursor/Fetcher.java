package io.alpenglow.artoo.lance.query.cursor;

import io.alpenglow.artoo.lance.query.FetchException;
import io.alpenglow.artoo.lance.query.Unit;

import java.util.Iterator;

public interface Fetcher<T> extends Iterator<T> {
  Unit<T> fetch() throws Throwable;

  @Override
  default T next() {
    try {
      return fetch().value();
    } catch (Throwable throwable) {
      throw FetchException.of(throwable);
    }
  }
}

