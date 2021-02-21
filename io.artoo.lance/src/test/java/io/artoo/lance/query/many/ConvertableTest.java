package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ConvertableTest {
  @Test
  @DisplayName("should convert a many to a list")
  void shouldConvertAsList() {
    final var list = Many.from(1, 2, 3, 4).asList();

    assertThat(list).containsExactly(1, 2, 3, 4);
  }

  @Test
  @DisplayName("should convert a many from an array to an array")
  void shouldConvertAnArrayManyAsArray() {
    final var array = Many.from(1, 2, 3, 4).asArrayOf(Integer.class);

    assertThat(array).containsExactly(1, 2, 3, 4);
  }

  @Test
  @DisplayName("should convert a many from an iterable to an array")
  void shouldConvertAnIterableManyAsArray() {
    record Dog(String name) {}
    final var dogs = new Dog[] {
      new Dog("Fuffy"), new Dog("Argos"), new Dog("Cerberos"), new Dog("Zeus")
    };

    final var array = Many.from(List.of(dogs)).asArrayOf(Dog.class);

    assertThat(array).containsExactly(dogs);
  }
}
