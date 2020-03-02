package oak.query.one;

import oak.func.$2.IntCons;
import oak.query.Queryable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Hookable<T> extends Queryable<T> {
  default One<T> peek(final IntCons<? super T> peek) {
    return () -> Peek<>(this, peek);
  }
}

final class Peek<T> implements Queryable<T> {
  private final Queryable<T> queryable;
  private final IntCons<? super T> peek;

  Peek(Queryable<T> queryable, IntCons<? super T> peek) {
    this.queryable = queryable;
    this.peek = peek;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    return null;
  }
}
