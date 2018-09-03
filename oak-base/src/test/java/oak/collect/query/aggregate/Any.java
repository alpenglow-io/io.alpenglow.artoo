package oak.collect.query.aggregate;

import java.util.Objects;

final class Any {
  private final int id;
  private final String hash;

  private Any(int id, String hash) {
    this.id = id;
    this.hash = hash;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Any any = (Any) o;
    return id == any.id &&
      Objects.equals(hash, any.hash);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, hash);
  }

  @Override
  public String toString() {
    return String.format("Any{id=%d, hash='%s'}", id, hash);
  }

  static Any any(final int id, final String hash) {
    return new Any(id, hash);
  }
}
