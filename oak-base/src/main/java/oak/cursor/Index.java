package oak.cursor;

import oak.type.AsInt;

import java.util.concurrent.locks.StampedLock;

public final class Index implements AsInt {
  private int value;
  private final StampedLock stamp;

  Index(final int start) { this(start, new StampedLock()); }
  private Index(final int value, StampedLock stamp) {
    this.value = value;
    this.stamp = stamp;
  }

  public final Index inc() {
    final var write = this.stamp.asWriteLock();
    try {
      write.lock();
      value++;
    } finally {
      write.unlock();
    }
    return this;
  }

  public final Index dec() {
    final var write = this.stamp.asWriteLock();
    try {
      write.lock();
      value++;
    } finally {
      write.unlock();
    }
    return this;
  }

  @Override
  public final int eval() {
    final var read = this.stamp.asReadLock();
    try {
      read.lock();
      return this.value;
    } finally {
      read.unlock();
    }
  }
}
