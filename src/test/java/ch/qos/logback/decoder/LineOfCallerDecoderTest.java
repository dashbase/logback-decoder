/**
 * Copyright (C) 2013, QOS.ch. All rights reserved.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Tests decoding %line
 *
 * @author Anthony Trinh
 */
public class LineOfCallerDecoderTest {

  @Test
  public void decodesLineNumber() {
    var event = parse("123");
    assertEquals(123, event.getLineNumberOfCaller());
    assertEquals(57, event.lineNumberOffset.start);
    assertEquals(60, event.lineNumberOffset.end);
  }

  // negative should never happen but Decoder should be able to parse it anyway
  @Test
  public void decodesNegativeLineNumber() {
    var event = parse("-2222");
    assertEquals(-2222, event.getLineNumberOfCaller());
  }

  // zero should never happen but Decoder should be able to parse it anyway
  @Test
  public void decodesZeroLineNumber() {
    var event = parse("0");
    assertEquals(0, event.getLineNumberOfCaller());
  }

  @Test
  public void noMatchWhenLineNumberMissing() {
    final String INPUT = "2013-06-12 15:27:15.044 INFO  [main] KdbFxFeedhandlerApp foobar123: Running com.ubs.sprint.kdb.fx.feedhandler.server.KdbFxFeedhandlerApp from directory: /sbclocal/sprint/kdb-fx-feedhandler/0.0.23/bin/.";
    final String PATT = "%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level [%thread] %logger{0} %line: %msg%n";
    Decoder decoder = new Decoder(PATT);
    StaticLoggingEvent event = (StaticLoggingEvent)decoder.decode(INPUT);
    assertNull(event);
  }

  private StaticLoggingEvent parse(String line) {
    final String INPUT = "2013-06-12 15:27:15.044 INFO  [main] KdbFxFeedhandlerApp " + line + ": Running com.ubs.sprint.kdb.fx.feedhandler.server.KdbFxFeedhandlerApp from directory: /sbclocal/sprint/kdb-fx-feedhandler/0.0.23/bin/.";
    final String PATT = "%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level [%thread] %logger{0} %line: %msg%n";
    Decoder decoder = new Decoder(PATT);
    StaticLoggingEvent event = (StaticLoggingEvent)decoder.decode(INPUT);
    assertNotNull(event);
    return event;
  }
}
