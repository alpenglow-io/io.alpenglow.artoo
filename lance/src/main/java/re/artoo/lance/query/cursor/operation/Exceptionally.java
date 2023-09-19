package re.artoo.lance.query.cursor.operation;

import com.java.lang.Exceptionable;
import re.artoo.lance.func.TryIntConsumer1;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetchable;

public final class Exceptionally<ELEMENT> implements Cursor<ELEMENT>, Exceptionable {
  private final Fetchable<ELEMENT> fetchable;
  private final TryIntConsumer1<? super Throwable> catchable;
  private int index;
  private ELEMENT element;
  private boolean hasElement;

  public Exceptionally(Fetchable<ELEMENT> fetchable, TryIntConsumer1<? super Throwable> catchable) {
    this.fetchable = fetchable;
    this.catchable = catchable;
  }

  @Override
  public boolean canFetch() throws Throwable {
    if (!hasElement) {
      var caught = true;
      while (caught) {
        try {
          hasElement = fetchable.canFetch();
          if (hasElement) element = fetchable.fetch((index, element) -> {
            this.index = index;
            return element;
          });
          caught = false;
        } catch (Throwable throwable) {
          catchable.invoke(index, throwable);
        }
      }
    }
    return hasElement;
  }

  @Override
  public <NEXT> NEXT fetch(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    try {
      return hasElement || canFetch() ? then.invoke(index, element) : checked.throwing(() -> Fetchable.Exception.of("catch", "catchable"));
    } finally {
      index++;
      hasElement = false;
    }
  }
}
