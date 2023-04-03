package re.artoo.lance.query.cursor.operation;

import re.artoo.lance.query.Cursor;

import java.util.List;

public record Iterable<ELEMENT>(List<ELEMENT> elements, Index index) implements Cursor<ELEMENT> {
  public Iterable(List<ELEMENT> elements) {
    this(elements, new Index());
  }

  @Override
  public boolean hasNext() {
    return index.value <= elements.size();
  }

  @Override
  public Next<ELEMENT> fetch() {
    return Next.of(index.value, elements.get(index.value++));
  }

  private static class Index {
    private int value = 0;
  }
}
