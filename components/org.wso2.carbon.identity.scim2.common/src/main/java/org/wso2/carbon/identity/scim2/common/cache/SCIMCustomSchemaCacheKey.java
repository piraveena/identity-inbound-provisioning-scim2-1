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

import java.io.Serializable;

public class SCIMCustomSchemaCacheKey implements Serializable {

    private static final long serialVersionUID = -1332814776225574523L;

    private final int tenantId;

    public SCIMCustomSchemaCacheKey(int tenantId) {

        this.tenantId = tenantId;
    }

    public int getTenantId() {

        return tenantId;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof SCIMCustomSchemaCacheKey)) {
            return false;
        }

        SCIMCustomSchemaCacheKey that = (SCIMCustomSchemaCacheKey) o;
        return tenantId == that.tenantId;
    }

    @Override
    public int hashCode() {
        return tenantId;
    }

}
