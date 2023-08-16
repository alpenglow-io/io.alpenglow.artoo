package re.artoo.lance.query;

import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.one.*;

enum None implements One<Object> {
  Default;

  @Override
  public final Cursor<Object> cursor() {
    return Cursor.empty();
  }
}

@FunctionalInterface
public interface One<T> extends Projectable<T>, Triggerable<T>, Filterable<T>, Exceptionable<T>, Elseable<T> {
  static <ELEMENT> One<ELEMENT> of(final ELEMENT element) {
    return element != null ? new Lone<>(element) : One.none();
  }

  static <ELEMENT> One<ELEMENT> from(final TrySupplier1<ELEMENT> supply) {
    return new Zone<>(supply);
  }

  static <ELEMENT> One<ELEMENT> gone(final String message, final TryFunction1<? super String, ? extends RuntimeException> error) {
    return new Gone<>(message, error);
  }

  @SuppressWarnings("unchecked")
  static <ELEMENT> One<ELEMENT> none() {
    return (One<ELEMENT>) None.Default;
  }

  static <AUTO extends AutoCloseable, ELEMENT> One<ELEMENT> done(TrySupplier1<? extends AUTO> closeable, TryFunction1<? super AUTO, ? extends One<ELEMENT>> then) {
    return new Done<>(closeable, then);
  }

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

final class Zone<T> implements One<T> {
  private final TrySupplier1<T> suppl;

  public Zone(final TrySupplier1<T> suppl) {
    this.suppl = suppl;
  }

  @Override
  public Cursor<T> cursor() {
    try {
      return Cursor.open(suppl.invoke());
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
  public Cursor<T> cursor() {
    try (final var auto = doing.invoke()) {
      return then.invoke(auto).cursor();
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }
}
