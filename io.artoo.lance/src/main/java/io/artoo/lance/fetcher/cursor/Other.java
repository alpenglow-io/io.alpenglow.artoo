package io.artoo.lance.fetcher.cursor;

import io.artoo.lance.fetcher.Cursor;
import io.artoo.lance.fetcher.Fetcher;
import io.artoo.lance.fetcher.routine.Routine;
import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.type.Value;

import java.util.concurrent.atomic.AtomicReference;

public interface Other<T> extends Fetcher<T> {
  default <C extends Cursor<T>> Cursor<T> or(final Suppl.Uni<? extends C> alternative) {
    return new Or<>(this, Value.lazy(alternative));
  }

  default <E extends RuntimeException> Cursor<T> or(final String message, final Func.Uni<? super String, ? extends E> exception) {
    return new OrException<>(this, message, exception);
  }

  default Cursor<T> exceptionally(Cons.Uni<? super Throwable> catch$) {
    return new Catch<>(this, catch$);
  }
}

final class Or<T, C extends Cursor<T>> implements Cursor<T> {
  private final AtomicReference<Fetcher<T>> reference = new AtomicReference<>();
  private final Fetcher<T> source;
  private final Value<? extends C> other;

  Or(final Fetcher<T> source, final Value<? extends C> other) {
    this.source = source;
    this.other = other;
  }

  @Override
  public final T fetch() throws Throwable {
    return hasNext() ? reference.get().fetch() : null;
  }

  @Override
  public final boolean hasNext() {
    if (reference.get() == null) reference.set(source.hasNext() ? source : other.get());

    return reference.get().hasNext();
  }

  @Override
  public final <R> Cursor<R> invoke(final Routine<T, R> routine) {
    if (source instanceof Cursor<T> cursor)
      cursor.invoke(routine);
    return Cursor.nothing();
  }
}

final class OrException<T, E extends RuntimeException> implements Cursor<T> {
  private final Fetcher<T> source;
  private final String message;
  private final Func.Uni<? super String, ? extends E> exception;

  OrException(final Fetcher<T> source, final String message, final Func.Uni<? super String, ? extends E> exception) {
    this.source = source;
    this.message = message;
    this.exception = exception;
  }

  @Override
  public final T fetch() throws Throwable {
    if (hasNext()) {
      return source.fetch();
    } else {
      throw exception.apply(message);
    }
  }

  @Override
  public final boolean hasNext() {
    return source.hasNext();
  }

  @Override
  public final <R> Cursor<R> invoke(final Routine<T, R> routine) {
    return source instanceof Cursor<T> cursor ? cursor.invoke(routine) : Cursor.nothing();
  }
}

final class Catch<T> implements Cursor<T> {
  private final Fetcher<T> source;
  private final Cons.Uni<? super Throwable> catch$;

  Catch(final Fetcher<T> source, final Cons.Uni<? super Throwable> catch$) {
    this.source = source;
    this.catch$ = catch$;
  }

  @Override
  public final T fetch() {
    try {
      return source.fetch();
    } catch (Throwable throwable) {
      catch$.accept(throwable);
      return null;
    }
  }

  @Override
  public final boolean hasNext() {
    return source.hasNext();
  }

  @Override
  public final <R> Cursor<R> invoke(final Routine<T, R> routine) {
    return source instanceof Cursor<T> cursor ? cursor.invoke(routine) : Cursor.nothing();
  }
}
