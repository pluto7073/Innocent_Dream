package io.innocentdream.crash;

import java.util.Arrays;

public class CrashReportSection {

    private final String title;

    private StackTraceElement[] stackTrace = new StackTraceElement[0];
    private String[] rows = new String[0];
    private String message;

    public CrashReportSection(String title) {
        this.title = title;
    }

    public CrashReportSection setStackTrace(StackTraceElement[] stackTrace) {
        this.stackTrace = stackTrace;
        return this;
    }

    public CrashReportSection addRow(String rowText) {
        int l = rows.length;
        rows = Arrays.copyOf(rows, l + 1);
        rows[l] = rowText;
        return this;
    }

    public CrashReportSection setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(title).append(':');
        if (message != null) {
            builder.append('\n').append(message);
        }
        for (String row : rows) {
            builder.append('\n').append(row);
        }
        for (StackTraceElement element : stackTrace) {
            builder.append("\n\t").append(element.toString());
        }
        return builder.toString();
    }
}
