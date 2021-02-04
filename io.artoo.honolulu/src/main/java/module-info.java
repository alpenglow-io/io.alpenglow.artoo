module io.artoo.honolulu {
  requires io.artoo.fxcalibur;

  requires ch.qos.logback.classic;
  requires org.slf4j;

  requires static javafx.graphicsEmpty;
  requires static javafx.controlsEmpty;

  opens io.artoo.honolulu to javafx.graphics;
}
