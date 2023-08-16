package re.artoo.lance.query.cursor.operation;

import com.java.lang.Throwing;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;

public final class Map<ELEMENT, RETURN> implements Cursor<RETURN>, Throwing {
  private final Fetch<ELEMENT> fetch;
  private final TryIntFunction1<? super ELEMENT, ? extends RETURN> operation;

  public Map(Fetch<ELEMENT> fetch, TryIntFunction1<? super ELEMENT, ? extends RETURN> operation) {
    this.fetch = fetch;
    this.operation = operation;
  }

  @Override
  public boolean hasElement() throws Throwable {
    return fetch.hasElement();
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super RETURN, ? extends NEXT> then) throws Throwable {
    return hasElement()
      ? fetch.element((index, element) -> then.invoke(index, operation.invoke(index, element)))
      : throwing(() -> Fetch.Exception.of("map", "mappable"));
  }
}
