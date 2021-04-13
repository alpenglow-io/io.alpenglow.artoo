package io.artoo.lance.query;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.one.Filterable;
import io.artoo.lance.query.one.Matchable;
import io.artoo.lance.query.one.Otherwise;
import io.artoo.lance.query.one.Peekable;
import io.artoo.lance.query.one.Projectable;

public interface One<T> extends Projectable<T>, Peekable<T>, Filterable<T>, Otherwise<T>, Matchable<T> {
  static <T> One<T> from(final T element) {
    return element != null ? One.lone(element) : One.none();
  }

  static <L> One<L> lone(final L element) {
    return new Lone<>(element);
  }

  static <T> One<T> from(final Suppl.Uni<T> suppl) {
    return new Pone<>(suppl);
  }

  static <T> One<T> gone(final String message, final Func.Uni<? super String, ? extends RuntimeException> error) {
    return new Gone<>(message, error);
  }

  @SuppressWarnings("unchecked")
  static <L> One<L> none() {
    return (One<L>) None.Default;
  }

  static <A extends AutoCloseable, T, O extends One<T>> One<T> done(Suppl.Uni<? extends A> going, Func.Uni<? super A, ? extends O> then) {
    return new Done<>(going, then);
  }

  default T yield() {
    return iterator().next();
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

final class Pone<T> implements One<T> {
  private final Suppl.Uni<T> suppl;

  public Pone(final Suppl.Uni<T> suppl) {
    this.suppl = suppl;
  }

  @Override
  public Cursor<T> cursor() {
    return Cursor.open(suppl.get());
  }
}

final class Gone<T> implements One<T> {
  private final String message;
  private final Func.Uni<? super String, ? extends RuntimeException> error;

  Gone(final String message, final Func.Uni<? super String, ? extends RuntimeException> error) {
    this.message = message;
    this.error = error;
  }

  @Override
  public Cursor<T> cursor() {
    throw error.apply(message);
  }
}

enum None implements One<Object> {
  Default;

  @Override
  public final Cursor<Object> cursor() {
    return Cursor.nothing();
  }
}

final class Done<A extends AutoCloseable, T, O extends One<T>> implements One<T> {
  private final Suppl.Uni<? extends A> doing;
  private final Func.Uni<? super A, ? extends O> then;

  Done(final Suppl.Uni<? extends A> doing, final Func.Uni<? super A, ? extends O> then) {
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
