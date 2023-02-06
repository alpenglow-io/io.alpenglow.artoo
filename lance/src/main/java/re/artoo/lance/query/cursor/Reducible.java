package re.artoo.lance.query.cursor;

import re.artoo.lance.func.*;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

public sealed interface Reducible<ELEMENT> extends Probe<ELEMENT> permits Cursor {
  default <FOLDED> Cursor<FOLDED> foldLeft(FOLDED initial, TryIntFunction2<? super FOLDED, ? super ELEMENT, ? extends FOLDED> operation) {
    return new Fold<>(this, Cursor.open(initial), (index, acceptance, folded, element) -> operation.invoke(index, folded, element));
  }
  default <FOLDED> Cursor<FOLDED> foldLeft(FOLDED initial, boolean acceptance, TryIntBooleanPredicate2<? super FOLDED, ? super ELEMENT> condition, TryIntBooleanFunction2<? super FOLDED, ? super ELEMENT, ? extends FOLDED> operation) {
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
  default <FOLDED> Cursor<FOLDED> foldRight(FOLDED initial, TryIntFunction2<? super FOLDED, ? super ELEMENT, ? extends FOLDED> operation) {
    return new Fold<>(this.reverse(), Cursor.open(initial), (index, truth, folded, element) -> operation.invoke(index, folded, element));
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
  private final TryIntBooleanFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> operation;
  private final boolean accepted;
  private final TryIntBooleanPredicate2<? super REDUCED, ? super ELEMENT> condition;

  Fold(
    Probe<? extends ELEMENT> probe,
    Probe<? extends REDUCED> initial,
    TryIntBooleanFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> operation
  ) {
    this(probe, initial, operation, true, (value, acceptance, reduced, element) -> true);
  }
  Fold(
    Probe<? extends ELEMENT> probe,
    Probe<? extends REDUCED> initial,
    TryIntBooleanFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> operation,
    boolean accepted,
    TryIntBooleanPredicate2<? super REDUCED, ? super ELEMENT> condition
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

  private static class Accept {
    private boolean value;
    static Accept value(boolean isTrue) {
      var accept = new Accept();
      accept.value = isTrue;
      return accept;
    }
    boolean that(boolean truth) {
      this.value = truth;
      return this.value;
    }
  }

  @Override
  public <R> R tick(TryIntFunction1<? super REDUCED, ? extends R> fetch) throws Throwable {
    final var accept = Accept.value(accepted);
    var reduced = initial.tick((index, it) -> it);
    while (probe.hasNext()) {
      final var constant = reduced;
      reduced = probe.tick((index, element) -> accept.that(condition.invoke(index, accept.value, constant, element))
        ? operation.invoke(index, accept.value, constant, element)
        : constant
      );
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
