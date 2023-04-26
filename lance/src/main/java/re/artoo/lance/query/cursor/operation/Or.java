package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;

import java.util.concurrent.atomic.AtomicReference;

public final class Or<ELEMENT> extends Head<ELEMENT> implements Cursor<ELEMENT> {
  private final Fetch<ELEMENT> fetch;
  private final AtomicReference<Fetch<ELEMENT>> otherwise;

  public Or(Fetch<ELEMENT> fetch, Fetch<ELEMENT> otherwise) {
    this(fetch, new AtomicReference<>(otherwise));
  }
  private Or(Fetch<ELEMENT> fetch, AtomicReference<Fetch<ELEMENT>> otherwise) {
    super("or", "elseable");
    this.fetch = fetch;
    this.otherwise = otherwise;
  }

  @Override
  public boolean hasElement() throws Throwable {
    if (!hasElement) {
      if (fetch.hasElement()) {
        otherwise.set(null);
        hasElement = true;
        fetch.element(this::set);
      } else if (otherwise.get() != null && otherwise.get().hasElement()) {
        hasElement = true;
        otherwise.get().element(this::set);
      } else {
        hasElement = false;
      }
    }
    return hasElement;
  }
}
