package re.artoo.lance.query.cursor.operation;

import com.java.lang.Exceptionable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetchable;

public final class Er<ELEMENT> implements Cursor<ELEMENT>, Exceptionable {
  private final Fetchable<ELEMENT> fetchable;
  private final String message;
  private final TryFunction1<? super String, ? extends java.lang.Throwable> exception;
  private int index;
  private boolean thrown;

  public Er(Fetchable<ELEMENT> fetchable, String message, TryFunction1<? super String, ? extends java.lang.Throwable> exception) {
    this.fetchable = fetchable;
    this.message = message;
    this.exception = exception;
  }

  @Override
  public boolean canFetch() throws java.lang.Throwable {
    try {
      return fetchable.canFetch() || (!thrown && this.<Boolean>throwing(() -> exception.invoke(message)));
    } catch (java.lang.Throwable throwable) {
      thrown = true;
      throw throwable;
    }
  }

  @Override
  public <NEXT> NEXT fetch(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws java.lang.Throwable {
    try {
      return canFetch() ? fetchable.fetch((__, element) -> then.invoke(index, element)) : throwing(() -> exception.invoke(message));
    } catch (java.lang.Throwable throwable) {
      thrown = true;
      throw throwable;
    } finally {
      index++;
    }
  }
}
