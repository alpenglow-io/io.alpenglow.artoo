package lance.literator;

import lance.literator.cursor.Closable;
import lance.literator.cursor.Iteration;
import lance.literator.cursor.Link;
import lance.literator.cursor.Mappable;
import lance.literator.cursor.Nothing;
import lance.literator.cursor.Open;
import lance.literator.cursor.Substitutable;
import lance.literator.cursor.Transformable;

import java.util.Iterator;

public interface Cursor<T> extends Mappable<T>, Substitutable<T>, Closable<T>, Transformable<T> {
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

