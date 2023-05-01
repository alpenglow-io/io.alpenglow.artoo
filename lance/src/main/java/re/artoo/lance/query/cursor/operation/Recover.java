package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;

public final class Recover<ELEMENT> extends Head<ELEMENT> implements Cursor<ELEMENT> {
  private final Fetch<ELEMENT> fetch;
  private final TryFunction1<? super Throwable, ? extends ELEMENT> fallback;

  public Recover(Fetch<ELEMENT> fetch, TryFunction1<? super Throwable, ? extends ELEMENT> fallback) {
    super("recover", "recoverable");
    this.fetch = fetch;
    this.fallback = fallback;
  }

  @Override
  public boolean hasElement() throws Throwable {
    try {
      if (!hasElement && (hasElement = fetch.hasElement())) {
        fetch.element(this::set);
      }
    } catch (Throwable throwable) {
      element = fallback.invoke(throwable);
      hasElement = true;
    }
    return hasElement;
  }
}
