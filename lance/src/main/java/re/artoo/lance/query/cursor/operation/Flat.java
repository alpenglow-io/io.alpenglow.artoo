package re.artoo.lance.query.cursor.operation;

import com.java.lang.Exceptionable;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetchable;

public final class Flat<ELEMENT> implements Cursor<ELEMENT>, Exceptionable {
  private final Fetchable<Fetchable<ELEMENT>> fetchable;
  private Fetchable<ELEMENT> flatten;
  private int index;
  private ELEMENT element;
  private boolean hasElement;

  public Flat(Fetchable<Fetchable<ELEMENT>> fetchable) {
    this(fetchable, null);
  }

  private Flat(Fetchable<Fetchable<ELEMENT>> fetchable, Fetchable<ELEMENT> flatten) {
    this.fetchable = fetchable;
    this.flatten = flatten;
  }

  @Override
  public boolean canFetch() throws java.lang.Throwable {
    if (!hasElement) {
      do {
        if (flatten == null && fetchable.canFetch() || (flatten != null && !flatten.canFetch()))
          flatten = fetchable.canFetch() ? fetchable.fetch((__, element) -> element) : null;

        if (flatten != null && (hasElement = flatten.canFetch()))
          element = flatten.fetch((__, element) -> element);
      } while (!hasElement && fetchable.canFetch());
    }
    return hasElement;
  }

  @Override
  public <NEXT> NEXT fetch(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws java.lang.Throwable {
    try {
      return hasElement || canFetch() ? then.invoke(index, element) : throwing(() -> Fetchable.Exception.of("flat", "flattable"));
    } finally {
      index++;
      hasElement = false;
    }
  }
}
