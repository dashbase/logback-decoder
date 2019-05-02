package ch.qos.logback.decoder;

import ch.qos.logback.core.pattern.parser2.PatternInfo;

public class MDCMapParser implements FieldCapturer<StaticLoggingEvent> {
    @Override
    public void captureField(StaticLoggingEvent event, String field, Offset offset, PatternInfo info) {
        // value is CSV. Convert it into Map.
        int startOffset = offset.start;

        int index = 0;
        try {
            while (index < field.length()) {
                // skip leading space
                while (field.charAt(index) == ' ') index++;
                // get key
                int keyStart = index;
                while (field.charAt(index) != '=') index++;
                String key = field.substring(keyStart, index);
                index++;
                int valueStart = index;
                while (index < field.length() && field.charAt(index) != ',') index++;
                event.putMDC(key, field.substring(valueStart, index), new Offset(startOffset + valueStart, startOffset + index));
                index++;
            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot parse " + field + " as MDC", e);
        }
    }
}
