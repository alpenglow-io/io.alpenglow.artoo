package re.artoo.lance.query.cursor.operation;

import com.java.lang.Raiseable;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

import java.util.concurrent.atomic.AtomicReference;

public final class Or<ELEMENT> implements Cursor<ELEMENT>, Raiseable {
  private final Fetch<ELEMENT> fetch;
  private final AtomicReference<Fetch<ELEMENT>> otherwise;

  public Or(Fetch<ELEMENT> fetch, Fetch<ELEMENT> otherwise) {
    this(fetch, new AtomicReference<>(otherwise));
  }

  private Or(Fetch<ELEMENT> fetch, AtomicReference<Fetch<ELEMENT>> otherwise) {
    this.fetch = fetch;
    this.otherwise = otherwise;
  }

  @Override
  public boolean hasElement() throws Throwable {
    try {
      return fetch.hasElement() || (otherwise.get() != null && otherwise.get().hasElement());
    } finally {
      if (fetch.hasElement()) otherwise.set(null);
    }
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    return hasElement() && fetch.hasElement()
      ? fetch.element(then)
      : otherwise.get() != null && otherwise.get().hasElement()
      ? otherwise.get().element(then)
      : raise(() -> FetchException.of("or", "elseable"));
  }
}
