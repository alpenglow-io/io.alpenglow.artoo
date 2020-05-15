package io.artoor.cursor;

import io.artoor.type.AsInt;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.StampedLock;

@SuppressWarnings("UnusedReturnValue")
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

  @Contract(" -> this")
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

  public final int evalAndInc() {
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

  public final void reset() {
    final var write = this.stamp.asWriteLock();
    try {
      write.lock();
      value = 0;
    } finally {
      write.unlock();
    }
  }
}
