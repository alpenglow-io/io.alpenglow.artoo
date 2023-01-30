package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;
import re.artoo.lance.scope.Late;
import re.artoo.lance.scope.Let;

import java.util.Iterator;

public interface Complementable<T> extends Probe<T> {
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

abstract non-sealed class As<T> implements Collectable<T> {
  protected final Probe<T> probe;

  protected As(final Probe<T> probe) {this.probe = probe;}

  @Override
  public final <R> R as(final Routine<T, R> routine) {
    return switch (probe) {
      case Cursor<T> cursor -> cursor.as(routine);
      default -> Cursor.<T>empty().as(routine);
    };
  }
}

final class Or<T, C extends Cursor<T>> extends As<T> implements Cursor<T> {
  private final Late<Probe<T>> reference = Late.init();
  private final Let<? extends C> other;

  Or(final Probe<T> probe, final Let<? extends C> other) {
    super(probe);
    this.other = other;
  }

  @Override
  public <R> R tick(TryIntFunction1<? super T, ? extends R> fetch) {
    return hasNext() ? reference.let(it -> it.tick(fetch)) : null;
  }
  @Override
  public boolean hasNext() {
    other.get(otherwise -> reference.set(() -> probe.hasNext() ? probe : otherwise));

    return reference.let(Iterator::hasNext);
  }
}

final class Er<T, E extends RuntimeException> extends As<T> implements Cursor<T> {
  private final String message;
  private final TryFunction2<? super String, ? super Throwable, ? extends E> exception;

  Er(final Probe<T> probe, final String message, final TryFunction2<? super String, ? super Throwable, ? extends E> exception) {
    super(probe);
    this.message = message;
    this.exception = exception;
  }
  @Override
  public <R> R tick(TryIntFunction1<? super T, ? extends R> fetch) throws Throwable {
    try {
      if (hasNext()) {
        return probe.tick(fetch);
      } else {
        throw exception.apply(message, null);
      }
    } catch (Throwable throwable) {
      throw exception.apply(message, throwable);
    }
  }
  @Override
  public boolean hasNext() {
    return probe.hasNext();
  }
}

final class Catch<T> extends As<T> implements Cursor<T> {
  private final TryConsumer1<? super Throwable> catch$;

  Catch(Probe<T> probe, TryConsumer1<? super Throwable> catch$) {
    super(probe);
    this.catch$ = catch$;
  }
  @Override
  public <R> R tick(TryIntFunction1<? super T, ? extends R> fetch) {
    try {
      return probe.tick(fetch);
    } catch (Throwable throwable) {
      catch$.accept(throwable);
      return null;
    }
  }

  @Override
  public boolean hasNext() {
    return probe.hasNext();
  }
}
