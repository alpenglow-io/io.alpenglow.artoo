package io.alpenglow.artoo.lance.query;

import java.util.Iterator;

/**
 * Literator stands for Lambda Iterator
 * @param <T>
 */
public interface Repeatable<T> extends Iterator<T> {
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

