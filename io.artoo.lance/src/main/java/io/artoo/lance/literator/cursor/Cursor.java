package io.artoo.lance.literator.cursor;

import io.artoo.lance.literator.Literator;
import io.artoo.lance.literator.cursor.impl.Iteration;
import io.artoo.lance.literator.cursor.impl.Link;
import io.artoo.lance.literator.cursor.impl.Nothing;
import io.artoo.lance.literator.cursor.impl.Open;

import java.util.Iterator;

public interface Cursor<T> extends Mapper<T>, Other<T>, Closer<T>, Transformer<T> {
  @SafeVarargs
  static <T> Cursor<T> open(final T... elements) {
    return new Open<>(elements);
  }

  static <T> Cursor<T> link(final Literator<T> prev, final Literator<T> next) {
    return new Link<>(prev, next);
  }

  @SuppressWarnings("unchecked")
  static <T> Cursor<T> nothing() {
    return (Cursor<T>) Nothing.Default;
  }

  static <T> Cursor<T> maybe(final T element) {
    return element == null ? nothing() : open(element);
  }

  static <T> Cursor<T> iteration(final Iterator<T> iterator) {
    return new Iteration<>(iterator);
  }
}

