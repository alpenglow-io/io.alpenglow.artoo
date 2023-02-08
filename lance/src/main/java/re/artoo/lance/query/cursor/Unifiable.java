package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntPredicate2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.tuple.Pair;
import re.artoo.lance.tuple.Tuple;

public sealed interface Unifiable<FIRST> extends Head<FIRST>, Tail<FIRST> permits Cursor {
  default <SECOND> Cursor<Pair<FIRST, SECOND>> horizontalUnion(Cursor<SECOND> tail, TryIntPredicate2<? super FIRST, ? super SECOND> condition) {
    return new HorizontalUnion<>(this, tail, condition);
  }
}

final class HorizontalUnion<FIRST, SECOND> implements Cursor<Pair<FIRST, SECOND>> {
  private final Cursor<FIRST> first;
  private final Cursor<SECOND> second;
  private final TryIntPredicate2<? super FIRST, ? super SECOND> condition;
  private FIRST current;
  private int index = 0;
  HorizontalUnion(Cursor<FIRST> first, Cursor<SECOND> second, TryIntPredicate2<? super FIRST, ? super SECOND> condition) {
    this.first = first;
    this.second = second;
    this.condition = condition;
  }

  @Override
  public <R> R scroll(TryIntFunction1<? super Pair<FIRST, SECOND>, ? extends R> fetch) throws Throwable {
    switch (current) {
      case FIRST it when second.hasNext() ->
    }
    if (second.hasNext()) {
      final var tailed = second.scroll();
      if ((condition.invoke(index, headed, tailed))) return fetch.invoke(index++, Tuple.of(headed, tailed));
    }
    return null;
  }

  @Override
  public boolean hasNext() {
    return first.hasNext();
  }
}
