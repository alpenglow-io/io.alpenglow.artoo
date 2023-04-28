package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryFunction2;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public final class Rethrow<ELEMENT> extends Head<ELEMENT> implements Cursor<ELEMENT> {
  private final Fetch<ELEMENT> fetch;
  private final String message;
  private final TryFunction2<? super String, ? super Throwable, ? extends RuntimeException> fallback;

  public Rethrow(Fetch<ELEMENT> fetch, String message, TryFunction2<? super String, ? super Throwable, ? extends RuntimeException> fallback) {
    super("rethrow", "rethrowable");
    this.fetch = fetch;
    this.message = message;
    this.fallback = fallback;
  }

  public Rethrow(Fetch<ELEMENT> fetch) {
    this(fetch, null, (ignored, throwable) -> throwable instanceof RuntimeException it ? it : FetchException.with(throwable));
  }

  @Override
  public boolean hasElement() throws Throwable {
    return hasElement || (hasElement = fetch.hasElement());
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    try {
      return hasElement ? fetch.element(then) : FetchException.byThrowingCantFetchNextElement("rethrow", "rethrowable");
    } catch (Throwable throwable) {
      throw fallback.invoke(message, throwable);
    } finally {
      hasElement = false;
    }
  }
}
