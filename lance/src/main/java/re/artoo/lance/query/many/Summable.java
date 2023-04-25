package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.One;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

@SuppressWarnings("unchecked")
public interface Summable<ELEMENT> extends Queryable<ELEMENT> {
  default <N extends Number> One<N> sum(final TryFunction1<? super ELEMENT, ? extends N> select) {
    return () -> cursor().<N>map(select).reduce(this::sum);
  }

  private <NUMBER> NUMBER sum(NUMBER sum, NUMBER element) {
    return (NUMBER) switch (sum) {
      case Number it when element instanceof BigInteger e -> BigInteger.valueOf(it.longValue() + e.longValue());
      case Number it when element instanceof BigDecimal e -> BigDecimal.valueOf(it.doubleValue() + e.doubleValue());
      case Number it when element instanceof Double e -> it.doubleValue() + e;
      case Number it when element instanceof Float e -> it.floatValue() + e;
      case Number it when element instanceof Long e -> it.longValue() + e;
      case Number it when element instanceof Integer e -> it.intValue() + e;
      case Number it when element instanceof Short e -> it.shortValue() + e;
      case Number it when element instanceof Byte e -> it.byteValue() + e;
      case Number it -> it;
      case null, default -> element instanceof Number it ? it : null;
    };
  }

  default One<ELEMENT> sum() {
    return () -> cursor().reduce(this::sum).filter(Objects::nonNull);
  }
}

