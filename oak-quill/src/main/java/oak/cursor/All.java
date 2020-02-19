package oak.cursor;

import org.jetbrains.annotations.Contract;

import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.nonNull;

final class All<E> implements Cursor<E> {
  private final E[] es;
  private final AtomicInteger index;

  All(E[] es) {
    this(es, new AtomicInteger(0));
  }
  @Contract(pure = true)
  private All(E[] es, AtomicInteger index) {
    this.es = es;
    this.index = index;
  }

  @Override
  public final boolean hasNext() {
    return nonNull(es) && es.length > 0 && index.get() < es.length;
  }

  @Override
  public final E next() {
    return es.length > 0 ? es[index.getAndIncrement()] : null;
  }
}
