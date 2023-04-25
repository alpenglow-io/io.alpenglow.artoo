package re.artoo.lance.test.query.cursor.operation;

import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.operation.Open;
import re.artoo.lance.query.cursor.operation.Or;
import re.artoo.lance.query.cursor.operation.Peek;

class OrTest {
  @Test
  void shouldOr() throws Throwable {
    var cursor =
      new Peek<>(
        new Or<>(
          Cursor.empty(),
          new Open<>(1, 2, 3)
        ),
        (index, element) -> System.out.println(element)
      );

    while (cursor.hasElement()) {
      System.out.println(cursor.next());
    }
  }
}
