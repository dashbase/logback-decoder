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

import ch.qos.logback.classic.Level;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;



/**
 * Tests decoding %message
 *
 * @author Anthony Trinh
 */
public class MessageDecoderTest extends DecoderTest {
  @Test
  public void decodesNumericMessage() {
    assertEquals("123", getMessage("123"));
  }

  @Test
  public void decodesSimpleMessage() {
    assertEquals("This is just a test", getMessage("This is just a test"));
  }

  @Test
  public void testMultiline() {
    assertEquals("This\nis\ntest.", getMessage("This\nis\ntest."));
  }

  @Test
  public void testMDCProperties() {
    String input = "20:44:20.120 [JGroups-Executor-17] INFO c._.u.s.xmpp.server.XMPPConnection SID:123abc CID:456xyz - START handlePresence(PbxUserPresence{extension='1234', message='', status=3, pbxId='customerABC', pnRegistered=false, timestamp=0})\n";
    decoder.setLayoutPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{35} SID:%X{SID} CID:%X{CID} - %msg%n");
    StaticLoggingEvent event = (StaticLoggingEvent)decoder.decode(input);
    assertEquals("JGroups-Executor-17", event.getThreadName());
    assertEquals(Level.INFO, event.getLevel());
    assertEquals("c._.u.s.xmpp.server.XMPPConnection", event.getLoggerName());
    assertEquals("START handlePresence(PbxUserPresence{extension='1234', message='', status=3, pbxId='customerABC', pnRegistered=false, timestamp=0})", event.getMessage());
    assertEquals("123abc", event.getMDCPropertyMap().get("SID"));
    assertEquals("456xyz", event.getMDCPropertyMap().get("CID"));
  }

  private String getMessage(String message) {
    final String INPUT = "2013-06-12 15:27:15.044 INFO: " + message + "\n";
    final String PATT = "%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level: %msg%n";
    decoder.setLayoutPattern(PATT);
    StaticLoggingEvent event = (StaticLoggingEvent)decoder.decode(INPUT);
    assertNotNull(event);
    return event.getMessage();
  }
}
