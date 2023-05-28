package re.artoo.lance.query.cursor.operation;

import com.java.lang.Raiseable;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;

import java.util.Iterator;

public final class Iterate<ELEMENT> implements Cursor<ELEMENT>, Raiseable {
  private final Iterator<ELEMENT> elements;
  private int index;

  public Iterate(Iterator<ELEMENT> elements) {
    this.elements = elements;
  }

  @Override
  public boolean hasElement() {
    return elements.hasNext();
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    try {
      return hasElement() ? then.invoke(index, elements.next()) : raise(() -> Fetch.Exception.of("iterate", "iterable"));
    } finally {
      index++;
    }
  }
}
