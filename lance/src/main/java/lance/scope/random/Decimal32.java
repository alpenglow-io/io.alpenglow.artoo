package lance.scope.random;

import lance.func.Func;
import lance.scope.Random;

public final class Decimal32 implements Random<Float> {
  private final Binary bit;

  public Decimal32(final Binary bit) {this.bit = bit;}

  @Override
  public <R> R let(final Func.TryFunction<? super Float, ? extends R> func) {
    return bit.let(24, rnd1 -> func.apply(rnd1 / ((float) (1 << 24))));
  }
}
