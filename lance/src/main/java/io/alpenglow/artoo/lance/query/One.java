package io.alpenglow.artoo.lance.query;

import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.func.TryFunction;
import io.alpenglow.artoo.lance.func.TrySupplier;
import io.alpenglow.artoo.lance.literator.Cursor;
import io.alpenglow.artoo.lance.query.one.Elseable;
import io.alpenglow.artoo.lance.query.one.Filterable;
import io.alpenglow.artoo.lance.query.one.Peekable;
import io.alpenglow.artoo.lance.query.one.Projectable;

enum None implements One<Object> {
  Empty;

  @Override
  public final Cursor<Object> cursor() {
    return Cursor.nothing();
  }
}

public interface One<T> extends Projectable<T>, Peekable<T>, Filterable<T>, Elseable<T> {
  static <T> One<T> maybe(final T element) {
    return element != null ? One.lone(element) : One.none();
  }

  static <L> One<L> lone(final L element) {
    return new Lone<>(element);
  }

  static <T> One<T> from(final TrySupplier<T> supply) {
    return new Sone<>(supply);
  }

  static <T> One<T> gone(final String message, final TryFunction<? super String, ? extends RuntimeException> error) {
    return new Gone<>(message, error);
  }

  @SuppressWarnings("unchecked")
  static <L> One<L> none() {
    return (One<L>) None.Empty;
  }

  static <A extends AutoCloseable, T, O extends One<T>> One<T> done(TrySupplier<? extends A> going, TryFunction<? super A, ? extends O> then) {
    return new Done<>(going, then);
  }

  interface OfTwo<A, B> extends Queryable.OfTwo<A, B> {}
}

final class Lone<T> implements One<T> {
  private final T element;

  public Lone(final T element) {
    this.element = element;
  }

  @Override
  public Cursor<T> cursor() {
    return Cursor.open(element);
  }
}

final class Sone<T> implements One<T> {
  private final TrySupplier<T> suppl;

  public Sone(final TrySupplier<T> suppl) {
    this.suppl = suppl;
  }

  @Override
  public Cursor<T> cursor() {
    try {
      return Cursor.open(suppl.tryGet());
    } catch (Throwable cause) {
      return One
        .<T>gone("Can't get returning", it -> new IllegalStateException(it, cause))
        .cursor();
    }
  }
}

final class Gone<T> implements One<T> {
  private final String message;
  private final TryFunction<? super String, ? extends RuntimeException> error;

  Gone(final String message, final TryFunction<? super String, ? extends RuntimeException> error) {
    this.message = message;
    this.error = error;
  }

  @Override
  public Cursor<T> cursor() {
    throw error.apply(message);
  }
}

final class Done<A extends AutoCloseable, T, O extends One<T>> implements One<T> {
  private final TrySupplier<? extends A> doing;
  private final TryFunction<? super A, ? extends O> then;

  Done(final TrySupplier<? extends A> doing, final TryFunction<? super A, ? extends O> then) {
    this.doing = doing;
    this.then = then;
  }

  @Override
  public final Cursor<T> cursor() {
    try (final var auto = doing.tryGet()) {
      return then.tryApply(auto).cursor();
    } catch (Throwable e) {
      return Cursor.nothing();
    }
  }
}
