package oak.sys;

import static java.lang.System.out;

public interface Shell {
  static void println(final Object any) {
    out.println(any);
  }

  static void print(final Object any) {
    out.print(any);
  }

  static void ln() {
    out.print('\n');
  }
}
