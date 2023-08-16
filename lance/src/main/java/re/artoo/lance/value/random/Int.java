package re.artoo.lance.value.random;

import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.value.Random;

@SuppressWarnings("unchecked")
public final class Int<T extends Number> implements Random<T> {
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

  public enum Zahlen {
    Z8(8), Z16(16), Z32(32), Z64(64);
    private final int bits;

    Zahlen(final int bits) {
      this.bits = bits;
    }
  }
}
