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

package org.wso2.carbon.identity.scim2.common.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.common.cache.BaseCache;
import org.wso2.charon3.core.schema.AttributeSchema;

/**
 * This stores custom AttributeSchema against tenants.
 */
public class SCIMAttributeSchemaCache extends BaseCache<SCIMAttributeSchemaCacheKey, SCIMAttributeSchemaCacheEntry> {

    private static final String SCIM_CUSTOM_SCHEMA_CACHE = "SCIMAttributeSchemaCache";
    private static final Log log = LogFactory.getLog(SCIMAttributeSchemaCache.class);

    private static volatile SCIMAttributeSchemaCache instance;

    private SCIMAttributeSchemaCache() {

        super(SCIM_CUSTOM_SCHEMA_CACHE);
    }

    public static SCIMAttributeSchemaCache getInstance() {

        if (instance == null) {
            synchronized (SCIMAttributeSchemaCache.class) {
                if (instance == null) {
                    instance = new SCIMAttributeSchemaCache();
                }
            }
        }
        return instance;
    }

    /**
     * Add custom attribute schema to cache against tenantId.
     *
     * @param tenantId TenantId.
     * @param customAttributeSchema CustomAttributeSchema.
     */
    public void addSCIMCustomAttributeSchema(int tenantId, AttributeSchema customAttributeSchema){

        SCIMAttributeSchemaCacheKey cacheKey = new SCIMAttributeSchemaCacheKey(tenantId);
        SCIMAttributeSchemaCacheEntry cacheEntry = new SCIMAttributeSchemaCacheEntry(customAttributeSchema);
        super.addToCache(cacheKey, cacheEntry);
        if (log.isDebugEnabled()) {
            log.debug("Successfully added scim custom attributes into SCIMCustomSchemaCache for the tenant:"
                    + tenantId);
        }

    }


    /**
     * Get SCIM2 Custom AttributeSchema by tenantId.
     *
     * @param tenantId TenantId.
     * @return AttributeSchema.
     */
    public AttributeSchema getSCIMCustomAttributeSchemaByTenant(int tenantId) {

        SCIMAttributeSchemaCacheKey cacheKey = new SCIMAttributeSchemaCacheKey(tenantId);
        SCIMAttributeSchemaCacheEntry cacheEntry = super.getValueFromCache(cacheKey);
        if (cacheEntry != null) {
            return cacheEntry.getSCIMCustomAttributeSchema();
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Cache entry is null for tenantId: " + tenantId);
            }
            return null;
        }
    }

    /**
     * Clear SCIM2 Custom AttributeSchema by tenantId.
     *
     * @param tenantId TenantId.
     */
    public void clearSCIMCustomAttributeSchemaByTenant(int tenantId) {

        if (log.isDebugEnabled()) {
            log.debug("Clearing SCIMCustomAttributeSchemaCache entry by the tenant with id: " + tenantId);
        }
        SCIMAttributeSchemaCacheKey cacheKey = new SCIMAttributeSchemaCacheKey(tenantId);
        super.clearCacheEntry(cacheKey);
    }
}
