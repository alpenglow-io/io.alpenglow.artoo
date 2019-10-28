package dev.lug.oak.system;

import static java.lang.System.out;

public interface Console {
  static void writeLine(final Object any) {
    out.println(any);
  }

  static void write(final Object any) {
    out.print(any);
  }
}
