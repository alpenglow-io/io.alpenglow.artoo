package io.artoo.lance.test.tuple;

import io.artoo.lance.tuple.Single;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SingleTest {
  record Sample(String text) implements Single<String> {}

  @Test
  public void shouldMap() {
    final var sample = new Sample("text");

    assertThat(sample.map(it -> new Sample("some " + it)).text()).isEqualTo("some text");
    assertThat(sample.map(it -> new Sample("some " + it)).first()).isEqualTo("some text");
  }
}
