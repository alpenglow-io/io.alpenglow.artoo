package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetch;

public final class Append<ELEMENT> extends Head<ELEMENT> implements Cursor<ELEMENT> {
  private final Fetch<ELEMENT> head;
  private final Fetch<ELEMENT> tail;

  public Append(Fetch<ELEMENT> head, Fetch<ELEMENT> tail) {
    super("append", "appendable");
    this.head = head;
    this.tail = tail;
  }

  @Override
  public boolean hasElement() throws Throwable {
    if (!hasElement && (hasElement = head.hasElement()))
        head.element(this::set);
    else if (!hasElement && (hasElement = tail.hasElement()))
        tail.element(this::set);
    return hasElement;
  }
}
