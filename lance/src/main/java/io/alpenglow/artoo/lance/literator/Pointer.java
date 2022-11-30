package io.alpenglow.artoo.lance.literator;

import java.util.Iterator;

/**
 * Literator stands for Lambda Iterator
 * @param <T>
 */
public interface Pointer<T> extends Iterator<T> {
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

