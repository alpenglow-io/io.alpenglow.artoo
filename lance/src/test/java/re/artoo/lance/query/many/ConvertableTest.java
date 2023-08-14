package re.artoo.lance.query.many;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Many;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ConvertableTest {
  @Test
  @DisplayName("should convert a many to a list")
  public void shouldConvertAsList() {
    final var list = Many.from(1, 2, 3, 4).asList();

    assertThat(list).containsExactly(1, 2, 3, 4);
  }

  @Test
  @DisplayName("should convert a many from an array to an array")
  public void shouldConvertAnArrayManyAsArray() {
    final var array = Many.from(1, 2, 3, 4).asArray(Integer[]::new);

    assertThat(array).containsExactly(1, 2, 3, 4);
  }

  @Test
  @Disabled("Many.from iterable must be reimplemented")
  @DisplayName("should convert a many from an iterable to an array")
  public void shouldConvertAnIterableManyAsArray() {
    record Dog(String name) {
    }
    final var dogs = new Dog[]{
      new Dog("Fuffy"), new Dog("Argos"), new Dog("Cerberos"), new Dog("Zeus")
    };

    final var array = Many.from(List.of(dogs)).asArray(Dog[]::new);

    assertThat(array).containsExactly(dogs);
  }
}
