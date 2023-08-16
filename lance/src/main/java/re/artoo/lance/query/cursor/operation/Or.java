package re.artoo.lance.query.cursor.operation;

import com.java.lang.Throwing;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.func.TrySupplier1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;
import re.artoo.lance.value.Lazy;

public final class Or<ELEMENT> implements Cursor<ELEMENT>, Throwing {
  private final Fetch<ELEMENT> fetch;
  private final Lazy<Fetch<ELEMENT>> defaults;
  private boolean forked;

  public Or(Fetch<ELEMENT> fetch, TrySupplier1<? extends Fetch<ELEMENT>> vice) {
    this(fetch, Lazy.lazy(vice));
  }
  private Or(Fetch<ELEMENT> fetch, Lazy<Fetch<ELEMENT>> defaults) {
    this.fetch = fetch;
    this.defaults = defaults;
  }

  @Override
  public boolean hasElement() throws Throwable {
    try {
      return fetch.hasElement() && !forked || (defaults.value() != null && defaults.value().hasElement());
    } finally {
      if (!fetch.hasElement()) forked = true;
    }
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    return hasElement() && fetch.hasElement() && !forked
      ? fetch.element(then)
      : defaults.value() != null && defaults.value().hasElement()
      ? defaults.value().element(then)
      : throwing(() -> Fetch.Exception.of("or", "elseable"));
  }
}
