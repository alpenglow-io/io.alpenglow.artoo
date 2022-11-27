package io.alpenglow.artoo.lance.scope.random;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.scope.Random;

import static io.alpenglow.artoo.lance.scope.Default.Nothing;
import static java.lang.StrictMath.log;
import static java.lang.StrictMath.sqrt;

public final class Gaussian implements Random<Double> {
  private final Random<Double> decimal;
  private volatile Object next;

  public Gaussian(final Random<Double> decimal) {
    this(
      decimal,
      Nothing
    );
  }

  private Gaussian(final Random<Double> decimal, final Object next) {
    this.decimal = decimal;
    this.next = next;
  }

  @Override
  public <R> R let(final TryFunction1<? super Double, ? extends R> func) {
    if (next.equals(Nothing)) {
      synchronized (this) {
        if (next.equals(Nothing)) {
          double v1, v2, s;
          do
          {
            v1 = decimal.let(it -> 2 * it - 1);
            v2 = decimal.let(it -> 2 * it - 1);
            s = v1 * v1 + v2 * v2;
          } while (s >= 1 || s == 0);

          final var multiplier = sqrt(-2 * log(s) / s);
          next = v2 * multiplier;
          return func.apply(v1 * multiplier);
        } else {
          final var applied = func.apply((double) next);
          next = Nothing;
          return applied;
        }
      }
    } else {
      final var applied = func.apply((double) next);
      next = Nothing;
      return applied;
    }
  }
}
