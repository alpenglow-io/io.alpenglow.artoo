package re.artoo.lance.query.cursor.operation;

import com.java.lang.Raiseable;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TryIntFunction2;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;

public final class Reduce<ELEMENT> implements Cursor<ELEMENT>, Raiseable {
  private final Fetch<ELEMENT> fetch;
  private final TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> operation;
  private ELEMENT element;
  private boolean hasElement;

  public Reduce(Fetch<ELEMENT> fetch, TryIntFunction2<? super ELEMENT, ? super ELEMENT, ? extends ELEMENT> operation) {
    this.fetch = fetch;
    this.operation = operation;
  }

  @Override
  public boolean hasElement() throws Throwable {
    if (!hasElement && (hasElement = fetch.hasElement())) {
      element = fetch.element((__, element) -> element);
      while (fetch.hasElement()) {
        element = fetch.element((index, element) -> operation.invoke(index, this.element, element));
      }
    }
    return hasElement;
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    try {
      return hasElement || hasElement() ? then.invoke(0, element) : raise(() -> Fetch.Exception.of("reduce", "reducible"));
    } finally {
      hasElement = false;
    }
  }
}
