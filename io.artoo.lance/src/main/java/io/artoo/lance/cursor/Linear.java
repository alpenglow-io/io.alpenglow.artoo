package io.artoo.lance.cursor;

import org.jetbrains.annotations.Contract;

import static java.util.Objects.nonNull;

final class Linear<R> implements Cursor<R> {
  private final R[] rs;
  private final Index index;

  Linear(R[] rs) {
    this(rs, Index.zero());
  }

  @Contract(pure = true)
  private Linear(R[] rs, Index index) {
    this.rs = rs;
    this.index = index;
  }

  @Override
  public final boolean hasNext() {
    return nonNull(rs) && rs.length > 0 && index.eval() < rs.length;
  }

  @Override
  public final R next() {
    return rs.length > 0 ? rs[index.evalAndInc()] : null;
  }
}
