package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;

public final class Catch<ELEMENT> extends Current<ELEMENT> implements Cursor<ELEMENT> {
  private final Fetch<ELEMENT> fetch;
  private final TryConsumer1<? super Throwable> feedback;

  public Catch(Fetch<ELEMENT> fetch, TryConsumer1<? super Throwable> feedback) {
    super(fetch, "catch", "catchable");
    this.fetch = fetch;
    this.feedback = feedback;
  }

  @Override
  public boolean hasElement() throws Throwable {
    if (!hasElement) {
      var caught = true;
      while (caught && (hasElement = fetch.hasElement())) {
        try {
          fetch.element(this::set);
          caught = false;
        } catch (Throwable throwable) {
          feedback.invoke(throwable);
        }
      }
    }
    return hasElement;
  }
}
