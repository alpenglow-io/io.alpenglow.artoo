package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

public sealed interface Reducible<ELEMENT> extends Inquiry<ELEMENT> permits Cursor {
  default <REDUCED> Cursor<REDUCED> foldLeft(REDUCED initial, TryIntFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> fold) {
    return new Fold<>(this, Cursor.open(initial), fold);
  }
  default <REDUCED> Cursor<REDUCED> foldLeft(REDUCED initial, TryFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> fold) {
    return foldLeft(initial, (index, reduced, element) -> fold.invoke(reduced, element));
  }
  default Cursor<ELEMENT> reduceLeft(TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> reduce) {
    return new Reduce<>(this, reduce);
  }
  default Cursor<ELEMENT> reduceLeft(TryFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> reduce) {
    return reduceLeft((index, reduced, element) -> reduce.invoke(reduced, element));
  }
  default <REDUCED> Cursor<REDUCED> foldRight(REDUCED initial, TryIntFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> fold) {
    return new Fold<>(this.reversal(), Cursor.open(initial), fold);
  }
  default <REDUCED> Cursor<REDUCED> foldRight(REDUCED initial, TryFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> fold) {
    return foldLeft(initial, (index, reduced, element) -> fold.invoke(reduced, element));
  }
  default Cursor<ELEMENT> reduceRight(TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> reduce) {
    return new Reduce<>(this.reversal(), reduce);
  }
  default Cursor<ELEMENT> reduceRight(TryFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> reduce) {
    return reduceRight((index, reduced, element) -> reduce.invoke(reduced, element));
  }
}
final class Fold<ELEMENT, REDUCED> implements Cursor<REDUCED> {
  private final Inquiry<? extends ELEMENT> inquiry;
  private final Inquiry<? extends REDUCED> initial;
  private final TryIntFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> fold;

  Fold(Inquiry<? extends ELEMENT> inquiry, Inquiry<? extends REDUCED> initial, TryIntFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> reducer) {
    this.inquiry = inquiry;
    this.initial = initial;
    this.fold = reducer;
  }

  @Override
  public <TO> TO as(Routine<REDUCED, TO> routine) {
    return null;
  }

  @Override
  public <R> R traverse(TryIntFunction1<? super REDUCED, ? extends R> fetch) throws Throwable {
    var reduced = initial.traverse((index, it) -> it);
    while (inquiry.hasNext()) {
      final var constant = reduced;
      reduced = (reduced = inquiry.traverse((index, element) -> fold.invoke(index, constant, element))) == null ? constant : reduced;
    }
    return fetch.invoke(0, reduced);
  }

  @Override
  public boolean hasNext() {
    return inquiry.hasNext() || initial.hasNext();
  }
}

final class Reduce<ELEMENT> implements Cursor<ELEMENT> {
  private final Inquiry<? extends ELEMENT> inquiry;
  private final TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> fold;

  Reduce(Inquiry<? extends ELEMENT> inquiry, TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> reducer) {
    this.inquiry = inquiry;
    this.fold = reducer;
  }

  @Override
  public <TO> TO as(Routine<ELEMENT, TO> routine) {
    return null;
  }

  @Override
  public <R> R traverse(TryIntFunction1<? super ELEMENT, ? extends R> fetch) throws Throwable {
    var reduced = inquiry.traverse((index, it) -> it);
    while (inquiry.hasNext()) {
      final var constant = reduced;
      reduced = (reduced = inquiry.traverse((index, element) -> fold.invoke(index, constant, element))) == null ? constant : reduced;
    }
    return fetch.invoke(0, reduced);
  }

  @Override
  public boolean hasNext() {
    return inquiry.hasNext();
  }
}
