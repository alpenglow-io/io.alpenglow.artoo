package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

public sealed interface Appendable<ELEMENT> extends Probe<ELEMENT> permits Cursor, As {
  default Cursor<ELEMENT> append(Cursor<ELEMENT> cursor) {
    return new Append<>(this, cursor);
  }
}

final class Append<ELEMENT> implements Cursor<ELEMENT> {
  private final Probe<? extends ELEMENT> head;
  private final Probe<? extends ELEMENT> tail;
  private int index;

  Append(Probe<? extends ELEMENT> head, Probe<? extends ELEMENT> tail) {
    this(head, tail, 0);
  }
  private Append(Probe<? extends ELEMENT> head, Probe<? extends ELEMENT> tail, int index) {
    this.head = head;
    this.tail = tail;
    this.index = index;
  }

  @Override
  public <R> R tick(TryIntFunction1<? super ELEMENT, ? extends R> fetch) throws Throwable {
    return head.hasNext()
      ? head.tick((__, it) -> fetch.invoke(index++, it))
      : tail.tick((__, it) -> fetch.invoke(index++, it));
  }
  @Override
  public boolean hasNext() {
    return head.hasNext() || tail.hasNext();
  }
}

