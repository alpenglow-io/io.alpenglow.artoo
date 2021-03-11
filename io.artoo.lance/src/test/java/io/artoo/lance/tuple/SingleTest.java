package io.artoo.lance.tuple;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SingleTest {
  record Sample(String text) implements Single<Sample, String> {
    @Override
    public Class<Sample> type$() {
      return Sample.class;
    }
  }

  @Test
  public void shouldMap() {
    final var sample = new Sample("text");

    assertThat(sample.map(it -> new Sample("some " + it)).text()).isEqualTo("some text");
    assertThat(sample.map(it -> new Sample("some " + it)).first()).isEqualTo("some text");
  }
}
