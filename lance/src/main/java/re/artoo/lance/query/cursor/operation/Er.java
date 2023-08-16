package re.artoo.lance.query.cursor.operation;

import com.java.lang.Throwing;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;

public final class Er<ELEMENT> implements Cursor<ELEMENT>, Throwing {
  private final Fetch<ELEMENT> fetch;
  private final String message;
  private final TryFunction1<? super String, ? extends Throwable> exception;
  private int index;
  private boolean thrown;

  public Er(Fetch<ELEMENT> fetch, String message, TryFunction1<? super String, ? extends Throwable> exception) {
    this.fetch = fetch;
    this.message = message;
    this.exception = exception;
  }

  @Override
  public boolean hasElement() throws Throwable {
    try {
      return fetch.hasElement() || (!thrown && this.<Boolean>throwing(() -> exception.invoke(message)));
    } catch (Throwable throwable) {
      thrown = true;
      throw throwable;
    }
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    try {
      return hasElement() ? fetch.element((__, element) -> then.invoke(index, element)) : throwing(() -> exception.invoke(message));
    } catch (Throwable throwable) {
      thrown = true;
      throw throwable;
    } finally {
      index++;
    }
  }
}
