package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntPredicate2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.tuple.Pair;
import re.artoo.lance.tuple.Tuple;
import re.artoo.lance.value.Array;

public sealed interface Unifiable<HEAD> extends Probe<HEAD> permits Cursor {
  default <TAIL> Cursor<Pair<HEAD, TAIL>> horizontalUnion(Cursor<TAIL> tail, TryIntPredicate2<? super HEAD, ? super TAIL> condition) {
    return new HorizontalUnion<>(this, tail, condition);
  }
}

final class HorizontalUnion<HEAD, TAIL> implements Cursor<Pair<HEAD, TAIL>> {
  private final Cursor<HEAD> head;
  private final Cursor<TAIL> tail;
  private final TryIntPredicate2<? super HEAD, ? super TAIL> condition;
  private HEAD current;
  private int index = 0;
  HorizontalUnion(Cursor<HEAD> head, Cursor<TAIL> tail, TryIntPredicate2<? super HEAD, ? super TAIL> condition) {
    this.head = head;
    this.tail = tail;
    this.condition = condition;
  }

  @Override
  public <R> R tick(TryIntFunction1<? super Pair<HEAD, TAIL>, ? extends R> fetch) throws Throwable {
    switch (current) {
      case HEAD it when tail.hasNext() ->
    }
    if (tail.hasNext()) {
      final var tailed = tail.tick();
      if ((condition.invoke(index, headed, tailed))) return fetch.invoke(index++, Tuple.of(headed, tailed));
    }
    return null;
  }

  @Override
  public boolean hasNext() {
    return head.hasNext();
  }
}
