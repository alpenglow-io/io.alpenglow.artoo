package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.query.One;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

@SuppressWarnings("DuplicatedCode")
public interface Extremable<ELEMENT> extends Queryable<ELEMENT> {
  private static <ELEMENT> TryFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> maximising() {
    return (max, selected) -> switch (max) {
      case null -> selected instanceof Number || selected instanceof Character ? selected : null;
      case Float it when selected instanceof Float el -> it > el ? max : selected;
      case Double it when selected instanceof Double el -> it > el ? max : selected;
      case Byte it when selected instanceof Byte el -> it > el ? max : selected;
      case Short it when selected instanceof Short el -> it > el ? max : selected;
      case Integer it when selected instanceof Integer el -> it > el ? max : selected;
      case Long it when selected instanceof Long el -> it > el ? max : selected;
      case BigDecimal it when selected instanceof BigDecimal el -> it.compareTo(el) < 0 ? max : selected;
      case BigInteger it when selected instanceof BigInteger el -> it.compareTo(el) < 0 ? max : selected;
      case Character it when selected instanceof Character el -> it.compareTo(el) < 0 ? max : selected;
      default -> max instanceof Number || selected instanceof Character ? max : null;
    };
  }
  private static <ELEMENT> TryFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> minimising() {
    return (min, selected) -> switch (min) {
      case null -> selected instanceof Number || selected instanceof Character ? selected : null;
      case Float it when selected instanceof Float el -> it < el ? min : selected;
      case Double it when selected instanceof Double el -> it < el ? min : selected;
      case Byte it when selected instanceof Byte el -> it < el ? min : selected;
      case Short it when selected instanceof Short el -> it < el ? min : selected;
      case Integer it when selected instanceof Integer el -> it < el ? min : selected;
      case Long it when selected instanceof Long el -> it < el ? min : selected;
      case BigDecimal it when selected instanceof BigDecimal el -> it.compareTo(el) > 0 ? min : selected;
      case BigInteger it when selected instanceof BigInteger el -> it.compareTo(el) > 0 ? min : selected;
      case Character it when selected instanceof Character el -> it.compareTo(el) > 0 ? min : selected;
      default -> min instanceof Number || selected instanceof Character ? min : null;
    };
  }
  default <N extends Number> One<N> max(final TryFunction1<? super ELEMENT, ? extends N> select) {
    return () -> cursor().nonNull().<N>map(select).reduce(maximising());
  }

  default One<ELEMENT> max() {
    return () -> cursor().reduce(maximising()).filter(Objects::nonNull);
  }

  default <N extends Number> One<N> min(final TryFunction1<? super ELEMENT, ? extends N> select) {
    return () -> cursor().nonNull().<N>map(select).reduce(minimising());
  }

  default One<ELEMENT> min() {
    return () -> cursor().reduce(minimising()).filter(Objects::nonNull);
  }
}
