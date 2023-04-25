package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public record Catch<ELEMENT>(Fetch<ELEMENT> fetch, TryConsumer1<? super Throwable> feedback, Next<ELEMENT> near) implements Cursor<ELEMENT> {
  public Catch(Fetch<ELEMENT> fetch, TryConsumer1<? super Throwable> feedback) {
    this(fetch, feedback, new Next<>());
  }

  @Override
  public boolean hasElement() throws Throwable {
    var caught = true;
    while (caught) {
      try {
        near.hasElement = fetch.hasElement();
        if (near.hasElement) {
          fetch.element(near::set);
        }
        caught = false;
      } catch (Throwable throwable) {
        feedback.invoke(throwable);
      }
    }
    return near.hasElement;
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    return near.hasElement ? near.let(then) : FetchException.byThrowingCantFetchNextElement("catch", "catchable");
  }
}
