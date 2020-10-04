package io.artoo.lance.fetcher;

import io.artoo.lance.func.Cons;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;

public interface Other<T> extends Fetcher<T> {
  default <C extends Cursor<T>> Cursor<T> or(final Suppl.Uni<? extends C> alternative) {
    return new Or<>(this, alternative);
  }

  default <E extends RuntimeException> Cursor<T> or(final String message, final Func.Uni<? super String, ? extends E> exception) {
    return new OrException<>(this, message, exception);
  }

  default Cursor<T> exceptionally(Cons.Uni<? super Throwable> catch$) {
    return new Catch<>(this, catch$);
  }
}

record Or<T, C extends Cursor<T>>(Fetcher<T> fetcher, Suppl.Uni<? extends C> alternative) implements Cursor<T> {
  @Override
  public final T fetch() throws Throwable {
    return fetcher.hasNext() ? fetcher.fetch() : alternative.get().fetch();
  }
}

record OrException<T, E extends RuntimeException>(Fetcher<T> fetcher, String message, Func.Uni<? super String, ? extends E> exception) implements Cursor<T> {
  @Override
  public T fetch() throws Throwable {
    if (fetcher.hasNext()) {
      return fetcher.fetch();
    } else {
      throw exception.apply(message);
    }
  }
}

record Catch<T>(Fetcher<T> fetcher, Cons.Uni<? super Throwable> catch$) implements Cursor<T> {
  @Override
  public final T fetch() {
    try {
      return fetcher.fetch();
    } catch (Throwable throwable) {
      catch$.accept(throwable);
      return null;
    }
  }
}
