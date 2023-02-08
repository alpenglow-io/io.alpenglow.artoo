package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;

public sealed interface Appendable<ELEMENT> extends Head<ELEMENT>, Tail<ELEMENT> permits Cursor, As {
  default Cursor<ELEMENT> append(Cursor<ELEMENT> cursor) {
    return new Append<>(this, cursor);
  }
}

final class Append<ELEMENT> implements Cursor<ELEMENT> {
  private final Head<? extends ELEMENT> head;
  private final Head<? extends ELEMENT> tail;
  private int index;

  Append(Head<? extends ELEMENT> head, Head<? extends ELEMENT> tail) {
    this(head, tail, 0);
  }
  private Append(Head<? extends ELEMENT> head, Head<? extends ELEMENT> tail, int index) {
    this.head = head;
    this.tail = tail;
    this.index = index;
  }

  @Override
  public <R> R scroll(TryIntFunction1<? super ELEMENT, ? extends R> fetch) throws Throwable {
    return head.hasNext()
      ? head.scroll((__, it) -> fetch.invoke(index++, it))
      : tail.scroll((__, it) -> fetch.invoke(index++, it));
  }
  @Override
  public boolean hasNext() {
    return head.hasNext() || tail.hasNext();
  }
}

