package io.artoo.lance.query.cursor;

final class Concat<T> implements Cursor<T> {
  private final Cursors<T> queue;

  Concat(final Cursors<T> queue) {this.queue = queue;}

  @Override
  public final T fetch() throws Throwable {
    return !queue.isEmpty() && hasNext() ? queue.peek().fetch() : null;
  }

  @Override
  public boolean hasNext() {
    if (queue.isNotEmpty() && !queue.peek().hasNext()) queue.detach();

    return !queue.isEmpty() && queue.peek().hasNext();
  }

  @Override
  public T next() {
    try {
      return fetch();
    } catch (Throwable throwable) {
      return null;
    }
  }

  @Override
  public Cursor<T> concat(final Cursor<T> cursor) {
    queue.attach(cursor);
    return this;
  }
}
