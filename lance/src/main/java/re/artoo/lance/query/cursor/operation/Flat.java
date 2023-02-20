package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Probe;

public record Flat<ELEMENT>(Probe<Probe<ELEMENT>> probe, Ticked<Probe<ELEMENT>> ticked) implements Cursor<ELEMENT> {
  public Flat(Probe<Probe<ELEMENT>> probe) {
    this(probe, Ticked.empty());
  }

  @Override
  public boolean canFetch() throws Throwable {
    /*
     * this is a bit tricky to catch it at first glance:
     * at the very beginning, we need to check if the fetcher has a fetcher within,
     * then we take it by setting the current flatten fetcher,
     * otherwise we take the next one
     */
    boolean isTickable = (ticked.element != null && ticked.element.canFetch()) || probe.canFetch();

    if (isTickable && (ticked.element == null || !ticked.element.canFetch())) {
      ticked.element = probe.fetch();
    }
    return isTickable;
  }
  @Override
  public ELEMENT fetch() throws Throwable {
    /*
     * if we don't have a current flatten fetcher,
     * then we check if we have one within (see above) and if so, we fetch a value from it,
     * otherwise we just fetch a value from it
     */
    return switch (ticked.element) {
      case null -> canFetch() ? ticked.element.fetch() : null;
      default -> ticked.element.canFetch() || canFetch() ? ticked.element.fetch() : null;
    };
  }

}
