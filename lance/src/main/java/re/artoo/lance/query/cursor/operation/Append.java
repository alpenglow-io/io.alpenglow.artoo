package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Probe;

public record Append<ELEMENT>(Probe<? extends ELEMENT> head, Probe<? extends ELEMENT> tail) implements Cursor<ELEMENT> {
  @Override
  public ELEMENT fetch() throws Throwable {
    return head.canFetch()
      ? head.fetch()
      : tail.canFetch()
      ? tail.fetch()
      : null;
  }

  @Override
  public boolean canFetch() throws Throwable {
    return head.canFetch() || tail.canFetch();
  }
}
