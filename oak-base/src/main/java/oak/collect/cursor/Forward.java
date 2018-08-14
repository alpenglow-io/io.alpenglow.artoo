package oak.collect.cursor;

import static java.lang.ThreadLocal.withInitial;
import static java.util.Objects.nonNull;

final class Forward<E> implements Cursor<E> {
  private final E[] es;
  private final ThreadLocal<Integer> index;

  Forward(E[] es) {
    this(es, -1);
  }
  private Forward(E[] es, int index) {
    this.es = es;
    this.index = withInitial(() -> index);
  }

  @Override
  public final boolean hasNext() {
    return nonNull(es) && es.length > 0 && current() < es.length;
  }

  @Override
  public final E next() {
    index.set(current());
    return es[index.get()];
  }

  private int current() {
    return index.get() + 1;
  }
}
