package dev.lug.oak.tail;

import dev.lug.oak.collect.cursor.Cursor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

import static java.util.stream.IntStream.range;

public interface Recursive<T> extends Iterable<T> {
  @NotNull
  @Override
  default Iterator<T> iterator() {
    // magic happens here
    return Cursor.none();
  }

  @SuppressWarnings("SwitchStatementWithTooFewBranches")
  static Integer strange(final int... numbers) {
    return switch (numbers.length) {
      case 1 -> numbers[0] + 1;
      default -> strange(Arrays.copyOf(numbers, numbers.length - 1));
    };
  }

  static void main(String... args) {
    System.out.println(strange(range(0, 8000).toArray()));
  }
}
