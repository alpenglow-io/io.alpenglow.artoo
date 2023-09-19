package re.artoo.lance.query.cursor.operation;

import com.java.lang.Exceptionable;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetchable;

public final class Map<ELEMENT, RETURN> implements Cursor<RETURN>, Exceptionable {
  private final Fetchable<ELEMENT> fetchable;
  private final TryIntFunction1<? super ELEMENT, ? extends RETURN> operation;

  public Map(Fetchable<ELEMENT> fetchable, TryIntFunction1<? super ELEMENT, ? extends RETURN> operation) {
    this.fetchable = fetchable;
    this.operation = operation;
  }

  @Override
  public boolean canFetch() throws Throwable {
    return fetchable.canFetch();
  }

  @Override
  public <NEXT> NEXT fetch(TryIntFunction1<? super RETURN, ? extends NEXT> then) throws Throwable {
    return canFetch()
      ? fetchable.fetch((index, element) -> then.invoke(index, operation.invoke(index, element)))
      : throwing(() -> Fetchable.Exception.of("map", "mappable"));
  }
}
