package re.artoo.lance.query.one;

import com.java.lang.Throwing;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.func.TryPredicate1;
import re.artoo.lance.query.One;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static re.artoo.lance.func.TryPredicate1.*;

class ExceptionableTest implements Throwing {
  @Test
  @DisplayName("should catch an exception on querying single element")
  void shouldCatchException() {
    for (var element : assertDoesNotThrow(() -> One.of("element")
      .raise(IllegalArgumentException::new).when(it -> !it.isEmpty())
      .trap(Throwable::printStackTrace))) {

      System.out.println(element);
    }
  }

  @Test
  @DisplayName("should rethrow an exception on querying single element")
  void shouldRethrowAnException() {
    assertThrows(IllegalArgumentException.class, () -> {
      for (var element : One.of("element").raise(IllegalArgumentException::new).when(not(String::isEmpty))) {
        System.out.println(element);
      }
    });
  }

  @Test
  @DisplayName("should recover after an exception on querying single element")
  void shouldRecoverAfterAnException() {
    assertDoesNotThrow(() -> {
      for (
        var element
        : One.of("element").select(it -> !it.isEmpty() ? throwing(() -> new IllegalStateException("illegal state")) : it).recover(Throwable::getMessage)
      ) {
        assertThat(element).isEqualTo("illegal state");
      }
    });
  }
}
