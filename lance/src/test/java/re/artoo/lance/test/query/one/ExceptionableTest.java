package re.artoo.lance.test.query.one;

import com.java.lang.Raiseable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.One;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionableTest implements Raiseable {
  @Test
  @DisplayName("should catch an exception on querying single element")
  void shouldCatchException() {
    for (var element : assertDoesNotThrow(() -> One.of("element")
      .select(it -> it.length() > 0 ? raise(IllegalArgumentException::new) : it)
      .exception(Throwable::printStackTrace))) {

      System.out.println(element);
    }
  }

  @Test
  @DisplayName("should rethrow an exception on querying single element")
  void shouldRethrowAnException() {
    assertThrows(IllegalArgumentException.class, () -> {
      for (
        var element
        : One.of("element").select(it -> it.length() > 0 ? raise(IllegalStateException::new) : it).rethrow(IllegalArgumentException::new)
      ) {
        System.out.println(element);
      }
    });
  }
}
