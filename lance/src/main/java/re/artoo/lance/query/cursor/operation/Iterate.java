package re.artoo.lance.query.cursor.operation;

import com.java.lang.Exceptionable;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetchable;

import java.util.Iterator;

public final class Iterate<ELEMENT> implements Cursor<ELEMENT>, Exceptionable {
  private final Iterator<ELEMENT> elements;
  private int index;

  public Iterate(Iterator<ELEMENT> elements) {
    this.elements = elements;
  }

  @Override
  public boolean canFetch() {
    return elements.hasNext();
  }

  @Override
  public <NEXT> NEXT fetch(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    try {
      return canFetch() ? then.invoke(index, elements.next()) : throwing(() -> Fetchable.Exception.of("iterate", "iterable"));
    } finally {
      index++;
    }
  }
}
