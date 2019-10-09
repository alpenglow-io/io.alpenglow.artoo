package oak.func.con;

import java.io.IOException;
import java.nio.file.Files;

import static java.lang.String.format;
import static java.lang.String.join;
import static java.lang.String.valueOf;
import static java.nio.file.Files.readAllLines;
import static java.nio.file.Paths.get;
import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.range;

final class ConsumerGeneration {
  public static void main(String[] args) throws IOException {
    final var template = "/home/lug/prjs/private/oak/oak-base/src/main/java/oak/func/con/Consumer.template";
    final var code = join("\n", readAllLines(get(template)));
    for (var ordinal = 4; ordinal < 21; ordinal++) {
      final var types = range(1, ordinal + 1).mapToObj(type -> format("T%d", type)).collect(joining(", "));
      final var params = range(1, ordinal + 1).mapToObj(param -> format("T%d t%d", param, param)).collect(joining(", "));
      final var vars = range(1, ordinal + 1).mapToObj(variable -> format("t%d", variable)).collect(joining(", "));
      final var supers = range(1, ordinal + 1).mapToObj(supr -> format("? super T%d", supr)).collect(joining(", "));

      final var source = template.replaceAll("Consumer.template", format("Consumer%d.java", ordinal));
      Files.writeString(get(source), code
        .replaceAll("#ordinal", valueOf(ordinal))
        .replaceAll("#types", types)
        .replaceAll("#params", params)
        .replaceAll("#vars", vars)
        .replaceAll("#supers", supers));
    }
  }
}
