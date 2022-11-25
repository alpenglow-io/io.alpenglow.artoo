package io.alpenglow.artoo.lance.func.tail;

import io.alpenglow.artoo.lance.func.Recursive.Tailrec;

public final class Fibonacci extends Tailrec<Integer, Integer, Fibonacci> {
  private final int counted;

  private Fibonacci(int counted) {
    this.counted = counted;
  }

  @Override
  public Return<Integer, Integer, Fibonacci> tryApply(Integer value) throws Throwable {
    return switch (value) {
      case 0 -> Return.with(0, this);
      case 1 -> Return.with(1, this);
      default -> Return.with(counted + value, new Fibonacci(counted + value).on(value - 1).next().on(value - 2).next());
    };
  }

  private int fib(int value) {
    return switch (value) {
      case 0 -> 0;
      case 1 -> 1;
      default -> fib(value - 1) + fib(value - 2);
    };
  }

  public static Fibonacci fibonacci() { return new Fibonacci(0); }
}
