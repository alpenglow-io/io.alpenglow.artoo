package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.value.Array;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.Many;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.function.Function;

import static java.lang.Integer.compare;

@SuppressWarnings("unchecked")
public interface Sortable<ELEMENT> extends Queryable<ELEMENT> {
  private Many<ELEMENT> order(Comparator<? super ELEMENT> comparator) {
    return () -> cursor()
      .fold(Array.<ELEMENT>none(), Array::push)
      .map(array -> array.sortBy(comparator))
      .flatMap(Array::cursor);
  }

  default Many<ELEMENT> order(Arrangement<? super ELEMENT> arrangement) {
    return order(arrangement.arrange());
  }

  class El<ELEMENT> {

    Function<ELEMENT, ELEMENT> self = it -> (ELEMENT) it;
  }
  private <T> Function<T, T> self() {
    return it -> (T) it;
  }

  default Many<ELEMENT> order(
    Arrangement<? super ELEMENT> first,
    Arrangement<? super ELEMENT> second
  ) {
    return order(first.arrange().thenComparing(it -> (ELEMENT) it, second.arrange()));
  }

  default Many<ELEMENT> order(
    Arrangement<? super ELEMENT> first,
    Arrangement<? super ELEMENT> second,
    Arrangement<? super ELEMENT> third
  ) {
    //noinspection unchecked
    return () -> cursor()
      .fold(Array.<ELEMENT>none(), Array::push)
      .map(array -> array.sortBy(
          first.arrange()
            .thenComparing(it -> (ELEMENT) it, second.arrange())
            .thenComparing(it -> (ELEMENT) it, third.arrange())
        )
      )
      .flatMap(Array::cursor);
  }

  default Many<ELEMENT> order(
    Arrangement<? super ELEMENT> first,
    Arrangement<? super ELEMENT> second,
    Arrangement<? super ELEMENT> third,
    Arrangement<? super ELEMENT> forth
  ) {
    //noinspection unchecked
    return () -> cursor()
      .fold(Array.<ELEMENT>none(), Array::push)
      .map(array -> array.sortBy(
          first.arrange()
            .thenComparing(it -> (ELEMENT) it, second.arrange())
            .thenComparing(it -> (ELEMENT) it, third.arrange())
            .thenComparing(it -> (ELEMENT) it, forth.arrange())
        )
      )
      .flatMap(Array::cursor);
  }

  default Many<ELEMENT> order(
    Arrangement<? super ELEMENT> first,
    Arrangement<? super ELEMENT> second,
    Arrangement<? super ELEMENT> third,
    Arrangement<? super ELEMENT> forth,
    Arrangement<? super ELEMENT> fifth
  ) {
    //noinspection unchecked
    return () -> cursor()
      .fold(Array.<ELEMENT>none(), Array::push)
      .map(array -> array.sortBy(
          first.arrange()
            .thenComparing(it -> (ELEMENT) it, second.arrange())
            .thenComparing(it -> (ELEMENT) it, third.arrange())
            .thenComparing(it -> (ELEMENT) it, forth.arrange())
            .thenComparing(it -> (ELEMENT) it, fifth.arrange())
        )
      )
      .flatMap(Array::cursor);
  }

  enum by {
    Arrange;

    public <ELEMENT> Comparator<? super ELEMENT> comparing(Comparator<? super ELEMENT> comparator) {
      return comparator;
    }

    public <ELEMENT, FIELD> Comparator<ELEMENT> ascending(TryFunction1<? super ELEMENT, ? extends FIELD> field) {
      return (element1, element2) -> {
        FIELD applied1 = field.apply(element1);
        FIELD applied2 = field.apply(element2);
        return switch (applied1) {
          case String field1 when applied2 instanceof String field2 -> field1.compareTo(field2);
          case Integer field1 when applied2 instanceof Integer field2 -> field1.compareTo(field2);
          case Float field1 when applied2 instanceof Float field2 -> field1.compareTo(field2);
          case Double field1 when applied2 instanceof Double field2 -> field1.compareTo(field2);
          case Long field1 when applied2 instanceof Long field2 -> field1.compareTo(field2);
          case Byte field1 when applied2 instanceof Byte field2 -> field1.compareTo(field2);
          case Short field1 when applied2 instanceof Short field2 -> field1.compareTo(field2);
          case BigInteger field1 when applied2 instanceof BigInteger field2 -> field1.compareTo(field2);
          case BigDecimal field1 when applied2 instanceof BigDecimal field2 -> field1.compareTo(field2);
          case Boolean field1 when applied2 instanceof Boolean field2 -> field1.compareTo(field2);
          case Object field1 when applied2 instanceof Object field2 -> compare(field1.hashCode(), field2.hashCode());
          default -> 0;
        };
      };
    }

    public <ELEMENT> Comparator<ELEMENT> ascending() {
      return ascending(it -> it);
    }

    public <ELEMENT, FIELD> Comparator<ELEMENT> descending(TryFunction1<? super ELEMENT, ? extends FIELD> field) {
      return this.<ELEMENT, FIELD>ascending(field).reversed();
    }

    public <ELEMENT> Comparator<ELEMENT> descending() {
      return descending(it -> it);
    }
  }

  interface Arrangement<T> extends TryFunction1<by, Comparator<? super T>> {
    default Comparator<? super T> arrange() {
      return this.apply(by.Arrange);
    }
  }
}
