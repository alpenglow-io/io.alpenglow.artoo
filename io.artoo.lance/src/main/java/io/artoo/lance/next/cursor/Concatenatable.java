package io.artoo.lance.next.cursor;

import io.artoo.lance.next.Cursor;
import io.artoo.lance.next.Next;
import io.artoo.lance.next.Queue;

public interface Concatenatable<T> extends Projectable<T> {
  default <N extends Next<T>> Cursor<T> concat(final N next) {
    return new Concat<>(Queue.each(next));
  }
}

final class Concat<T> implements Cursor<T> {
  private final Queue<T> queue;

  Concat(final Queue<T> queue) {this.queue = queue;}

  @Override
  public final T fetch() throws Throwable {
    return !queue.isEmpty() && hasNext() ? queue.fetch() : null;
  }

  @Override
  public boolean hasNext() {
    if (queue.isNotEmpty() && !queue.hasNext())
      queue.detach();

    return !queue.isEmpty() && queue.hasNext();
  }

  @Override
  public final <N extends Next<T>> Cursor<T> concat(final N next) {
    queue.attach(next);
    return this;
  }
}


