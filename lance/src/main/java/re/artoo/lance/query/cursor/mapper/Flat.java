package re.artoo.lance.query.cursor.mapper;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetcher;
import re.artoo.lance.query.cursor.routine.Routine;

@SuppressWarnings("SwitchStatementWithTooFewBranches")
public final class Flat<T> implements Cursor<T> {
  private final Fetcher<Fetcher<T>> source;
  private Fetcher<T> current;
  private int index = 0;

  public Flat(final Fetcher<Fetcher<T>> source) {
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
  public <R> R fetch(TryIntFunction1<? super T, ? extends R> detach) throws Throwable {
    /*
     * if we don't have a current flatten fetcher,
     * then we check if we have one within (see above) and if so, we fetch a value from it,
     * otherwise we just fetch a value from it
     */
    return detach.invoke(index++, switch (current) {
      case null -> hasNext() ? current.fetch() : null;
      default -> current.hasNext() || hasNext() ? current.fetch() : null;
    });
  }

  @Override
  public <R> R as(final Routine<T, R> routine) {
    return routine.onSource().apply(this);
  }
}
