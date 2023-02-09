package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryPredicate2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.tuple.Pair;
import re.artoo.lance.tuple.Tuple;
import re.artoo.lance.value.Array;

public interface Joinable<FIRST> extends Head<FIRST>, Tail<FIRST> {
  default <SECOND> Cursor<Pair<FIRST, SECOND>> fullOuterJoin(Cursor<SECOND> second, TryPredicate2<? super FIRST, ? super SECOND> condition) {
    return new FullOuterJoin<>(this, second, condition);
  }
}

final class FullOuterJoin<FIRST, SECOND, LEFT extends Head<FIRST> & Tail<FIRST>, RIGHT extends Head<SECOND> & Tail<SECOND>> implements Cursor<Pair<FIRST, SECOND>> {
  private final LEFT left;
  private final RIGHT right;
  private final TryPredicate2<? super FIRST, ? super SECOND> condition;

  private Array<Pair<FIRST, SECOND>> scrolled = Array.empty();

  private Head<SECOND> rightHead;
  private Head<FIRST> leftHead;
  private Tail<FIRST> leftTail;
  private Tail<SECOND> rightTail;
  private int forward = 0;
  FullOuterJoin(LEFT left, RIGHT right, TryPredicate2<? super FIRST, ? super SECOND> condition) {
    this.left = left;
    this.right = right;
    this.condition = condition;
  }

  @Override
  public <R> R scroll(TryIntFunction1<? super Pair<FIRST, SECOND>, ? extends R> fetch) throws Throwable {
    Pair<FIRST, SECOND> pair = null;
    if (left.hasNext()) {
      if (rightHead == null || !rightHead.hasNext()) rightHead = right.head();
      pair = Tuple.of(left.scroll(), null);
      final var before = scrolled.length();
      while (rightHead.hasNext() && before == scrolled.length()) {
        final var second = right.scroll();
        if (condition.invoke(pair.first(), second)) scrolled = scrolled.push((pair = Tuple.of(pair.first(), second)));
      }
    } else if (right.hasNext()) {
      if (leftHead == null || !leftHead.hasNext()) leftHead = left.head();
      pair = Tuple.of(null, right.scroll());
      final var before = scrolled.length();
      while (leftHead.hasNext() && before == scrolled.length()) {
        final var first = left.scroll();
        final var found = Tuple.of(first, pair.second());
        if (condition.invoke(first, pair.second()) && scrolled.notContains(found)) scrolled = scrolled.push(found);
      }
    }

    return fetch.invoke(0, pair);
  }

  @Override
  public boolean hasNext() {
    if (!left.hasNext() && !right.hasNext()) scrolled = Array.empty();
    return left.hasNext() || right.hasNext();
  }

  @Override
  public <R> R scrollback(TryIntFunction1<? super Pair<FIRST, SECOND>, ? extends R> fetch) throws Throwable {
    Pair<FIRST, SECOND> pair = null;
    if (left.hasPrior()) {
      if (rightTail == null || !rightTail.hasPrior()) rightTail = right.tail();
      pair = Tuple.of(left.scrollback(), null);
      final var before = scrolled.length();
      while (rightTail.hasPrior() && before == scrolled.length()) {
        final var second = right.scrollback();
        if (condition.invoke(pair.first(), second)) scrolled = scrolled.push((pair = Tuple.of(pair.first(), second)));
      }
    } else if (right.hasPrior()) {
      if (leftTail == null || !leftTail.hasPrior()) leftTail = left.tail();
      pair = Tuple.of(null, right.scrollback());
      final var before = scrolled.length();
      while (leftTail.hasPrior() && before == scrolled.length()) {
        final var first = left.scrollback();
        final var found = Tuple.of(first, pair.second());
        if (condition.invoke(first, pair.second()) && scrolled.notContains(found)) scrolled = scrolled.push(found);
      }
    }

    return fetch.invoke(0, pair);
  }

  @Override
  public boolean hasPrior() {
    if (!left.hasPrior() && !right.hasPrior()) scrolled = Array.empty();
    return left.hasPrior() || right.hasPrior();
  }
}
