package re.artoo.lance.query.cursor;

import re.artoo.lance.func.*;
import re.artoo.lance.query.Cursor;

public sealed interface Reducible<ELEMENT> extends Head<ELEMENT>, Tail<ELEMENT> permits Cursor {
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
  private final Head<? extends ELEMENT> head;
  private final Head<? extends REDUCED> initial;
  private final TryIntBooleanFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> operation;
  private final boolean accepted;
  private final TryIntBooleanPredicate2<? super REDUCED, ? super ELEMENT> condition;

  Fold(
    Head<? extends ELEMENT> head,
    Head<? extends REDUCED> initial,
    TryIntBooleanFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> operation
  ) {
    this(head, initial, operation, true, (value, acceptance, reduced, element) -> true);
  }
  Fold(
    Head<? extends ELEMENT> head,
    Head<? extends REDUCED> initial,
    TryIntBooleanFunction2<? super REDUCED, ? super ELEMENT, ? extends REDUCED> operation,
    boolean accepted,
    TryIntBooleanPredicate2<? super REDUCED, ? super ELEMENT> condition
  ) {
    this.head = head;
    this.initial = initial;
    this.operation = operation;
    this.accepted = accepted;
    this.condition = condition;
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
  public <R> R scroll(TryIntFunction1<? super REDUCED, ? extends R> fetch) throws Throwable {
    final var accept = Accept.value(accepted);
    var reduced = initial.scroll();
    while (head.hasNext()) {
      final var constant = reduced;
      reduced = head.scroll((index, element) -> accept.that(condition.invoke(index, accept.value, constant, element))
        ? operation.invoke(index, accept.value, constant, element)
        : constant
      );
    }
    return fetch.invoke(0, reduced);
  }

  @Override
  public boolean hasNext() {
    return head.hasNext() || initial.hasNext();
  }
}

final class Reduce<ELEMENT> implements Cursor<ELEMENT> {
  private final Head<? extends ELEMENT> head;
  private final TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> fold;

  Reduce(Head<? extends ELEMENT> head, TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> reducer) {
    this.head = head;
    this.fold = reducer;
  }

  @Override
  public <R> R scroll(TryIntFunction1<? super ELEMENT, ? extends R> fetch) throws Throwable {
    var reduced = head.scroll((index, it) -> it);
    while (head.hasNext()) {
      final var constant = reduced;
      reduced = head.scroll((index, element) -> fold.invoke(index, constant, element));
      reduced = reduced == null ? constant : reduced;
    }
    return fetch.invoke(0, reduced);
  }

  @Override
  public boolean hasNext() {
    return head.hasNext();
  }
}
