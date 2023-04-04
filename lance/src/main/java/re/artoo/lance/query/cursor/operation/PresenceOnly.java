package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Fetch;

public record PresenceOnly<ELEMENT>(Fetch<ELEMENT> fetch) implements Cursor<ELEMENT> {
  @Override
  public boolean hasNext() {
    return fetch.hasNext();
  }

  @SuppressWarnings("UnnecessaryBreak")
  @Override
  public Next<ELEMENT> fetch() {
    again: if (hasNext()) {
      switch (fetch.next()) {
        case Next<ELEMENT> it when it.element() != null:
          return it;
        default:
          break again;
      }
    }
    return FetchException.byThrowingCantFetchNextElement("presence-only", "present-only");
  }
}
