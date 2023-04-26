package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.OperationException;
import re.artoo.lance.query.cursor.Fetch;

public final class Catch<ELEMENT> extends Head<ELEMENT> implements Cursor<ELEMENT> {
  private final Fetch<ELEMENT> fetch;
  private final TryConsumer1<? super Throwable> feedback;

  public Catch(Fetch<ELEMENT> fetch, TryConsumer1<? super Throwable> feedback) {
    super("catch", "catchable");
    this.fetch = fetch;
    this.feedback = feedback;
  }

  @Override
  public boolean hasElement() throws Throwable {
    if (!hasElement && (hasElement = fetch.hasElement())) {
      try {
        fetch.element(this::set);
      } catch (Throwable throwable) {
        this.throwable = throwable;
      }
    }
    return hasElement;
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> let) throws Throwable {
    try {
      if (hasElement && throwable == null) {
        return let.invoke(index, element);
      } else if (hasElement) {
        feedback.invoke(throwable);
        return null;
      }

      return OperationException.byThrowingCantFetchNextElement("catch", "catchable");
    } finally {
      hasElement = false;
    }
  }
}
