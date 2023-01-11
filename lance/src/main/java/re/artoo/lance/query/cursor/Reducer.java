package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntPredicate1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface Reducer<ELEMENT> extends Fetcher<ELEMENT> {
  private static <REDUCED> TryFunction2<REDUCED, ? extends REDUCED, REDUCED> maximise() {
    return (max, selected) -> switch (max) {
      case null -> selected;
      case Float it when selected instanceof Float el -> it > el ? max : selected;
      case Double it when selected instanceof Double el -> it > el ? max : selected;
      case Byte it when selected instanceof Byte el -> it > el ? max : selected;
      case Short it when selected instanceof Short el -> it > el ? max : selected;
      case Integer it when selected instanceof Integer el -> it > el ? max : selected;
      case Long it when selected instanceof Long el -> it > el ? max : selected;
      case BigDecimal it when selected instanceof BigDecimal el -> it.compareTo(el) < 0 ? max : selected;
      case BigInteger it when selected instanceof BigInteger el -> it.compareTo(el) < 0 ? max : selected;
      default -> max;
    };
  }

  private static <REDUCED> TryFunction2<REDUCED, ? extends REDUCED, REDUCED> minimise() {
    return (min, selected) -> switch (min) {
      case null -> selected;
      case Float it when selected instanceof Float el -> it < el ? min : selected;
      case Double it when selected instanceof Double el -> it < el ? min : selected;
      case Byte it when selected instanceof Byte el -> it < el ? min : selected;
      case Short it when selected instanceof Short el -> it < el ? min : selected;
      case Integer it when selected instanceof Integer el -> it < el ? min : selected;
      case Long it when selected instanceof Long el -> it < el ? min : selected;
      case BigDecimal it when selected instanceof BigDecimal el -> it.compareTo(el) > 0 ? min : selected;
      case BigInteger it when selected instanceof BigInteger el -> it.compareTo(el) > 0 ? min : selected;
      default -> min;
    };
  }

  default <REDUCED, SELECTED> Cursor<REDUCED> reduce(
    REDUCED reduced,
    TryIntPredicate1<? super ELEMENT> filter,
    TryIntFunction1<? super ELEMENT, ? extends SELECTED> map,
    TryFunction2<? super REDUCED, ? super SELECTED, ? extends REDUCED> operation
  ) {
    return new Reduce<>(this, reduced, filter, map, operation);
  }

  default <REDUCED> Cursor<REDUCED> reduce(REDUCED reduced, TryIntPredicate1<? super ELEMENT> filter, TryFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> operation) {
    return reduce(reduced, filter, (index, it) -> it, operation);
  }

  default <REDUCED> Cursor<REDUCED> reduce(REDUCED reduced, TryFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> operation) {
    return reduce(reduced, (index, it) -> true, (index, it) -> it, operation);
  }

  default <REDUCED extends Number> Cursor<REDUCED> max(TryIntPredicate1<? super ELEMENT> filter, TryIntFunction1<? super ELEMENT, ? extends REDUCED> map) {
    return reduce(null, filter, map, maximise());
  }

  default <REDUCED extends Number> Cursor<REDUCED> min(TryIntPredicate1<? super ELEMENT> filter, TryIntFunction1<? super ELEMENT, ? extends REDUCED> map) {
    return reduce(null, filter, map, minimise());
  }

  default Cursor<ELEMENT> max(TryIntPredicate1<? super ELEMENT> filter) {
    return reduce(null, filter, maximise());
  }

  default Cursor<ELEMENT> min(TryIntPredicate1<? super ELEMENT> filter) {
    return reduce(null, filter, minimise());
  }

  default Cursor<ELEMENT> max() {
    return reduce(null, maximise());
  }

  default Cursor<ELEMENT> min() {
    return reduce(null, minimise());
  }

}

final class Reduce<ELEMENT, SELECTED, REDUCED> implements Cursor<REDUCED> {
  private final Fetcher<ELEMENT> fetcher;
  private final REDUCED initial;
  private final TryIntPredicate1<? super ELEMENT> filter;
  private final TryIntFunction1<? super ELEMENT, ? extends SELECTED> map;
  private final TryFunction2<? super REDUCED, ? super SELECTED, ? extends REDUCED> operation;

  Reduce(Fetcher<ELEMENT> fetcher, REDUCED initial, TryIntPredicate1<? super ELEMENT> filter, TryIntFunction1<? super ELEMENT, ? extends SELECTED> map, TryFunction2<? super REDUCED, ? super SELECTED, ? extends REDUCED> operation) {
    this.fetcher = fetcher;
    this.initial = initial;
    this.filter = filter;
    this.map = map;
    this.operation = operation;
  }

  @Override
  public <TO> TO as(Routine<REDUCED, TO> routine) {
    return null;
  }

  @Override
  public <R> R fetch(TryIntFunction1<? super REDUCED, ? extends R> detach) throws Throwable {
    var reduced = initial;
    while (fetcher.hasNext()) {
      final var computed = reduced;
      reduced = fetcher.fetch((index, element) ->
        filter.invoke(index, element)
          ? operation.invoke(computed, map.invoke(index, element))
          : computed
      );
    }
    return detach.invoke(0, reduced);
  }

  @Override
  public boolean hasNext() {
    return fetcher.hasNext();
  }
}
