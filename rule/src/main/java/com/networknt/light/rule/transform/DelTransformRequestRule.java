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

package com.networknt.light.rule.transform;

import com.networknt.light.rule.Rule;

import java.util.Map;

/**
 * Created by steve on 16/02/15.
 *
 * AccessLevel R [owner, admin, ruleAdmin]
 */
public class DelTransformRequestRule extends AbstractTransformRule implements Rule {
    public boolean execute (Object ...objects) throws Exception {
        Map<String, Object> inputMap = (Map<String, Object>)objects[0];
        Map<String, Object> data = (Map<String, Object>)inputMap.get("data");
        Map<String, Object> payload = (Map<String, Object>) inputMap.get("payload");
        Map<String, Object> user = (Map<String, Object>)payload.get("user");
        String error = null;
        String host = (String)user.get("host");
        String ruleClass = (String)data.get("ruleClass");
        Integer sequence = (Integer)data.get("sequence");
        if(host != null) {
            // admin or ruleAdmin deleting transform rule for their site.
            if(!host.equals(data.get("host"))) {
                error = "User can only delete transform rule from host: " + host;
                inputMap.put("responseCode", 403);
            } else {
                // check if the ruleClass belongs to the host.
                if(!ruleClass.contains(host)) {
                    error = "ruleClass is not owned by the host: " + host;
                    inputMap.put("responseCode", 403);
                } else {
                    String json = getTransformRequestBySeq(ruleClass, sequence);
                    if(json == null) {
                        error = "Transform rule does not exist";
                        inputMap.put("responseCode", 404);
                    } else {
                        Map eventMap = getEventMap(inputMap);
                        Map<String, Object> eventData = (Map<String, Object>)eventMap.get("data");
                        inputMap.put("eventMap", eventMap);
                        eventData.put("ruleClass", data.get("ruleClass"));
                        eventData.put("sequence", data.get("sequence"));
                    }
                }
            }
        } else {
            String json = getTransformRequestBySeq(ruleClass, sequence);
            if(json == null) {
                error = "Transform rule does not exist";
                inputMap.put("responseCode", 404);
            } else {
                Map eventMap = getEventMap(inputMap);
                Map<String, Object> eventData = (Map<String, Object>)eventMap.get("data");
                inputMap.put("eventMap", eventMap);
                eventData.put("ruleClass", data.get("ruleClass"));
                eventData.put("sequence", data.get("sequence"));
            }
        }

        if(error != null) {
            inputMap.put("result", error);
            return false;
        } else {
            return true;
        }
    }
}
