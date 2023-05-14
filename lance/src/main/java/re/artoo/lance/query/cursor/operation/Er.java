package re.artoo.lance.query.cursor.operation;

import com.java.lang.Raiseable;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public final class Er<ELEMENT> implements Cursor<ELEMENT>, Raiseable {
  private final Fetch<ELEMENT> fetch;
  private final String message;
  private final TryFunction1<? super String, ? extends Throwable> pushback;
  private int index;

  public Er(Fetch<ELEMENT> fetch, String message, TryFunction1<? super String, ? extends Throwable> pushback) {
    this.fetch = fetch;
    this.message = message;
    this.pushback = pushback;
  }

  @Override
  public boolean hasElement() throws Throwable {
    return fetch.hasElement() || this.<Boolean>raise(() -> pushback.invoke(message));
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    try {
      return hasElement() ? fetch.element((__, element) -> then.invoke(index, element)) : raise(() -> FetchException.of("er", "errable"));
    } finally {
      index++;
    }
  }
}
