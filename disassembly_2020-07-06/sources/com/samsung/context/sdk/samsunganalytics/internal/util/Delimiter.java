package com.samsung.context.sdk.samsunganalytics.internal.util;

import java.util.Map;

public class Delimiter<K, V> {
    public static final String LOG_DELIMITER = "\u000e";

    public enum Depth {
        ONE_DEPTH("\u0002", "\u0003"),
        TWO_DEPTH("\u0004", "\u0005"),
        THREE_DEPTH("\u0006", "\u0007");
        
        private String collDlm;
        private String keyvalueDlm;

        private Depth(String str, String str2) {
            this.collDlm = str;
            this.keyvalueDlm = str2;
        }

        public String getCollectionDLM() {
            return this.collDlm;
        }

        public String getKeyValueDLM() {
            return this.keyvalueDlm;
        }
    }

    public String makeDelimiterString(Map<K, V> map, Depth depth) {
        String str;
        String str2 = null;
        for (Map.Entry next : map.entrySet()) {
            if (str2 == null) {
                str = next.getKey().toString();
            } else {
                str = (str2 + depth.getCollectionDLM()) + next.getKey();
            }
            str2 = (str + depth.getKeyValueDLM()) + next.getValue();
        }
        return str2;
    }
}
