package re.artoo.lance.query.cursor.routine.concat;

import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Fetcher;

import java.util.Iterator;

public final class Liter<T> implements Concat<T> {
  private final Fetcher<T> next;

  Liter(final Fetcher<T> fetcher) {this.next = fetcher;}

  @Override
  public final TryFunction1<T[], Cursor<T>> onArray() {
    return prev -> Cursor.chain(Cursor.open(prev), next);
  }

  @Override
  public TryFunction1<Fetcher<T>, Cursor<T>> onSource() {
    return prev -> Cursor.chain(prev, next);
  }

  @Override
  public TryFunction1<Iterator<T>, Cursor<T>> onIterator() {
    return prev -> Cursor.chain(Cursor.iteration(prev), next);
  }
}
