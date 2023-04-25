package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public record Flat<ELEMENT>(Fetch<Fetch<ELEMENT>> fetch, Flatten<ELEMENT> flatten) implements Cursor<ELEMENT> {
  public Flat(Fetch<Fetch<ELEMENT>> fetch) {
    this(fetch, new Flatten<>());
  }

  @Override
  public boolean hasElement() throws Throwable {
    flatten.fetch = flatten.fetch == null && fetch.hasElement() ? fetch.next() : flatten.fetch;
    while (flatten.fetch != null && !flatten.fetch.hasElement()) {
      flatten.fetch = fetch.hasElement() ? fetch.next() : null;
    }

    return flatten.fetch != null;
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    return hasElement() ? flatten.fetch.element(then) : FetchException.byThrowingCantFetchNextElement("flat-map", "flattable");
  }

  private static final class Flatten<NEXT> {
    private Fetch<NEXT> fetch;
  }
}
