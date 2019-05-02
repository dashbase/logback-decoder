package ch.qos.logback.decoder;

import ch.qos.logback.core.pattern.parser2.PatternInfo;

public class MDCValueParser implements FieldCapturer<StaticLoggingEvent> {
    private final String key;

    public MDCValueParser(String key) {
        this.key = key;
    }

    @Override
    public void captureField(StaticLoggingEvent event, String field, Offset offset, PatternInfo info) {
        if (!field.isEmpty()) {
            event.putMDC(key, field, offset);
        }
    }
}
