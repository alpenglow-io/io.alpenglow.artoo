package io.alpenglow.artoo.lance.query.cursor;

import io.alpenglow.artoo.lance.query.FetchException;

import java.util.Iterator;

/**
 * Literator stands for Lambda Iterator
 * @param <T>
 */
public sealed interface Source<T> extends Iterator<T> permits Closable, Mappable, Substitutable, Transformable {
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

