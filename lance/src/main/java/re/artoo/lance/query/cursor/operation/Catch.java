package re.artoo.lance.query.cursor.operation;

import com.java.lang.Raiseable;
import re.artoo.lance.func.TryIntConsumer1;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;

public final class Catch<ELEMENT> implements Cursor<ELEMENT>, Raiseable {
  private final Fetch<ELEMENT> fetch;
  private final TryIntConsumer1<? super Throwable> feedback;
  private int index;
  private ELEMENT element;
  private boolean hasElement;

  public Catch(Fetch<ELEMENT> fetch, TryIntConsumer1<? super Throwable> feedback) {
    this.fetch = fetch;
    this.feedback = feedback;
  }

  @Override
  public boolean hasElement() throws Throwable {
    if (!hasElement) {
      var caught = true;
      while (caught) {
        try {
          hasElement = fetch.hasElement();
          if (hasElement) element = fetch.element((index, element) -> {
            this.index = index;
            return element;
          });
          caught = false;
        } catch (Throwable throwable) {
          feedback.invoke(index, throwable);
        }
      }
    }
    return hasElement;
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    try {
      return hasElement || hasElement() ? then.invoke(index, element) : raise(() -> Fetch.Exception.of("catch", "catchable"));
    } finally {
      index++;
      hasElement = false;
    }
  }
}
