package re.artoo.lance.experimental.value.random;

import re.artoo.lance.experimental.value.Random;
import re.artoo.lance.func.TryFunction1;

public final class Decimal64 implements Random<Double> {
  private final Binary bit;

  public Decimal64(final Binary bit) {
    this.bit = bit;
  }

  @Override
  public <R> R let(final TryFunction1<? super Double, ? extends R> func) {
    return
      bit.let(26, rnd1 ->
        bit.let(27, rnd2 ->
          func.apply(
            ((long) rnd1 << 27) * 0x1.0p-53 + rnd2 * 0x1.0p-53
          )
        )
      );
  }
}
