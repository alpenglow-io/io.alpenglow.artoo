package io.alpenglow.artoo.lance.query.cursor.projector;

import io.alpenglow.artoo.lance.query.Cursor;
import io.alpenglow.artoo.lance.query.Unit;
import io.alpenglow.artoo.lance.query.cursor.Fetcher;
import io.alpenglow.artoo.lance.query.cursor.routine.Routine;

@SuppressWarnings("SwitchStatementWithTooFewBranches")
public final class Flat<SOURCE> implements Cursor<SOURCE> {
  private final Fetcher<Fetcher<SOURCE>> source;
  private Fetcher<SOURCE> current;

  public Flat(final Fetcher<Fetcher<SOURCE>> source) {
    this.source = source;
  }

  @Override
  public boolean hasNext() {
    /*
     * this is a bit tricky to catch it at first glance:
     * at the very beginning, we need to check if the fetcher has a fetcher within,
     * then we take it by setting the current flatten fetcher,
     * otherwise we take the next one
     */
    boolean hasNext = (current != null && current.hasNext()) || source.hasNext();

    if (hasNext && (current == null || !current.hasNext())) {
      current = source.next();
    }
    return hasNext;

  }

  @Override
  public Unit<SOURCE> fetch() throws Throwable {
    /*
     * if we don't have a current flatten fetcher,
     * then we check if we have one within (see above) and if so, we fetch a value from it,
     * otherwise we just fetch a value from it
     */
    return switch (current) {
      case null -> hasNext() ? current.fetch() : Unit.nothing();
      default -> current.hasNext() || hasNext() ? current.fetch() : Unit.nothing();
    };
  }

  @Override
  public <R> R as(final Routine<SOURCE, R> routine) {
    return routine.onSource().apply(this);
  }
}
