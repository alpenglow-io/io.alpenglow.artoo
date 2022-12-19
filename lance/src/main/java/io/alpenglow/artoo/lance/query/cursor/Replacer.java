package io.alpenglow.artoo.lance.query.cursor;

import io.alpenglow.artoo.lance.func.TryConsumer1;
import io.alpenglow.artoo.lance.func.TryFunction2;
import io.alpenglow.artoo.lance.func.TrySupplier1;
import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.cursor.routine.Routine;
import io.alpenglow.artoo.lance.scope.Late;
import io.alpenglow.artoo.lance.scope.Let;

import java.util.Iterator;

public interface Replacer<T> extends Fetcher<T> {
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

abstract non-sealed class As<T> implements Convertor<T> {
  protected final Fetcher<T> fetcher;

  protected As(final Fetcher<T> fetcher) {this.fetcher = fetcher;}

  @Override
  public final <R> R as(final Routine<T, R> routine) {
    return switch (fetcher) {
      case Cursor<T> cursor -> cursor.as(routine);
      default -> Cursor.<T>empty().as(routine);
    };
  }
}

final class Or<T, C extends Cursor<T>> extends As<T> implements Cursor<T> {
  private final Late<Fetcher<T>> reference = Late.init();
  private final Let<? extends C> other;

  Or(final Fetcher<T> fetcher, final Let<? extends C> other) {
    super(fetcher);
    this.other = other;
  }

  @Override
  public T fetch() {
    return hasNext() ? reference.let(Fetcher::fetch) : null;
  }

  @Override
  public boolean hasNext() {
    other.get(otherwise -> reference.set(() -> fetcher.hasNext() ? fetcher : otherwise));

    return reference.let(Iterator::hasNext);
  }
}

final class Er<T, E extends RuntimeException> extends As<T> implements Cursor<T> {
  private final String message;
  private final TryFunction2<? super String, ? super Throwable, ? extends E> exception;

  Er(final Fetcher<T> fetcher, final String message, final TryFunction2<? super String, ? super Throwable, ? extends E> exception) {
    super(fetcher);
    this.message = message;
    this.exception = exception;
  }

  @Override
  public T fetch() {
    try {
      if (hasNext()) {
        return fetcher.fetch();
      } else {
        throw exception.apply(message, null);
      }
    } catch (Throwable throwable) {
      throw exception.apply(message, throwable);
    }
  }

  @Override
  public boolean hasNext() {
    return fetcher.hasNext();
  }
}

final class Catch<T> extends As<T> implements Cursor<T> {
  private final TryConsumer1<? super Throwable> catch$;

  Catch(Fetcher<T> fetcher, TryConsumer1<? super Throwable> catch$) {
    super(fetcher);
    this.catch$ = catch$;
  }

  @Override
  public T fetch() {
    try {
      return fetcher.fetch();
    } catch (Throwable throwable) {
      catch$.accept(throwable);
      return null;
    }
  }

  @Override
  public boolean hasNext() {
    return fetcher.hasNext();
  }
}
