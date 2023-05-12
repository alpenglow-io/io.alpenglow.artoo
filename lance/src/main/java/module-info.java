module re.artoo.lance {
  requires static java.logging;

  requires jdk.incubator.concurrent;

  exports re.artoo.lance.func;
  exports re.artoo.lance.query.cursor;
  exports re.artoo.lance.query;
  exports re.artoo.lance.query.many;
  exports re.artoo.lance.query.one;
  exports re.artoo.lance.tuple;
  exports re.artoo.lance.value;
  exports re.artoo.lance.scope;
  exports re.artoo.lance;
  exports re.artoo.lance.tuple.record;
  exports re.artoo.lance.query.cursor.operation;

  exports com.java.lang to re.artoo.lance.test;
}
