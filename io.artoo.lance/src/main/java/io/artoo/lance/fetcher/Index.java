package io.artoo.lance.fetcher;

final class Index {
  private int value = 0;
  private final Object lock = new Object() {};

  public final Index inc() {
    synchronized (lock) {
      value++;
    }
    return this;
  }

  public final int value() {
    return value;
  }

  public final Index reset() {
    synchronized (lock) {
      value = 0;
    }
    return this;
  }
}
