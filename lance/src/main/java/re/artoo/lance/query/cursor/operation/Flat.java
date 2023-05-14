package re.artoo.lance.query.cursor.operation;

import com.java.lang.Raiseable;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public final class Flat<ELEMENT> implements Cursor<ELEMENT>, Raiseable {
  private final Fetch<Fetch<ELEMENT>> fetch;
  private Fetch<ELEMENT> flatten;
  private int index;
  private ELEMENT element;
  private boolean hasElement;

  public Flat(Fetch<Fetch<ELEMENT>> fetch) {
    this(fetch, null);
  }
  private Flat(Fetch<Fetch<ELEMENT>> fetch, Fetch<ELEMENT> flatten) {
    this.fetch = fetch;
    this.flatten = flatten;
  }

  @Override
  public boolean hasElement() throws Throwable {
    if (!hasElement) {
      do {
        if (flatten == null && fetch.hasElement() || (flatten != null && !flatten.hasElement()))
          flatten = fetch.hasElement() ? fetch.element((__, element) -> element) : null;

        if (flatten != null && (hasElement = flatten.hasElement()))
          element = flatten.element((__, element) -> element);
      } while (!hasElement && fetch.hasElement());
    }
    return hasElement;
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    try {
      return hasElement || hasElement() ? then.invoke(index, element) : raise(() -> FetchException.of("flat", "flattable"));
    } finally {
      index++;
      hasElement = false;
    }
  }
}
