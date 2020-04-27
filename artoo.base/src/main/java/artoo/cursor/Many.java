package artoo.cursor;

import org.jetbrains.annotations.Contract;

import static java.util.Objects.nonNull;

final class Many<E> implements Cursor<E> {
  private final E[] es;
  private final Index index;

  Many(E[] es) {
    this(es, Index.zero());
  }

  @Contract(pure = true)
  private Many(E[] es, Index index) {
    this.es = es;
    this.index = index;
  }

  @Override
  public final boolean hasNext() {
    return nonNull(es) && es.length > 0 && index.eval() < es.length;
  }

  @Override
  public final E next() {
    return es.length > 0 ? es[index.getAndIncrement()] : null;
  }
}
