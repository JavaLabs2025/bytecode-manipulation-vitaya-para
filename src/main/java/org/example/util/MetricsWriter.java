package org.example.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.metrics.MetricsCalculator.MetricsResult;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class MetricsWriter {

    private final ObjectMapper objectMapper;

    public MetricsWriter() {
        this.objectMapper = new ObjectMapper();
    }

    public void writeMetrics(MetricsResult result, File outputFile) throws IOException {
        Map<String, Object> metricsMap = metricsMapper(result);
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(metricsMap);
        System.out.println(json);

        if (outputFile != null) {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, metricsMap);
        }
    }

    private Map<String, Object> metricsMapper(MetricsResult result) {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put("maxInheritanceDepth", result.maxInheritanceDepth());
        map.put("avgInheritanceDepth", result.avgInheritanceDepth());
        map.put("abcMetric", result.abcMetric());
        map.put("avgOverriddenMethods", result.avgOverriddenMethods());
        map.put("avgFieldsPerClass", result.avgFieldsPerClass());

        return map;
    }
}