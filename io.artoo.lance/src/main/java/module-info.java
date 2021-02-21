module io.artoo.lance {
  opens io.artoo.lance to org.junit.jupiter;

  requires static org.jetbrains.annotations;

  exports io.artoo.lance.func;
  exports io.artoo.lance.literator;
  exports io.artoo.lance.literator.cursor;
  exports io.artoo.lance.literator.cursor.routine;
  exports io.artoo.lance.scope;
  exports io.artoo.lance.query;
}
