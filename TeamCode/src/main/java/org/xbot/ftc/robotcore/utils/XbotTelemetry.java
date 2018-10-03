package org.xbot.ftc.robotcore.utils;

import java.util.ArrayList;
import java.util.List;

public class XbotTelemetry {

    private String caption;
    private Object value;

    private static List<XbotTelemetry> toAddToTelemetry = new ArrayList<>();

    private XbotTelemetry(String caption, Object value) {
        this.caption = caption;
        this.value = value;
    }

    public String getCaption() {
        return caption;
    }

    public Object getValue() {
        return value;
    }

    public static void addData(String caption, Object value) {
        toAddToTelemetry.add(new XbotTelemetry(caption, value));
    }

    public static List<XbotTelemetry> getDataToAddToTelemetry() {
        return toAddToTelemetry;
    }

    public static void clearData() {
        toAddToTelemetry.clear();
    }
}
