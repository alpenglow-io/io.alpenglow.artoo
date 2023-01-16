package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

public sealed interface Reducer<ELEMENT> extends Fetcher<ELEMENT> permits Cursor {
  default Cursor<String> reduce(String initial, TryFunction2<? super String, ? super ELEMENT, ? extends String> operation) {
    return reduce(() -> initial, operation);
  }
  default <REDUCED> Cursor<REDUCED>  reduce(TrySupplier1<? extends REDUCED> initial, TryFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> operation) {
    return reduce(initial, (index, reduced, element) -> operation.invoke(reduced, element));
  }
  default <REDUCED> Cursor<REDUCED> reduce(TrySupplier1<? extends REDUCED> initial, TryIntFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> operation) {
    return new Reduce<>(this, Cursor.lazy(initial), operation);
  }
}

final class Reduce<ELEMENT, REDUCED> implements Cursor<REDUCED> {
  private final Fetcher<? extends ELEMENT> fetcher;
  private final Fetcher<? extends REDUCED> initial;
  private final TryIntFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> reducer;

  Reduce(Fetcher<? extends ELEMENT> fetcher, Fetcher<? extends REDUCED> initial, TryIntFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> reducer) {
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
    var reduced = initial.fetch();
    while (fetcher.hasNext()) {
      final var constant = reduced;
      reduced = fetcher.fetch((index, element) -> reducer.invoke(index, constant, element));
    }
    return detach.invoke(0, reduced);
  }

  @Override
  public boolean hasNext() {
    return fetcher.hasNext() || initial.hasNext();
  }
}
