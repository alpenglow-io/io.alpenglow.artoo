package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface Reducer<ELEMENT> extends Fetcher<ELEMENT> {


  default <REDUCED> Cursor<REDUCED> reduce(REDUCED initial, TryFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> operation) {
    return new Reduce<>(this, initial, (index, reduced, element) -> operation.invoke(reduced, element));
  }

  default <REDUCED> Cursor<REDUCED> reduce(REDUCED initial, TryIntFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> reducer) {
    return new Reduce<>(this, initial, reducer);
  }
}

final class Reduce<ELEMENT, REDUCED> implements Cursor<REDUCED> {
  private final Fetcher<ELEMENT> fetcher;
  private final REDUCED initial;
  private final TryIntFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> reducer;

  Reduce(Fetcher<ELEMENT> fetcher, REDUCED initial, TryIntFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> reducer) {
    this.fetcher = fetcher;
    this.initial = initial;
    this.reducer = reducer;
  }

  @Override
  public <TO> TO as(Routine<REDUCED, TO> routine) {
    return null;
  }

  @Override
  public <R> R fetch(TryIntFunction1<? super REDUCED, ? extends R> detach) throws Throwable {
    var reduced = initial;
    while (fetcher.hasNext()) {
      final var constant = reduced;
      reduced = fetcher.fetch((index, element) -> reducer.invoke(index, constant, element));
    }
    return detach.invoke(0, reduced);
  }

  @Override
  public boolean hasNext() {
    return fetcher.hasNext();
  }
}
