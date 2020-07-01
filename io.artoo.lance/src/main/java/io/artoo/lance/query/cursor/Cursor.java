package io.artoo.lance.query.cursor;

import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;

import java.util.Iterator;

import static io.artoo.lance.type.Nullability.nonNullable;

public interface Cursor<T> extends Iterator<T> {
  static <R> Cursor<R> from(Iterator<R> iterator) {
    return new Cursor<R>() {
      @Override
      public Cursor<R> append(final R element) {
        return null;
      }

      @Override
      public Cursor<R> set(final R... elements) {
        return null;
      }

      @Override
      public Cursor<R> grab(final Throwable cause) {
        return null;
      }

      @Override
      public Cursor<R> scroll() {
        return null;
      }

      @Override
      public boolean has(final R element) {
        return true;
      }

      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public R next() {
        return iterator.next();
      }
    };
  }

  Cursor<T> append(T element);

  default Throwable cause() { return null; }
  default boolean hasCause() { return false; }

  Cursor<T> set(final T... elements);
  Cursor<T> grab(final Throwable cause);

  Cursor<T> scroll();
  boolean has(T element);

  default <R> R fetch(final Func.Uni<T, R> then) throws Throwable {
    final var next = next();
    if (next != null) {
      return then.tryApply(next);
    }
    return null;
  }

  default int size() { return 0; }

  default Cursor<T> peek(Cons.Uni<? super T> peek) { return new Peek<>(this, peek); }
  default Cursor<T> beep(Cons.Uni<? super Throwable> beep) { return new Beep<>(this, beep); }

  @SafeVarargs
  static <R> Cursor<R> local(final R... elements) {
    return new Local<>(nonNullable(elements, "elements"));
  }
}

