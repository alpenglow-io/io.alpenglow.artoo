package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;

import java.util.List;

public final class Iterable<T> implements Cursor<T> {
  private final List<T> list;
  private int index = 0;

  public Iterable(final List<T> list) {this.list = list;}
  @Override
  public T fetch() throws Throwable {
    return list.get(index);
  }

  @Override
  public boolean canFetch() {
    return index < list.size();
  }

}
