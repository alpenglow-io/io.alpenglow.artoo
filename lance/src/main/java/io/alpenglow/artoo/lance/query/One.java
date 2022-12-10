package io.alpenglow.artoo.lance.query;

import io.alpenglow.artoo.lance.Queryable;
import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.func.TrySupplier1;
import io.alpenglow.artoo.lance.query.one.Elseable;
import io.alpenglow.artoo.lance.query.one.Filterable;
import io.alpenglow.artoo.lance.query.one.Peekable;
import io.alpenglow.artoo.lance.query.one.Projectable;

enum None implements One<Object> {
  Default;

  @Override
  public final Cursor<Object> cursor() {
    return Cursor.empty();
  }
}

public interface One<T> extends Projectable<T>, Peekable<T>, Filterable<T>, Elseable<T> {
  static <T> One<T> of(final T element) {
    return element != null ? new Lone<>(element) : One.none();
  }

  static <T> One<T> from(final TrySupplier1<T> supply) {
    return new Sone<>(supply);
  }

  static <T> One<T> gone(final String message, final TryFunction1<? super String, ? extends RuntimeException> error) {
    return new Gone<>(message, error);
  }

  @SuppressWarnings("unchecked")
  static <L> One<L> none() {
    return (One<L>) None.Default;
  }

  static <A extends AutoCloseable, T, O extends One<T>> One<T> done(TrySupplier1<? extends A> going, TryFunction1<? super A, ? extends O> then) {
    return new Done<>(going, then);
  }

  interface OfTwo<A, B> extends Queryable.OfTwo<A, B> {}
}

final class Lone<ELEMENT> implements One<ELEMENT> {
  private final ELEMENT element;

  public Lone(final ELEMENT element) {
    this.element = element;
  }

  @Override
  public Cursor<ELEMENT> cursor() {
    return Cursor.open(element);
  }
}

final class Sone<T> implements One<T> {
  private final TrySupplier1<T> suppl;

  public Sone(final TrySupplier1<T> suppl) {
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
  private final TryFunction1<? super String, ? extends RuntimeException> error;

  Gone(final String message, final TryFunction1<? super String, ? extends RuntimeException> error) {
    this.message = message;
    this.error = error;
  }

  @Override
  public Cursor<T> cursor() {
    throw error.apply(message);
  }
}

final class Done<A extends AutoCloseable, T, O extends One<T>> implements One<T> {
  private final TrySupplier1<? extends A> doing;
  private final TryFunction1<? super A, ? extends O> then;

  Done(final TrySupplier1<? extends A> doing, final TryFunction1<? super A, ? extends O> then) {
    this.doing = doing;
    this.then = then;
  }

  @Override
  public final Cursor<T> cursor() {
    try (final var auto = doing.tryGet()) {
      return then.tryApply(auto).cursor();
    } catch (Throwable e) {
      return Cursor.empty();
    }
  }
}
