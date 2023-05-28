package re.artoo.lance.query.cursor.operation;

import com.java.lang.Raiseable;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;

public final class Recover<ELEMENT> implements Cursor<ELEMENT>, Raiseable {
  private final Fetch<ELEMENT> fetch;
  private final TryIntFunction1<? super Throwable, ? extends ELEMENT> fallback;
  private int index;
  private ELEMENT element;
  private boolean hasElement;

  public Recover(Fetch<ELEMENT> fetch, TryIntFunction1<? super Throwable, ? extends ELEMENT> fallback) {
    this.fetch = fetch;
    this.fallback = fallback;
  }

  @Override
  public boolean hasElement() throws Throwable {
    if (!hasElement) {
      try {
        hasElement = fetch.hasElement();
        if (hasElement) element = fetch.element((index, element) -> {
          this.index = index;
          return element;
        });
      } catch (Throwable throwable) {
        element = fallback.invoke(index, throwable);
      }
    }
    return hasElement;
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    try {
      return hasElement || hasElement() ? then.invoke(index, element) : raise(() -> Fetch.Exception.of("recover", "recoverable"));
    } finally {
      index++;
      hasElement = false;
    }
  }
}
