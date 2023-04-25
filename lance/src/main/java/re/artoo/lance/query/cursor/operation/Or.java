package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

import java.util.concurrent.atomic.AtomicReference;

public record Or<ELEMENT>(Fetch<ELEMENT> fetch, AtomicReference<Fetch<ELEMENT>> otherwise, Next<ELEMENT> near) implements Cursor<ELEMENT> {
  public Or(Fetch<ELEMENT> fetch, Fetch<ELEMENT> otherwise) {
    this(fetch, new AtomicReference<>(otherwise), new Next<>());
  }
  @Override
  public boolean hasElement() throws Throwable {
    if (fetch.hasElement()) {
      otherwise.set(null);
      near.hasElement = true;
      fetch.element(near::set);
    } else if (otherwise.get() != null && otherwise.get().hasElement()) {
      near.hasElement = true;
      otherwise.get().element(near::set);
    } else {
      near.hasElement = false;
    }
    return near.hasElement;
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    return hasElement() ? near.let(then) : FetchException.byThrowingCantFetchNextElement("or", "elseable");
  }
}
