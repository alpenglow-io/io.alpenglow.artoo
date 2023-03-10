package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.One;

import java.math.BigDecimal;
import java.math.BigInteger;

@SuppressWarnings("unchecked")
public interface Summable<T> extends Queryable<T> {
  default <N extends Number> One<N> sum(final TryFunction1<? super T, ? extends N> select) {
    return () -> cursor().<N>map(select).reduce(this::sum);
  }

  private <N> N sum(N sum, N element) {
    return sum != null ?
      (N) switch (sum) {
        case Number it when element instanceof Number e -> it.doubleValue() + e.doubleValue();
        case Number it -> it;
        default -> element instanceof Number it ? it : null;
      }
      : (N) (element instanceof Number it ? it : null);
  }

  default One<T> sum() {
    return () -> cursor().reduce(this::sum);
  }
}

