/*
 * Copyright 2015 Network New Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.networknt.light.rule.rule;

import com.networknt.light.rule.Rule;

import java.util.Map;

/**
 * Created by steve on 14/03/15.
 *
 * AccessLevel R [owner, admin, ruleAdmin]
 *
 */
public class UpdCorsRule extends AbstractRuleRule implements Rule {
    public boolean execute (Object ...objects) throws Exception {
        Map<String, Object> inputMap = (Map<String, Object>)objects[0];
        Map<String, Object> data = (Map<String, Object>)inputMap.get("data");
        Map eventMap = getEventMap(inputMap);
        Map<String, Object> eventData = (Map<String, Object>)eventMap.get("data");
        inputMap.put("eventMap", eventMap);
        String error = updateValidation(inputMap, eventData);
        if(error != null) {
            inputMap.put("result", error);
            return false;
        } else {
            eventData.put("enableCors", data.get("enableCors"));
            String corsHosts = (String)data.get("corsHosts");
            if(corsHosts != null) {
                eventData.put("corsHosts", corsHosts);
            }
            eventData.put("updateDate", new java.util.Date());
            return true;
        }
    }
}
