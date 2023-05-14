package re.artoo.lance.query.cursor.operation;

import com.java.lang.Raiseable;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public final class Rethrow<ELEMENT> implements Cursor<ELEMENT>, Raiseable {
  private final Fetch<ELEMENT> fetch;
  private final TryIntFunction1<? super Throwable, ? extends Throwable> feedback;
  private int index;
  private ELEMENT element;
  private Throwable throwable;
  private boolean hasElement;

  public Rethrow(Fetch<ELEMENT> fetch, TryIntFunction1<? super Throwable, ? extends Throwable> feedback) {
    this.fetch = fetch;
    this.feedback = feedback;
  }

  @Override
  public boolean hasElement() throws Throwable {
    if (!hasElement && (hasElement = fetch.hasElement())) {
      try {
        element = fetch.element((index, element) -> {
          this.index = index;
          return element;
        });
      } catch (Throwable throwable) {
        this.throwable = feedback.invoke(index, throwable);
      }
    }
    return hasElement;
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    try {
      if (hasElement || hasElement()) {
        if (throwable == null) return then.invoke(index, element);
        throw throwable;
      }
      return FetchException.of("rethrow", "rethrowable");
    } finally {
      index++;
      hasElement = false;
    }
  }
}
