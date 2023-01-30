package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.One;

import java.math.BigDecimal;
import java.math.BigInteger;

@SuppressWarnings("unchecked")
public interface Summable<T> extends Queryable<T> {
  default <N extends Number> One<N> sum(final TryFunction1<? super T, ? extends N> select) {
    return () -> cursor().<N>map(select).reduceLeft(this::sum);
  }

  private <N> N sum(N sum, N element) {
    return (N) switch (sum) {
      case BigDecimal it when element instanceof BigDecimal e -> it.add(e);
      case BigInteger it when element instanceof BigInteger e -> it.add(e);
      case Byte it when element instanceof Byte e -> it + e;
      case Short it when element instanceof Short e -> it + e;
      case Integer it when element instanceof Integer e -> it + e;
      case Long it when element instanceof Long e -> it + e;
      case Float it when element instanceof Float e -> it + e;
      case Double it when element instanceof Double e -> it + e;
      default -> element;
    };
  }

  default One<T> sum() {
    return () -> cursor().reduceLeft(this::sum);
  }
}

