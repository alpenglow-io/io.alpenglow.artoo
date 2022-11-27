package io.alpenglow.artoo.lance.scope.random;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.scope.Random;

@SuppressWarnings("unchecked")
public final class Int<T extends Number> implements Random<T> {
  public enum Zahlen {
    Z8(8), Z16(16), Z32(32), Z64(64);
    private final int bits;

    Zahlen(final int bits) {
      this.bits = bits;
    }
  }

  private final Zahlen zahlen;
  private final Binary bit;

  public Int(final Zahlen zahlen, final Binary bit) {
    this.zahlen = zahlen;
    this.bit = bit;
  }

  @Override
  public <R> R let(final TryFunction1<? super T, ? extends R> func) {
    return bit.let(zahlen.bits, rnd -> func.apply((T) rnd));
  }
}
