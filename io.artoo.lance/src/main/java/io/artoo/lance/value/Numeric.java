package io.artoo.lance.value;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface Numeric<R extends Record & Numeric<R>> {
  @SuppressWarnings("unchecked")
  static <R extends Record & Numeric<R>> R from(Number number) {
    if (number instanceof Byte b) return (R) new Int8(b);

    if (number instanceof Short s) return (R) new Int16(s);

    if (number instanceof Integer i) return (R) new Int32(i);

    if (number instanceof Long l) return (R) new Int64(l);

    if (number instanceof Float f) return (R) new Single32(f);

    if (number instanceof Double d) return (R) new Single64(d);

    throw new IllegalArgumentException(String.format("can't cast %s to number", number));
  }

  @SuppressWarnings("unchecked")
  static <T extends Record, R extends Record & Numeric<R>> R from$(T record) {
    if (record instanceof Int8 b) return (R) b;

    if (record instanceof Int16 s) return (R) s;

    if (record instanceof Int32 i) return (R) i;

    if (record instanceof Int64 l) return (R) l;

    if (record instanceof Single32 f) return (R) f;

    if (record instanceof Single64 d) return (R) d;

    throw new IllegalArgumentException(String.format("can't cast %s to number", record));
  }

  static <T extends Record> boolean isNumber(T record) {
    return record instanceof Numeric;
  }

  Number raw();

  <V extends Record & Numeric<V>> R add(V value);
  <V extends Record & Numeric<V>> R div(V value);
  R inc();

  static <R extends Record & Numeric<R>> R inc(R count) {
    return count == null ? null : count.inc();
  }

  static <R extends Record & Numeric<R>> R add(R first, R second) {
    return first == null ? second : first.add(second);
  }

  static <R extends Record & Numeric<R>> R div(R first, R second) {
    return second == null || first == null ? null : first.div(second);
  }

  @NotNull
  @Contract(pure = true)
  static <V, R extends Record & Numeric<R>> Function<? super V, ? extends R> asNumeral() {
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



