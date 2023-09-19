package re.artoo.lance.query.cursor.operation;

import com.java.lang.Exceptionable;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetchable;

public final class Rethrow<ELEMENT> implements Cursor<ELEMENT>, Exceptionable {
  private final Fetchable<ELEMENT> fetchable;
  private final TryIntFunction1<? super java.lang.Throwable, ? extends java.lang.Throwable> feedback;
  private int index;
  private ELEMENT element;
  private java.lang.Throwable throwable;
  private boolean hasElement;

  public Rethrow(Fetchable<ELEMENT> fetchable, TryIntFunction1<? super java.lang.Throwable, ? extends java.lang.Throwable> feedback) {
    this.fetchable = fetchable;
    this.feedback = feedback;
  }

  @Override
  public boolean canFetch() throws java.lang.Throwable {
    if (!hasElement && (hasElement = fetchable.canFetch())) {
      try {
        element = fetchable.fetch((index, element) -> {
          this.index = index;
          return element;
        });
      } catch (java.lang.Throwable throwable) {
        this.throwable = feedback.invoke(index, throwable);
      }
    }
    return hasElement;
  }

  @Override
  public <NEXT> NEXT fetch(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws java.lang.Throwable {
    try {
      if (hasElement || canFetch()) {
        if (throwable == null) return then.invoke(index, element);
        throw throwable;
      }
      return Fetchable.Exception.of("rethrow", "rethrowable");
    } finally {
      index++;
      hasElement = false;
    }
  }
}
