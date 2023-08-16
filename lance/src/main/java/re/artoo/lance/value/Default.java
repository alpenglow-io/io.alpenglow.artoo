package re.artoo.lance.value;

public enum Default {
  Nothing, Flushed;

  public boolean notEquals(Object value) {
    return !this.equals(value);
  }
}
