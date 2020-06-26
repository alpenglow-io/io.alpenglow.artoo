package io.artoo.lance.query.cursor;

import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Pred;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

import static io.artoo.lance.type.Nullability.nonNullable;
import static io.artoo.lance.type.Nullability.nullable;

@SuppressWarnings("unchecked")
public interface Cursor<T> extends Iterator<T> {
  @SafeVarargs
  @NotNull
  @Contract("_ -> new")
  static <T> Cursor<T> pipe(final T... elements) {
    return new Pipe<>(elements);
  }

  static <R> Cursor<R> from(Iterator<R> iterator) {
    return new Cursor<R>() {
      @Override
      public Cursor<R> append(final R element) {
        return null;
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
  default boolean next(Pred.Uni<T> then) {
    var still = hasNext();
    final var next = next();
    if (next != null) {
      still &= then.test(next);
    }
    return still;
  }

  default Throwable cause() { return null; }
  default int size() { return 0; }

  default Cursor<T> peek(Cons.Uni<? super T> peek) { return new Peek<>(this, peek); }
  default Cursor<T> beep(Cons.Uni<Throwable> beep) { return new Beep<>(this, beep); }

  default Cursor<T> halt(Throwable cause) { return new Halt<>(cause); }
  default boolean hasHalted() { return false; }

  static <R> Cursor<R> of(final R value) {
    return nullable(value, Pipe::new, Cursor::empty);
  }

  @NotNull
  static <R> Cursor<R> empty() {
    return new Pipe<>((R[]) new Object[0]);
  }
}

