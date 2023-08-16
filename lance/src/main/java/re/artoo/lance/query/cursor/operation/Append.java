package re.artoo.lance.query.cursor.operation;

import com.java.lang.Throwing;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;

public final class Append<ELEMENT> implements Cursor<ELEMENT>, Throwing {
  private final Fetch<ELEMENT> head;
  private final Fetch<ELEMENT> tail;
  private int index;

  public Append(Fetch<ELEMENT> head, Fetch<ELEMENT> tail) {
    this.head = head;
    this.tail = tail;
    this.index = 0;
  }

  @Override
  public boolean hasElement() throws Throwable {
    return head.hasElement() || tail.hasElement();
  }

  @Override
  public <NEXT> NEXT element(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    try {
      return head.hasElement()
        ? head.element(then)
        : tail.hasElement()
        ? tail.element((index, element) -> then.invoke(this.index, element))
        : throwing(() -> Fetch.Exception.of("append", "appendable"));
    } finally {
      index++;
    }
  }
}
