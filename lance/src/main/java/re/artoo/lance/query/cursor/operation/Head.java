package re.artoo.lance.query.cursor.operation;

import com.java.lang.Exceptionable;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public final class Head<ELEMENT> implements Cursor<ELEMENT>, Exceptionable {
  private final Fetch<ELEMENT> fetch;
  private int index;
  private ELEMENT element;
  private boolean hasElement;

  public Head(Fetch<ELEMENT> fetch) {
    this(fetch, 0, null, false);
  }
  private Head(Fetch<ELEMENT> fetch, int index, ELEMENT element, boolean hasElement) {
    this.fetch = fetch;
    this.index = index;
    this.element = element;
    this.hasElement = hasElement;
  }

  @Override
  public boolean hasElement() throws Throwable {
    if (!hasElement && (hasElement = fetch.hasElement())) {
      element = fetch.element((index, element) -> {
        this.index = index;
        return element;
      });
    }
    return hasElement;
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    try {
      return hasElement || hasElement() ? then.invoke(index, element) : raise(() -> FetchException.of("head", "head"));
    } finally {
      hasElement = false;
    }
  }
}
