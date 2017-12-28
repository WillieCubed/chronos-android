package com.craft.apps.countdowns.index;

import com.craft.apps.countdowns.common.model.Countdown;

import org.json.JSONArray;

import java.util.List;

/**
 * @version 1.0.0
 * @since 1.0.0
 */
public class AssistantIndex {

    public static String getStructuredContent(List<Countdown> countdowns) {
        // TODO: 7/3/17 Implement me
        JSONArray object = new JSONArray();
        for (Countdown countdown : countdowns) {
            object.put(countdown);
        }
        return object.toString();
    }

}
