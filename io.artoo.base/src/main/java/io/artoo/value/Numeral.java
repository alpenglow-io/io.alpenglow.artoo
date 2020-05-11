package io.artoo.value;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface Numeral<N extends Number, R extends Record & Numeral<N, R>> {
  N box();

  <L extends Number, V extends Record & Numeral<L, V>> R add(V value);
  <L extends Number, V extends Record & Numeral<L, V>> R div(V value);
  R inc();

  static <N extends Number, R extends Record & Numeral<N, R>> R inc(R count) {
    return count == null ? null : count.inc();
  }

  static <N extends Number, R extends Record & Numeral<N, R>> R add(R first, R second) {
    return first == null ? second : first.add(second);
  }

  static <N extends Number, R extends Record & Numeral<N, R>> R div(R first, R second) {
    return second == null || first == null ? null : first.div(second);
  }

  @NotNull
  @Contract(pure = true)
  static <V, N extends Number, R extends Record & Numeral<N, R>> Function<? super V, ? extends R> asNumeral() {
    return it -> it == null ? null : (R) switch (it.getClass().getSimpleName()) {
      case "Byte" -> new Int8((Byte) it);
      case "Short" -> new Int16((Short) it);
      case "Integer" -> new Int32((Integer) it);
      case "Long" -> new Int64((Long) it);
      case "Float" -> new Single32((Float) it);
      case "Double" -> new Single64((Double) it);
      default -> null;
    };
  }

  @Contract(pure = true)
  static <V> @NotNull Function<? super V, ? extends Single64> asSingle64() {
    return it -> it == null ? null : switch (it.getClass().getSimpleName()) {
      case "Byte", "Short", "Integer", "Long", "Float", "Double" -> new Single64((Double) it);
      default -> null;
    };
  }

}



