package com.craft.apps.countdowns.index;

import com.craft.apps.countdowns.common.model.Countdown;
import java.util.List;
import org.json.JSONArray;

/**
 * @author willie
 * @version 1.0.0
 * @since v1.0.0 (7/3/17)
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
