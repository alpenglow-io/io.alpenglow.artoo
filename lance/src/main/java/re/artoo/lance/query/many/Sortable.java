package re.artoo.lance.query.many;

import re.artoo.lance.Queryable;
import re.artoo.lance.experimental.Array;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.Many;

import java.util.Comparator;

import static java.lang.Integer.compare;
import static java.util.Comparator.comparingInt;

public interface Sortable<ELEMENT> extends Queryable<ELEMENT> {
  default Many<ELEMENT> order(Arrangement<? super ELEMENT> arrangement) {
    return () -> cursor()
      .fold(Array.<ELEMENT>none(), Array::push)
      .map(array -> array.sortBy(arrangement.arrange()))
      .flatMap(Array::cursor);
  }

  default Many<ELEMENT> order(
    Arrangement<? super ELEMENT> first,
    Arrangement<? super ELEMENT> second
  ) {
    //noinspection unchecked
    return () -> cursor()
      .fold(Array.<ELEMENT>none(), Array::push)
      .map(array -> array.sortBy(
        first.arrange()
          .thenComparing(it -> (ELEMENT) it, second.arrange())))
      .flatMap(Array::cursor);
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
          fifth.arrange()
            .thenComparing(it -> (ELEMENT) it, forth.arrange())
            .thenComparing(it -> (ELEMENT) it, third.arrange())
            .thenComparing(it -> (ELEMENT) it, second.arrange())
            .thenComparing(it -> (ELEMENT) it, first.arrange())
        )
      )
      .flatMap(Array::cursor);
  }

  enum by {
    Arrange;

    public <ELEMENT, FIELD> Comparator<ELEMENT> ascending(TryFunction1<? super ELEMENT, ? extends FIELD> field) {
      return comparingInt(it -> field.apply(it).hashCode());
    }

    public <ELEMENT> Comparator<ELEMENT> ascending() {
      return ascending(it -> it);
    }

    public <ELEMENT, FIELD> Comparator<ELEMENT> descending(TryFunction1<? super ELEMENT, ? extends FIELD> field) {
      return Comparator.<ELEMENT>comparingInt(it -> field.apply(it).hashCode()).reversed();
    }

    public <ELEMENT> Comparator<ELEMENT> descending() {
      return descending(it -> it);
    }
  }

  interface Arrangement<T> extends TryFunction1<by, Comparator<T>> {
    default Comparator<T> arrange() {
      return apply(by.Arrange);
    }
  }
}
