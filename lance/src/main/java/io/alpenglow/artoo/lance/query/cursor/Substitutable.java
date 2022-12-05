package io.alpenglow.artoo.lance.query.cursor;

import io.alpenglow.artoo.lance.func.TryFunction2;
import io.alpenglow.artoo.lance.func.TryConsumer1;
import io.alpenglow.artoo.lance.func.TrySupplier1;
import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.cursor.routine.Routine;
import io.alpenglow.artoo.lance.scope.Late;
import io.alpenglow.artoo.lance.scope.Let;

import java.util.Iterator;

public interface Substitutable<T> extends Source<T> {
  default <C extends Cursor<T>> Cursor<T> or(final TrySupplier1<? extends C> alternative) {
    return new Or<>(this, Let.lazy(alternative));
  }

  default <E extends RuntimeException> Cursor<T> or(final String message, final TryFunction2<? super String, ? super Throwable, ? extends E> exception) {
    return new Er<>(this, message, exception);
  }

  default Cursor<T> exceptionally(TryConsumer1<? super Throwable> catch$) {
    return new Catch<>(this, catch$);
  }
}

abstract non-sealed class As<T> implements Transformable<T> {
  protected final Source<T> source;

  protected As(final Source<T> source) {this.source = source;}

  @Override
  public final <R> R as(final Routine<T, R> routine) {
    return switch (source) {
      case Cursor<T> cursor -> cursor.as(routine);
      default -> Cursor.<T>nothing().as(routine);
    };
  }
}

final class Or<T, C extends Cursor<T>> extends As<T> implements Cursor<T> {
  private final Late<Source<T>> reference = Late.init();
  private final Let<? extends C> other;

  Or(final Source<T> source, final Let<? extends C> other) {
    super(source);
    this.other = other;
  }

  @Override
  public T fetch() {
    return hasNext() ? reference.let(Source::fetch) : null;
  }

  @Override
  public boolean hasNext() {
    other.get(otherwise -> reference.set(() -> source.hasNext() ? source : otherwise));

    return reference.let(Iterator::hasNext);
  }
}

final class Er<T, E extends RuntimeException> extends As<T> implements Cursor<T> {
  private final String message;
  private final TryFunction2<? super String, ? super Throwable, ? extends E> exception;

  Er(final Source<T> source, final String message, final TryFunction2<? super String, ? super Throwable, ? extends E> exception) {
    super(source);
    this.message = message;
    this.exception = exception;
  }

  @Override
  public T fetch() {
    try {
      if (hasNext()) {
        return source.fetch();
      } else {
        throw exception.apply(message, null);
      }
    } catch (Throwable throwable) {
      throw exception.apply(message, throwable);
    }
  }

  @Override
  public boolean hasNext() {
    return source.hasNext();
  }
}

final class Catch<T> extends As<T> implements Cursor<T> {
  private final TryConsumer1<? super Throwable> catch$;

  Catch(Source<T> source, TryConsumer1<? super Throwable> catch$) {
    super(source);
    this.catch$ = catch$;
  }

  @Override
  public T fetch() {
    try {
      return source.fetch();
    } catch (Throwable throwable) {
      catch$.accept(throwable);
      return null;
    }
  }

  @Override
  public boolean hasNext() {
    return source.hasNext();
  }
}
