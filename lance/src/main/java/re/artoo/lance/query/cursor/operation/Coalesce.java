package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Probe;
import re.artoo.lance.query.cursor.Reference;

public record Coalesce<ELEMENT>(Probe<ELEMENT> probe, Reference<ELEMENT> reference) implements Cursor<ELEMENT> {
  public Coalesce(Probe<ELEMENT> probe) {
    this(probe, Ticked.empty());
  }

  @Override
  public boolean canFetch() throws Throwable {
    if (reference.retrieve() == null && probe.canFetch()) reference.element = probe.fetch();
    while (reference.element == null && probe.canFetch())
      reference.element = probe.fetch();
    return reference.element != null;
  }

  @Override
  public ELEMENT fetch() throws Throwable {
    try {
      return canFetch() ? reference.element : null;
    } finally {
      reference.element = null;
    }
  }
}
