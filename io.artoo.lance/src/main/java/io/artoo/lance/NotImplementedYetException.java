package io.artoo.lance;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

public final class NotImplementedYetException extends RuntimeException {
  public NotImplementedYetException(final String method) {
    super("""
      Method %s is not implemented yet.
      """
      .formatted(method)
    );
  }

  public static void main(String[] args) {
    DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d yyyy  HH:mm");

    LocalDateTime leaving = LocalDateTime.of(2021, Month.APRIL, 2, 11, 12);
    ZoneId leavingZone = ZoneId.of("UTC");
    ZonedDateTime departure = ZonedDateTime.of(leaving, leavingZone);

    String out1 = departure.format(format);
    System.out.printf("LEAVING:  %s (%s)%n", out1, leavingZone);

// Flight is 10 hours and 50 minutes, or 650 minutes
    ZoneId arrivingZone = ZoneId.of("Europe/Rome");
    final var minutes = arrivingZone.getRules().getOffset(departure.toInstant()).getTotalSeconds() / 60;
    ZonedDateTime arrival = departure.withZoneSameInstant(arrivingZone);

    String out2 = arrival.format(format);
    System.out.printf("ARRIVING: %s (%s)%n", out2, arrivingZone);

    if (arrivingZone.getRules().isDaylightSavings(arrival.toInstant()))
      System.out.printf("  (%s daylight saving time will be in effect.)%n",
        arrivingZone);
    else
      System.out.printf("  (%s standard time will be in effect.)%n",
        arrivingZone);
  }
}
