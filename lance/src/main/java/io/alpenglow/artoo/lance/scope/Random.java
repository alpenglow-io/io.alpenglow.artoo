package io.alpenglow.artoo.lance.scope;

import io.alpenglow.artoo.lance.scope.random.Binary;
import io.alpenglow.artoo.lance.scope.random.Decimal32;
import io.alpenglow.artoo.lance.scope.random.Decimal64;
import io.alpenglow.artoo.lance.scope.random.Gaussian;
import io.alpenglow.artoo.lance.scope.random.Int;
import io.alpenglow.artoo.lance.scope.random.Scramble;

public sealed interface Random<N extends Number> extends Let<N> permits Binary, Decimal32, Decimal64, Gaussian, Int {
  static Random<Double> decimal64(final long seed) {
    return
      new Decimal64(
        new Binary.Digit(
          new Scramble.Initial(
            seed
          )
        )
      );
  }

  static Random<Float> decimal32(final long seed) {
    return
      new Decimal32(
        new Binary.Digit(
          new Scramble.Initial(
            seed
          )
        )
      );
  }

  static Random<Double> gaussian(final long seed) {
    return
      new Gaussian(
        new Decimal64(
          new Binary.Digit(
            new Scramble.Initial(
              seed
            )
          )
        )
      );
  }

  static Random<Long> int64(final long seed) {
    return
      new Int<>(
        Int.Zahlen.Z64,
        new Binary.Digit(
          new Scramble.Initial(
            seed
          )
        )
      );
  }

  static Random<Integer> int32(final long seed) {
    return
      new Int<>(
        Int.Zahlen.Z32,
        new Binary.Digit(
          new Scramble.Initial(
            seed
          )
        )
      );
  }

  static Random<Short> int16(final long seed) {
    return
      new Int<>(
        Int.Zahlen.Z16,
        new Binary.Digit(
          new Scramble.Initial(
            seed
          )
        )
      );
  }

  static Random<Byte> int8(final long seed) {
    return
      new Int<>(
        Int.Zahlen.Z8,
        new Binary.Digit(
          new Scramble.Initial(
            seed
          )
        )
      );
  }
}
