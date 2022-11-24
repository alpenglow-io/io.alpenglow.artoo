package lance.query.func;

import lance.func.Func;
import lance.func.Recursive.Tailrec;

import java.math.BigDecimal;
import java.math.BigInteger;

@SuppressWarnings("unchecked")
public final class Extreme<T, R extends Number, V> extends Tailrec<T, V, Extreme<T, R, V>> {
  public enum Type { Maximum(1), Minimum(-1);
    private final int eval;

    Type(final int eval) { this.eval = eval; }
  }

  private final V extreme;
  private final Extreme.Type type;
  private final Func.TryFunction<? super T, ? extends R> selector;

  private Extreme(final V extreme, final Type type, final Func.TryFunction<? super T, ? extends R> selector) {
    this.extreme = extreme;
    this.type = type;
    this.selector = selector;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Return<T, V, Extreme<T, R, V>> tryApply(final T element) throws Throwable {
    if (element != null) {
      final var selected = selector.tryApply(element);
      if (compare((R) extreme, selected) == type.eval) {
        return Return.with((V) selected, new Extreme<>((V) selected, type, selector));
      }

    }
    return Return.with(extreme, this);
  }

  private int compare(final R compared, final R selected) {
    if (compared == null && selected != null)
      return type.eval;
    return switch (selected) {
      case Byte b -> b > compared.byteValue() ? 1 : -1;
      case Short s -> s > compared.shortValue() ? 1 : -1;
      case Integer i -> i > compared.intValue() ? 1 : -1;
      case Long l -> l > compared.longValue() ? 1 : -1;
      case Float f -> f > compared.floatValue() ? 1 : -1;
      case Double d -> d > compared.doubleValue() ? 1 : -1;
      case BigInteger it -> it.longValue() > BigInteger.valueOf(compared.longValue()).longValue() ? 1 : -1;
      case BigDecimal it -> it.doubleValue() > BigDecimal.valueOf(compared.doubleValue()).doubleValue() ? 1 : -1;

      default -> type.eval * -1;
    };
  }

  public static <T, R extends Number, V> Extreme<T, R, V> max() {
    return new Extreme<>(null, Type.Maximum, it -> (R) it);
  }

  public static <T, R extends Number, V> Extreme<T, R, V> max(Func.TryFunction<? super T, ? extends R> selector) {
    return new Extreme<>(null, Type.Maximum, selector);
  }

  public static <T, R extends Number, V> Extreme<T, R, V> min() {
    return new Extreme<>(null, Type.Minimum, it -> (R) it);
  }

  public static <T, R extends Number, V> Extreme<T, R, V> min(Func.TryFunction<? super T, ? extends R> selector) {
    return new Extreme<>(null, Type.Minimum, selector);
  }
}
