package oak.collect.rope;

import oak.collect.cursor.Cursor;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Rope<T> extends Iterable<T> {
  Rope<T> add(T value);
}

final class SyncRope<T> implements Rope<T> {
  private final Node<T> empty;
  private volatile Node<T> first;
  private volatile Node<T> last;
  private volatile long size;

  SyncRope() {
    this(
      new EmptyNode<>(),
      0
    );
  }
  private SyncRope(Node<T> empty, long size) {
    this.empty = empty;
    this.first = this.empty;
    this.last = this.empty;
    this.size = size;
  }

  @Override
  public Rope<T> add(T value) {
    synchronized (this) {
      if (first.equals(empty)) {
        first = first.next(value);
      } else if (!first.equals(empty) && last.equals(empty)) {
        first = first.next(value);
        last = first.next();
      } else {
        last = last.next(value);
      }
    }
    return this;
  }

  @NotNull
  @Override
  public Iterator<T> iterator() {
    return Cursor.jump(first.iterator(), first.next().iterator());
  }
}
