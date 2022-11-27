package io.alpenglow.artoo.lance.scope.random;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.scope.Random;

public sealed interface Binary extends Random<Integer> permits Binary.Digit {
  @Override
  default <R> R let(final TryFunction1<? super Integer, ? extends R> func) {
    return let(32, func);
  }

  <R> R let(int bits, TryFunction1<? super Integer, ? extends R> func);

  final class Digit implements Binary {
    private static final long addend = 0xBL;

    private final Scramble scramble;

    public Digit(final Scramble scramble) {this.scramble = scramble;}

    @Override
    public <R> R let(int bits, final TryFunction1<? super Integer, ? extends R> func) {
      return scramble.apply((atomic, multiplier, mask) -> {
        var random = Integer.MIN_VALUE;

        long
          old = atomic.get(),
          next = (old * multiplier + addend) & mask;
        while (!atomic.compareAndSet(old, next)) {
          random = (int) (next >>> (48 - bits));
        }

        return func.apply(random);
      });
    }
  }
}
