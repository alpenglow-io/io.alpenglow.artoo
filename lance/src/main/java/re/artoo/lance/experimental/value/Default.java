package re.artoo.lance.experimental.value;

public enum Default {
  Nothing, Flushed;

  public boolean notEquals(Object value) {
    return !this.equals(value);
  }
}
