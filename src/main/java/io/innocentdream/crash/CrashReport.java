package io.innocentdream.crash;

import io.innocentdream.utils.Utils;

import java.io.File;
import java.text.DateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Date;

public class CrashReport {

    public static final ArrayList<CrashReportPopulator> POPULATORS = new ArrayList<>();

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss");

    private final Throwable throwable;

    private final ArrayList<CrashReportSection> sections;
    private final String fileName;

    public CrashReport(Throwable throwable) {
        this.throwable = throwable;
        this.sections = new ArrayList<>();
        this.sections.add(new CrashReportSection("What Went Wrong")
                .setMessage(throwable.getClass().getName() + ": " + throwable.getMessage())
                .setStackTrace(throwable.getStackTrace()));
        POPULATORS.forEach(p -> p.populateCrashReport(this));
        Instant instant = Instant.now();
        fileName = String.format("crash_report_%s.txt", FORMATTER.format(instant));
        File f = new File(Utils.RUN_DIR, "crashReports");
        f.mkdirs();
    }

    public void addSection(CrashReportSection section) {
        this.sections.add(section);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("A Problem Has Occurred!");
        for (CrashReportSection s : sections) {
            builder.append("\n\n").append(s.toString());
        }
        builder.append("\n\n").append("This crash report has been saved to ").append(new File(Utils.RUN_DIR, "crashReports").getAbsolutePath())
                .append(File.separator).append(fileName);
        return builder.toString();
    }
}
