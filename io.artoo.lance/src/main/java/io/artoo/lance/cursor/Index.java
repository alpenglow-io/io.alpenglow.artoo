package io.artoo.lance.cursor;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public record Index(AtomicInteger value) {
  public Index { assert value != null; }

  @Contract(" -> new")
  static @NotNull Index zero() { return new Index(new AtomicInteger(0)); }

  @Contract(" -> this")
  public final Index inc() {
    this.value.incrementAndGet();
    return this;
  }

  public final Index dec() {
    this.value.decrementAndGet();
    return this;
  }

  public final int evalAndInc() {
    return this.value.getAndIncrement();
  }

  public final int eval() {
    return this.value.get();
  }

  public final void reset() {
    this.value.set(0);
  }
}
