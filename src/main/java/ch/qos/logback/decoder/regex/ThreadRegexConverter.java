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
package ch.qos.logback.decoder.regex;

import java.io.InputStream;

import ch.qos.logback.core.pattern.DynamicConverter;
import ch.qos.logback.decoder.PatternNames;

/**
 * Converts a thread-of-caller pattern into a regular expression
 */
public class ThreadRegexConverter extends DynamicConverter<InputStream> {
  
  public String convert(InputStream le) {
    return "(?<" + PatternNames.THREAD_NAME + ">" + RegexPatterns.THREAD_NAME_REGEX + ")";
  }
}
