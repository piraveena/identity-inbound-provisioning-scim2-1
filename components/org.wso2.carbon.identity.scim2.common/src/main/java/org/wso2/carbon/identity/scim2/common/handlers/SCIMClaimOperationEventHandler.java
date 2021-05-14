/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.scim2.common.handlers;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.event.IdentityEventConstants;
import org.wso2.carbon.identity.event.IdentityEventException;
import org.wso2.carbon.identity.event.event.Event;
import org.wso2.carbon.identity.event.handler.AbstractEventHandler;
import org.wso2.carbon.identity.scim2.common.cache.SCIMAttributeSchemaCache;
import org.wso2.carbon.identity.scim2.common.utils.SCIMCommonUtils;

import static org.wso2.carbon.identity.scim2.common.utils.SCIMCommonUtils.getCustomSchemaURI;

public class SCIMClaimOperationEventHandler extends AbstractEventHandler {

    private static final Log log = LogFactory.getLog(SCIMClaimOperationEventHandler.class);
    public static final String WSO2_CARBON_DIALECT = "http://wso2.org/claims";

    @Override
    public void handleEvent(Event event) throws IdentityEventException {

        int tenantId = (int) event.getEventProperties().get(IdentityEventConstants.EventProperty.TENANT_ID);
        if (log.isDebugEnabled()) {
            log.debug(event.getEventName() + " event received to SCIMClaimOperationEventHandler for the tenant with " +
                    "Id: "+ tenantId);
        }

        if (!SCIMCommonUtils.isCustomSchemaEnabled()) {
            if (log.isDebugEnabled()) {
                log.debug("SCIM2 Custom user schema has disabled in server level.");
            }
            return;
        }

        String claimDialectUri =
                (String) event.getEventProperties().get(IdentityEventConstants.EventProperty.CLAIM_DIALECT_URI);
        if (StringUtils.isNotBlank(claimDialectUri) && !(claimDialectUri.equalsIgnoreCase(getCustomSchemaURI())
                || claimDialectUri.equalsIgnoreCase(WSO2_CARBON_DIALECT))) {
            if (log.isDebugEnabled()) {
                log.debug("Needs to handle only if this claim update happens to SCIM2 custom schema or local claim "
                        + "dialect for tenant with Id: "+ tenantId);
            }
            return;
        }
        // If claim dialect rename happens, then we need to check whether the custom schema has renamed to another name.
        String oldClaimDialectUri =
                (String) event.getEventProperties().get(IdentityEventConstants.EventProperty.OLD_CLAIM_DIALECT_URI);
        if (StringUtils.isNotBlank(oldClaimDialectUri) && !oldClaimDialectUri.equalsIgnoreCase(getCustomSchemaURI())) {
            if (log.isDebugEnabled()) {
                log.debug("Needs to clear the cache only if the SCIM2 custom schema has changed");
            }
            return;
        }

        SCIMAttributeSchemaCache.getInstance().clearSCIMCustomAttributeSchemaByTenant(tenantId);
    }

    @Override
    public String getName() {

        return "SCIMClaimOperationEventHandler";
    }
}
