package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

public sealed interface Reducer<ELEMENT> extends Fetcher<ELEMENT> permits Cursor {
  default Cursor<String> left(String initial, TryFunction2<? super String, ? super ELEMENT, ? extends String> operation) {
    return left(() -> initial, operation);
  }

  default <REDUCED> Cursor<REDUCED> left(TrySupplier1<? extends REDUCED> initial, TryFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> operation) {
    return left(initial, (index, reduced, element) -> operation.invoke(reduced, element));
  }

  default <REDUCED> Cursor<REDUCED> left(TrySupplier1<? extends REDUCED> initial, TryIntFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> operation) {
    return new Left<>(this, Cursor.lazy(initial), operation);
  }

  default <REDUCED> Cursor<REDUCED> right(TryIntFunction1<? super ELEMENT, ? extends REDUCED> initial, TryIntFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> fold) {
    return new Right<>(this, initial, fold);
  }
}

final class Right<ELEMENT, REDUCED> implements Cursor<REDUCED> {
  private final Fetcher<? extends ELEMENT> fetcher;
  private final TryIntFunction1<? super ELEMENT, ? extends REDUCED> initial;
  private final TryIntFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> fold;

  Right(Fetcher<? extends ELEMENT> fetcher, TryIntFunction1<? super ELEMENT, ? extends REDUCED> initial, TryIntFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> fold) {
    this.fetcher = fetcher;
    this.initial = initial;
    this.fold = fold;
  }

  @Override
  public <TO> TO as(Routine<REDUCED, TO> routine) {
    return null;
  }

  @Override
  public <R> R fetch(TryIntFunction1<? super REDUCED, ? extends R> detach) throws Throwable {
    var reduced = fetcher.fetch(initial);
    while (fetcher.hasNext()) {
      final var constant = reduced;
      reduced = (reduced = fetcher.fetch((index, element) -> fold.invoke(index, constant, element))) == null ? constant : reduced;
    }
    return detach.invoke(0, reduced);
  }

  @Override
  public boolean hasNext() {
    return fetcher.hasNext();
  }
}

final class Left<ELEMENT, REDUCED> implements Cursor<REDUCED> {
  private final Fetcher<? extends ELEMENT> fetcher;
  private final Fetcher<? extends REDUCED> initial;
  private final TryIntFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> fold;

  Left(Fetcher<? extends ELEMENT> fetcher, Fetcher<? extends REDUCED> initial, TryIntFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> reducer) {
    this.fetcher = fetcher;
    this.initial = initial;
    this.fold = reducer;
  }

  @Override
  public <TO> TO as(Routine<REDUCED, TO> routine) {
    return null;
  }

  @Override
  public <R> R fetch(TryIntFunction1<? super REDUCED, ? extends R> detach) throws Throwable {
    var reduced = initial.fetch((index, it) -> it);
    while (fetcher.hasNext()) {
      final var constant = reduced;
      reduced = (reduced = fetcher.fetch((index, element) -> fold.invoke(index, constant, element))) == null ? constant : reduced;
    }
    return detach.invoke(0, reduced);
  }

  @Override
  public boolean hasNext() {
    return fetcher.hasNext() || initial.hasNext();
  }
}
