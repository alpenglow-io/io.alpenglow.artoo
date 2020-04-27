package artoo.cursor;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import artoo.type.AsInt;

import java.util.concurrent.locks.StampedLock;

public final class Index implements AsInt {
  private final StampedLock stamp;
  private int value;

  @Contract(" -> new")
  static @NotNull Index zero() { return new Index(0); }

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

  public final Index incAfterwards() {
    final var write = this.stamp.asWriteLock();
    try {
      write.lock();
      return value++;
    } finally {
      write.unlock();
    }
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
