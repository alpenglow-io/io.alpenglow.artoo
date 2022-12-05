package io.alpenglow.artoo.lance.query.cursor.map;

import io.alpenglow.artoo.lance.query.cursor.Source;

final class Flatten<T> {
  private boolean hasNext = true;
  private Source<T> source;
  private final Object lock = new Object();

  public Flatten<T> hasNext(boolean hasNext) {
    synchronized (lock) {
      this.hasNext = hasNext;
    }
    return this;
  }

  public boolean hasNext() {
    return hasNext;
  }

  public Flatten<T> source(Source<T> source) {
    synchronized (lock) {
      this.source = source;
    }
    return this;
  }

  public Source<T> source() {
    return source;
  }
}
