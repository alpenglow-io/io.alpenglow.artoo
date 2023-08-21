package re.artoo.lance.query.cursor.operation;

import com.java.lang.Throwing;
import re.artoo.lance.func.TryIntConsumer2;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;

public final class Collect<ELEMENT, COLLECTED> implements Cursor<COLLECTED>, Throwing {
  private final Fetch<ELEMENT> fetch;
  private final TryIntConsumer2<? super ELEMENT, ? super COLLECTED> operation;
  private COLLECTED collected;
  private boolean hasCollected = true;

  public Collect(Fetch<ELEMENT> fetch, COLLECTED initial, TryIntConsumer2<? super ELEMENT, ? super COLLECTED> operation) {
    this.fetch = fetch;
    this.operation = operation;
    this.collected = initial;
  }

  @Override
  public boolean hasElement() throws Throwable {
    if (hasCollected) {
      while (fetch.hasElement()) {
        collected = fetch.element((index, element) -> {
          operation.invoke(index, element, collected);
          return collected;
        });
      }
    }
    return hasCollected;
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super COLLECTED, ? extends NEXT> then) throws Throwable {
    try {
      return hasCollected || hasElement() ? then.invoke(0, collected) : throwing(() -> Fetch.Exception.of("fold", "foldable"));
    } finally {
      hasCollected = false;
    }
  }
}
