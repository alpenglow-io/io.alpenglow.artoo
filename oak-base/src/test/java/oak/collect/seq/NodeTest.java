package oak.collect.seq;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class EmptyNodeTest {
  @Test
  void shouldBeEqual() {
    assertThat(new EmptyNode<Integer>()).isEqualTo(new EmptyNode<String>());
  }

  @Test
  void shouldAddNode() {
    final var node = new EmptyNode<Integer>();

    assertThat(node.next(0)).isNotNull();
  }

  @Test
  void shouldNotIterate() {
    final var node = new EmptyNode<Integer>();

    for (var value : node) fail();
  }
}

class SingleNodeTest {
  @Test
  void shouldIterate() {
    final var single = new SingleNode<>(1);

    assertThat(single).containsOnly(1);
  }
}

class NextNodeTest {
  @Test
  void shouldIterate() {
    final var next = new NextNode<>(1, new SingleNode<>(2));

    assertThat(next).containsOnly(1, 2);
  }

  @Test
  void shouldIterateMore() {
    final var first = new NextNode<>(1, new NextNode<>(2, new SingleNode<>(3)));
    final var last = new SingleNode<>(3);

    assertThat(first).containsOnly(1, 2, 3);
  }
}
