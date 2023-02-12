package re.artoo.lance.query.cursor.mapper;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Probe;

public record Flat<ELEMENT>(Probe<Probe<ELEMENT>> probe, Ticked<Probe<ELEMENT>> ticked) implements Cursor<ELEMENT> {
  public Flat(Probe<Probe<ELEMENT>> probe) {
    this(probe, Ticked.empty());
  }

  @Override
  public boolean isTickable() throws Throwable {
    /*
     * this is a bit tricky to catch it at first glance:
     * at the very beginning, we need to check if the fetcher has a fetcher within,
     * then we take it by setting the current flatten fetcher,
     * otherwise we take the next one
     */
    boolean isTickable = (ticked.element != null && ticked.element.isTickable()) || probe.isTickable();

    if (isTickable && (ticked.element == null || !ticked.element.isTickable())) {
      ticked.element = probe.tick();
    }
    return isTickable;
  }
  @Override
  public ELEMENT tick() throws Throwable {
    /*
     * if we don't have a current flatten fetcher,
     * then we check if we have one within (see above) and if so, we fetch a value from it,
     * otherwise we just fetch a value from it
     */
    return switch (ticked.element) {
      case null -> isTickable() ? ticked.element.tick() : null;
      default -> ticked.element.isTickable() || isTickable() ? ticked.element.tick() : null;
    };
  }

}
