package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;

import java.util.List;

public final class Iteration<T> implements Cursor<T> {
  private final List<T> list;
  private int index = 0;

  public Iteration(final List<T> list) {this.list = list;}
  @Override
  public <R> R scroll(TryIntFunction1<? super T, ? extends R> fetch) throws Throwable {
    return fetch.invoke(index++, list.get(index));
  }

  @Override
  public boolean hasNext() {
    return index < list.size();
  }

}
