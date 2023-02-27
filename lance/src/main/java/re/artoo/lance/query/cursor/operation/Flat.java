package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Probe;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public record Flat<ELEMENT>(Probe<Probe<ELEMENT>> probe, AtomicReference<Probe<ELEMENT>> reference, AtomicBoolean fetched) implements Cursor<ELEMENT> {
  public Flat(Probe<Probe<ELEMENT>> probe) {
    this(probe, new AtomicReference<>(), new AtomicBoolean(false));
  }

  @Override
  public boolean canFetch() throws Throwable {
    if (!fetched.get()) return true;
    if ()
    /*
     * this is a bit tricky to catch it at first glance:
     * at the very beginning, we need to check if the fetcher has a fetcher within,
     * then we take it by setting the current flatten fetcher,
     * otherwise we take the next one
     */
    boolean isTickable = (reference.get() != null && reference.get().canFetch()) || probe.canFetch();

    if (isTickable && (reference.get() == null || !reference.get().canFetch())) {
      reference.set(probe.fetch());
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
    return switch (reference.get()) {
      case null -> canFetch() ? reference.get().fetch() : null;
      default -> reference.get().canFetch() || canFetch() ? reference.get().fetch() : null;
    };
  }

}
