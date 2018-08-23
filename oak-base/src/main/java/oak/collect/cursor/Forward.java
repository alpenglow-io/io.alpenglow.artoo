package oak.collect.cursor;

import static java.util.Objects.nonNull;
import static oak.collect.cursor.LocalIndex.zero;

final class Forward<E> implements Cursor<E> {
  private final E[] es;
  private final LocalIndex index;

  Forward(E[] es) {
    this(es, zero());
  }
  private Forward(E[] es, LocalIndex index) {
    this.es = es;
    this.index = index;
  }

  @Override
  public final boolean hasNext() {
    return nonNull(es) && es.length > 0 && index.get() + 1 < es.length;
  }

  @Override
  public final E next() {
    return es.length > 0 ? es[index.inc()] : null;
  }
}
