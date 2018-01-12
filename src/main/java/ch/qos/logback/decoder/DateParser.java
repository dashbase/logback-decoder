/**
 * Copyright (C) 2012, QOS.ch. All rights reserved.
 *
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *
 *   or (per the licensee's choosing)
 *
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */
package ch.qos.logback.decoder;

import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.pattern.parser2.DatePatternInfo;
import ch.qos.logback.core.pattern.parser2.PatternInfo;

/**
 * A {@code DateParser} parses a date field from a string and populates the
 * appropriate field in a given logging event
 */
public class DateParser implements FieldCapturer<IStaticLoggingEvent> {

  private Logger logger() {
    return LoggerFactory.getLogger(DateParser.class);
  }

  @Override
  public void captureField(IStaticLoggingEvent event, String fieldAsStr, PatternInfo info) {

    if (info instanceof DatePatternInfo) {
      DatePatternInfo dpi = (DatePatternInfo)info;
      try {
        DateTimeFormatter dtf = dpi.getDateFormat();

        // If the date pattern only contains time, use the today's year/month/day when parsing the input string.
        if (dtf != DatePatternInfo.ISO8601_FORMATTER
            && !dpi.getOption().toLowerCase().contains("d")) {
          ZoneId zoneId = dtf.getZone();
          LocalDate today = LocalDate.now(zoneId == null ? ZoneOffset.UTC: zoneId);
          dtf = new DateTimeFormatterBuilder().append(dtf)
              .parseDefaulting(ChronoField.YEAR, today.getYear())
              .parseDefaulting(ChronoField.MONTH_OF_YEAR, today.getMonthValue())
              .parseDefaulting(ChronoField.DAY_OF_MONTH, today.getDayOfMonth())
              .toFormatter();
          dtf = dtf.withZone(zoneId);
        }

        ZonedDateTime date = ZonedDateTime.parse(fieldAsStr, dtf);
        event.setTimeStamp(date.toInstant().toEpochMilli());
      } catch (DateTimeParseException e) {
        logger().error(e.toString());
      }
    } else {
      logger().debug("expected DatePatternInfo, actual {}", info.getClass().getName());
    }
  }
}
