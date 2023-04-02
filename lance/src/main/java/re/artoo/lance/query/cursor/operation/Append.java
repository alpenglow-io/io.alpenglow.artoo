package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Next;
import re.artoo.lance.query.cursor.Probe;

public record Append<ELEMENT>(Probe<ELEMENT> head, Probe<ELEMENT> tail) implements Cursor<ELEMENT> {
  @Override
  public boolean hasNext() {
    return head.hasNext() || tail.hasNext();
  }

  @Override
  public Next<ELEMENT> next() {
    return head.hasNext() ? head.next() : tail.hasNext() ? tail.next() : Next.failure("append", "appendable");
  }
}
