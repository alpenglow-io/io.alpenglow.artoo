package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.func.TryIntPredicate2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

public sealed interface Reducible<ELEMENT> extends Probe<ELEMENT> permits Cursor {
  default <REDUCED> Cursor<REDUCED> foldLeft(REDUCED initial, TryIntFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> operation) {
    return new Fold<>(this, Cursor.open(initial), operation);
  }

  default <REDUCED> Cursor<REDUCED> foldLeft(REDUCED initial, boolean acceptance, TryIntPredicate2<? super REDUCED, ? super ELEMENT> condition, TryIntFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> operation) {
    return new Fold<>(this, Cursor.open(initial), operation, acceptance, condition);
  }
  default <REDUCED> Cursor<REDUCED> foldLeft(REDUCED initial, TryFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> operation) {
    return foldLeft(initial, (index, reduced, element) -> operation.invoke(reduced, element));
  }
  default Cursor<ELEMENT> reduceLeft(TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> reduce) {
    return new Reduce<>(this, reduce);
  }
  default Cursor<ELEMENT> reduceLeft(TryFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> reduce) {
    return reduceLeft((index, reduced, element) -> reduce.invoke(reduced, element));
  }
  default <REDUCED> Cursor<REDUCED> foldRight(REDUCED initial, TryIntFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> fold) {
    return new Fold<>(this.reverse(), Cursor.open(initial), fold);
  }
  default <REDUCED> Cursor<REDUCED> foldRight(REDUCED initial, TryFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> fold) {
    return foldLeft(initial, (index, reduced, element) -> fold.invoke(reduced, element));
  }
  default Cursor<ELEMENT> reduceRight(TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> reduce) {
    return new Reduce<>(this.reverse(), reduce);
  }
  default Cursor<ELEMENT> reduceRight(TryFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> reduce) {
    return reduceRight((index, reduced, element) -> reduce.invoke(reduced, element));
  }
}
final class Fold<ELEMENT, REDUCED> implements Cursor<REDUCED> {
  private final Probe<? extends ELEMENT> probe;
  private final Probe<? extends REDUCED> initial;
  private final TryIntFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> operation;
  private final boolean accepted;
  private final TryIntPredicate2<? super REDUCED, ? super ELEMENT> condition;

  Fold(
    Probe<? extends ELEMENT> probe,
    Probe<? extends REDUCED> initial,
    TryIntFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> operation
  ) {
    this(probe, initial, operation, true, (integer, reduced, element) -> true);
  }
  Fold(
    Probe<? extends ELEMENT> probe,
    Probe<? extends REDUCED> initial,
    TryIntFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> operation,
    boolean accepted,
    TryIntPredicate2<? super REDUCED, ? super ELEMENT> condition
  ) {
    this.probe = probe;
    this.initial = initial;
    this.operation = operation;
    this.accepted = accepted;
    this.condition = condition;
  }

  @Override
  public <TO> TO as(Routine<REDUCED, TO> routine) {
    return null;
  }

  private static class Acceptance {
    boolean isTrue;
    private Acceptance(boolean isTrue) {
      this.isTrue = isTrue;
    }
  }

  @Override
  public <R> R tick(TryIntFunction1<? super REDUCED, ? extends R> fetch) throws Throwable {
    final var acceptance = new Acceptance(accepted);
    var reduced = initial.tick((index, it) -> it);
    while (probe.hasNext()) {
      final var constant = reduced;
      reduced = probe.tick((index, element) -> (acceptance.isTrue &= condition.invoke(index, constant, element))
        ? operation.invoke(index, constant, element)
        : constant
      );
      reduced = reduced == null ? constant : reduced;
    }
    return fetch.invoke(0, reduced);
  }

  @Override
  public boolean hasNext() {
    return probe.hasNext() || initial.hasNext();
  }
}

final class Reduce<ELEMENT> implements Cursor<ELEMENT> {
  private final Probe<? extends ELEMENT> probe;
  private final TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> fold;

  Reduce(Probe<? extends ELEMENT> probe, TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> reducer) {
    this.probe = probe;
    this.fold = reducer;
  }

  @Override
  public <TO> TO as(Routine<ELEMENT, TO> routine) {
    return null;
  }

  @Override
  public <R> R tick(TryIntFunction1<? super ELEMENT, ? extends R> fetch) throws Throwable {
    var reduced = probe.tick((index, it) -> it);
    while (probe.hasNext()) {
      final var constant = reduced;
      reduced = probe.tick((index, element) -> fold.invoke(index, constant, element));
      reduced = reduced == null ? constant : reduced;
    }
    return fetch.invoke(0, reduced);
  }

  @Override
  public boolean hasNext() {
    return probe.hasNext();
  }
}
