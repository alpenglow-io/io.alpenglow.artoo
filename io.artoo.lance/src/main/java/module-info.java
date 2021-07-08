module io.artoo.lance {
  requires static org.jetbrains.annotations;
  requires static java.logging;

  exports io.artoo.lance.func;
  exports io.artoo.lance.literator;
  exports io.artoo.lance.literator.cursor;
  exports io.artoo.lance.literator.cursor.routine;
  exports io.artoo.lance.scope;
  exports io.artoo.lance.query;
  exports io.artoo.lance.query.many;
  exports io.artoo.lance.query.one;
  exports io.artoo.lance.tuple;
  exports io.artoo.lance.value;
  exports io.artoo.lance.task;
}
