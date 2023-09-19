package re.artoo.lance.query.cursor.operation;

import com.java.lang.Exceptionable;
import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetchable;

public final class Append<ELEMENT> implements Cursor<ELEMENT>, Exceptionable {
  private final Fetchable<ELEMENT> head;
  private final Fetchable<ELEMENT> tail;
  private int index;

  public Append(Fetchable<ELEMENT> head, Fetchable<ELEMENT> tail) {
    this.head = head;
    this.tail = tail;
    this.index = 0;
  }

  @Override
  public boolean canFetch() throws Throwable {
    return head.canFetch() || tail.canFetch();
  }

  @Override
  public <NEXT> NEXT fetch(TryIntFunction1<? super ELEMENT, ? extends NEXT> then) throws Throwable {
    try {
      return head.canFetch()
        ? head.fetch(then)
        : tail.canFetch()
        ? tail.fetch((index, element) -> then.invoke(this.index, element))
        : throwing(() -> Fetchable.Exception.of("append", "appendable"));
    } finally {
      index++;
    }
  }
}
