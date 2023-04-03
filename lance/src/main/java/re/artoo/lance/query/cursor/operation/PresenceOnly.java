package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;

public record PresenceOnly<ELEMENT>(Probe<ELEMENT> probe) implements Cursor<ELEMENT> {
  @Override
  public boolean hasNext() {
    return probe.hasNext();
  }

  @SuppressWarnings("UnnecessaryBreak")
  @Override
  public Next<ELEMENT> fetch() {
    again: if (hasNext()) {
      switch (probe.fetch()) {
        case Next<ELEMENT> it when it.element() != null:
          return it;
        default:
          break again;
      }
    }
    return FetchException.byThrowingCantFetchNextElement("presence-only", "present-only");
  }
}
