package io.artoo.cursor;

import org.jetbrains.annotations.Contract;

import static java.util.Objects.nonNull;

final class Linear<E> implements Cursor<E> {
  private final E[] es;
  private final Index index;

  Linear(E[] es) {
    this(es, Index.zero());
  }

  @Contract(pure = true)
  private Linear(E[] es, Index index) {
    this.es = es;
    this.index = index;
  }

  @Override
  public final boolean hasNext() {
    return nonNull(es) && es.length > 0 && index.eval() < es.length;
  }

  @Override
  public final E next() {
    return es.length > 0 ? es[index.evalAndInc()] : null;
  }

  @Override
  public final void resume() {
    this.index.reset();
  }
}
