package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.cursor.Probe;

public record Flat<ELEMENT>(Probe<Probe<ELEMENT>> probe, Flatten<ELEMENT> flatten) implements Cursor<ELEMENT> {
  public Flat(Probe<Probe<ELEMENT>> probe) {
    this(probe, new Flatten<>());
  }

  @Override
  public boolean hasNext() {
    flatten.hasNext = probe.hasNext() || (flatten.probe != null && flatten.probe.hasNext());

    if (flatten.hasNext && !flatten.probe.hasNext()) {
      flatten.probe = probe.fetch().element();
    }

    return flatten.hasNext;
  }

  @Override
  public Next<ELEMENT> fetch() {
    return hasNext() ? flatten.probe.fetch() : FetchException.byThrowingCantFetchNextElement("flat-map", "flattable");
  }

  private static final class Flatten<NEXT> {
    private boolean hasNext = true;
    private Probe<NEXT> probe;
  }
}
