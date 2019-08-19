package oak.collect.cursor;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Objects;

import static java.util.Arrays.copyOf;
import static java.util.Objects.requireNonNull;
import static oak.type.Any.nonNullOrElse;

public interface Cursor<E> extends Iterator<E>, Enumeration<E> {
  static <T> Cursor<T> forward(T[] values) {
    return new Forward<>(copyOf(values, values.length));
  }
  static <T> Cursor<T> once(T value) { return new Once<>(value); }
  static <T> Cursor<T> none() {
    return new None<>();
  }
  static <T> Cursor<T> jump(final Iterator<T> first, final Iterator<T> next) {
    return new Jump<>(
      requireNonNull(first, "First cursor is null"),
      requireNonNull(next, "Next cursor is null")
    );
  }

  static <R> Cursor<R> maybe(final R value) {
    return nonNullOrElse(value, Cursor::once, Cursor::none);
  }

  static <T> Cursor<T> maybe(final Iterable<T> iterable) {
    return maybe(requireNonNull(iterable, "Iterable is null").iterator().next());
  }

  @Override
  default boolean hasMoreElements() {
    return hasNext();
  }

  @Override
  default E nextElement() {
    return next();
  }

  @Override
  default Iterator<E> asIterator() {
    return this;
  }
}
