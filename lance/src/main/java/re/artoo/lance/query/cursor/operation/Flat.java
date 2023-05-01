package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;

public final class Flat<ELEMENT> extends Head<ELEMENT> implements Cursor<ELEMENT> {
  private final Fetch<Fetch<ELEMENT>> fetch;
  private Fetch<ELEMENT> flatten;

  public Flat(Fetch<Fetch<ELEMENT>> fetch) {
    this(fetch, null);
  }
  private Flat(Fetch<Fetch<ELEMENT>> fetch, Fetch<ELEMENT> flatten) {
    super("flat", "flattable");
    this.fetch = fetch;
    this.flatten = flatten;
  }

  @Override
  public boolean hasElement() throws Throwable {
    if (!hasElement) {
      do {
        if (flatten == null && fetch.hasElement() || (flatten != null && !flatten.hasElement()))
          flatten = fetch.next();
        else
          index++;

        if (flatten != null && (hasElement = flatten.hasElement())) flatten.element((ith, element) -> set(index, element));
      } while (!hasElement && fetch.hasElement());
    }
    return hasElement;
  }
}
