package re.artoo.lance.query.cursor.operation;

import com.java.lang.Exceptionable;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetchable;

public final class Eventually<ELEMENT> implements Cursor<ELEMENT>, Exceptionable {
  private final Fetchable<ELEMENT> fetchable;
  private final TryIntFunction1<? super Throwable, ? extends ELEMENT> fallback;
  private int index;
  private ELEMENT element;
  private boolean hasElement;

  public Eventually(Fetchable<ELEMENT> fetchable, TryIntFunction1<? super Throwable, ? extends ELEMENT> fallback) {
    this.fetchable = fetchable;
    this.fallback = fallback;
  }

  @Override
  public boolean canFetch() throws Throwable {
    if (!hasElement) {
      try {
        hasElement = fetchable.canFetch();
        if (hasElement) element = fetchable.fetch((index, element) -> {
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
  public <NEXT> NEXT fetch(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    try {
      return hasElement || canFetch() ? then.invoke(index, element) : throwing(() -> Fetchable.Exception.of("recover", "recoverable"));
    } finally {
      index++;
      hasElement = false;
    }
  }
}
