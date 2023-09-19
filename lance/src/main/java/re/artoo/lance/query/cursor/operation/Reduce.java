package re.artoo.lance.query.cursor.operation;

import com.java.lang.Exceptionable;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetchable;

public final class Reduce<ELEMENT> implements Cursor<ELEMENT>, Exceptionable {
  private final Fetchable<ELEMENT> fetchable;
  private final TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> operation;
  private ELEMENT element;
  private boolean hasElement;

  public Reduce(Fetchable<ELEMENT> fetchable, TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> operation) {
    this.fetchable = fetchable;
    this.operation = operation;
  }

  @Override
  public boolean canFetch() throws java.lang.Throwable {
    if (!hasElement && (hasElement = fetchable.canFetch())) {
      element = fetchable.fetch((__, element) -> element);
      while (fetchable.canFetch()) {
        element = fetchable.fetch((index, element) -> operation.invoke(index, this.element, element));
      }
    }
    return hasElement;
  }

  @Override
  public <NEXT> NEXT fetch(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws java.lang.Throwable {
    try {
      return hasElement || canFetch() ? then.invoke(0, element) : throwing(() -> Fetchable.Exception.of("reduce", "reducible"));
    } finally {
      hasElement = false;
    }
  }
}
