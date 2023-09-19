package re.artoo.lance.query.cursor.operation;

import com.java.lang.Exceptionable;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetchable;

public final class Head<ELEMENT> implements Cursor<ELEMENT>, Exceptionable {
  private final Fetchable<ELEMENT> fetchable;
  private int index;
  private ELEMENT element;
  private boolean hasElement;

  public Head(Fetchable<ELEMENT> fetchable) {
    this(fetchable, 0, null, false);
  }

  private Head(Fetchable<ELEMENT> fetchable, int index, ELEMENT element, boolean hasElement) {
    this.fetchable = fetchable;
    this.index = index;
    this.element = element;
    this.hasElement = hasElement;
  }

  @Override
  public boolean canFetch() throws Throwable {
    if (!hasElement && (hasElement = fetchable.canFetch())) {
      element = fetchable.fetch((index, element) -> {
        this.index = index;
        return element;
      });
    }
    return hasElement;
  }

  @Override
  public <NEXT> NEXT fetch(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    try {
      return hasElement || canFetch() ? then.invoke(index, element) : checked.throwing(() -> Fetchable.Exception.of("head", "head"));
    } finally {
      hasElement = false;
    }
  }
}
