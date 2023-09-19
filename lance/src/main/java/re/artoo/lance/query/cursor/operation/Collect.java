package re.artoo.lance.query.cursor.operation;

import com.java.lang.Exceptionable;
import re.artoo.lance.func.TryIntConsumer2;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetchable;

public final class Collect<ELEMENT, COLLECTED> implements Cursor<COLLECTED>, Exceptionable {
  private final Fetchable<ELEMENT> fetchable;
  private final TryIntConsumer2<? super ELEMENT, ? super COLLECTED> operation;
  private COLLECTED collected;
  private boolean hasCollected = true;

  public Collect(Fetchable<ELEMENT> fetchable, COLLECTED initial, TryIntConsumer2<? super ELEMENT, ? super COLLECTED> operation) {
    this.fetchable = fetchable;
    this.operation = operation;
    this.collected = initial;
  }

  @Override
  public boolean canFetch() throws java.lang.Throwable {
    if (hasCollected) {
      while (fetchable.canFetch()) {
        collected = fetchable.fetch((index, element) -> {
          operation.invoke(index, element, collected);
          return collected;
        });
      }
    }
    return hasCollected;
  }

  @Override
  public <NEXT> NEXT fetch(TryIntFunction1<? super COLLECTED, ? extends NEXT> then) throws java.lang.Throwable {
    try {
      return hasCollected || canFetch() ? then.invoke(0, collected) : throwing(() -> Fetchable.Exception.of("fold", "foldable"));
    } finally {
      hasCollected = false;
    }
  }
}
