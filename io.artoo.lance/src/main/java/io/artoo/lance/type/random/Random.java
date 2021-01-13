package io.artoo.lance.type.random;

import io.artoo.lance.func.Func;
import io.artoo.lance.type.Let;

import static io.artoo.lance.func.Func.Default.Nothing;
import static java.lang.StrictMath.log;
import static java.lang.StrictMath.sqrt;

@SuppressWarnings("StatementWithEmptyBody")
public sealed interface Random<N extends Number> extends Let<N> permits Random.Real, Random.Bit, Random.Zahlen {
  static Random<Double> real(final long seed) {
    return
      new Real.Pseudo(
        new Bit.Pseudo(
          new Scramble.Initial(
            seed
          )
        )
      );
  }

  static Random<Double> gaussian(final long seed) {
    return
      new Real.Gaussian(
        new Real.Pseudo(
          new Bit.Pseudo(
            new Scramble.Initial(
              seed
            )
          )
        )
      );
  }

  static Random<Long> zahlen(final long seed) {
    return
      new Zahlen.Pseudo(
        new Bit.Pseudo(
          new Scramble.Initial(
            seed
          )
        )
      );
  }

  sealed interface Bit extends Random<Integer> permits Bit.Pseudo {
    @Override
    default <R> R let(final Func.Uni<? super Integer, ? extends R> func) {
      return let(32, func);
    }

    <R> R let(int bits, Func.Uni<? super Integer, ? extends R> func);

    final class Pseudo implements Bit {
      private static final long addend = 0xBL;

      private final Scramble scramble;

      private Pseudo(final Scramble scramble) {this.scramble = scramble;}

      @Override
      public <R> R let(int bits, final Func.Uni<? super Integer, ? extends R> func) {
        return scramble.apply((atomic, multiplier, mask) -> {
          var random = Integer.MIN_VALUE;
          for (
            long old = atomic.get(), next = (old * multiplier + addend) & mask;

            !atomic.compareAndSet(old, next);

            random = (int) (next >>> (48 - bits))
          )
            ;
          return func.apply(random);
        });
      }
    }
  }

  sealed interface Real extends Random<Double> permits Real.Gaussian, Real.Pseudo {
    final class Pseudo implements Real {
      private final Bit bit;

      private Pseudo(final Bit bit) {this.bit = bit;}

/*      @Override
      public final float decimal32() { return bit.next(24) / ((float) (1 << 24)); }*/

      @Override
      public <R> R let(final Func.Uni<? super Double, ? extends R> func) {
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

    final class Gaussian implements Real {
      private final Real real;
      private volatile Object next;

      private Gaussian(final Real real) {
        this(
          real,
          Nothing
        );
      }

      private Gaussian(final Real real, final Object next) {
        this.real = real;
        this.next = next;
      }

      @Override
      public <R> R let(final Func.Uni<? super Double, ? extends R> func) {
        if (next.equals(Nothing)) {
          synchronized (this) {
            if (next.equals(Nothing)) {
              double v1, v2, s;
              do
              {
                v1 = real.let(it -> 2 * it - 1);
                v2 = real.let(it -> 2 * it - 1);
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
  }

  sealed interface Zahlen extends Random<Long> permits Zahlen.Pseudo {
    final class Pseudo implements Zahlen {
      private final Bit bit;

      private Pseudo(final Bit bit) {
        this.bit = bit;
      }

      @Override
      public <R> R let(final Func.Uni<? super Long, ? extends R> func) {
        return bit.let(64, rnd -> func.apply(rnd.longValue()));
      }
    }
  }
}
