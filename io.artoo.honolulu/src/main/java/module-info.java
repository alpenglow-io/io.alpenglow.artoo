module io.artoo.honolulu {
  requires io.artoo.fxcalibur;
  requires org.slf4j;
  requires org.apache.logging.log4j;

  opens io.artoo.honolulu to javafx.graphics;
}
