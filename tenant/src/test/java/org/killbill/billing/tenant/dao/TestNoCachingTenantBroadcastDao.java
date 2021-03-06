/*
 * Copyright 2014 Groupon, Inc
 * Copyright 2014 The Billing Project, LLC
 *
 * The Billing Project licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.killbill.billing.tenant.dao;

import java.util.List;
import java.util.UUID;

import org.killbill.billing.callcontext.InternalCallContext;
import org.killbill.billing.tenant.TenantTestSuiteWithEmbeddedDb;
import org.killbill.billing.util.callcontext.CallOrigin;
import org.killbill.billing.util.callcontext.UserType;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestNoCachingTenantBroadcastDao extends TenantTestSuiteWithEmbeddedDb {

    @Test(groups = "slow")
    public void testBasic() throws Exception {
        final TenantBroadcastModelDao model = new TenantBroadcastModelDao("foo");

        final InternalCallContext context79 = createContext(79L);
        tenantBroadcastDao.create(model, context79);

        final TenantBroadcastModelDao result1 = tenantBroadcastDao.getById(model.getId(), context79);
        Assert.assertEquals(result1.getTenantRecordId(), new Long(79L));
        Assert.assertEquals(result1.getType(), "foo");

        final TenantBroadcastModelDao resultNull = tenantBroadcastDao.getById(model.getId(), internalCallContext);
        Assert.assertNull(resultNull);

        final TenantBroadcastModelDao result2 = noCachingTenantBroadcastDao.getLatestEntry();
        Assert.assertEquals(result2.getTenantRecordId(), new Long(79L));
        Assert.assertEquals(result2.getType(), "foo");
    }

    @Test(groups = "slow")
    public void testLatestEntries() throws Exception {

        final InternalCallContext context79 = createContext(81L);
        TenantBroadcastModelDao latestInsert = null;
        for (int i = 0; i < 100; i++) {
            final TenantBroadcastModelDao model = new TenantBroadcastModelDao("foo-" + i);
            tenantBroadcastDao.create(model, context79);
            latestInsert = model;
        }
        final TenantBroadcastModelDao latestInsertRefreshed = tenantBroadcastDao.getById(latestInsert.getId(), context79);
        final TenantBroadcastModelDao lastEntry = noCachingTenantBroadcastDao.getLatestEntry();

        Assert.assertEquals(lastEntry.getRecordId(), latestInsertRefreshed.getRecordId());

        final int expectedEntries = 25;
        final Long fromRecordId = lastEntry.getRecordId() - expectedEntries;
        final List<TenantBroadcastModelDao> result = noCachingTenantBroadcastDao.getLatestEntriesFrom(fromRecordId);
        Assert.assertEquals(result.size(), expectedEntries);

        long i = 0;
        for (TenantBroadcastModelDao cur : result) {
            Assert.assertEquals(cur.getRecordId().longValue(), (fromRecordId + i++ + 1L));
        }

    }

    private InternalCallContext createContext(final Long tenantRecordId) {
        return new InternalCallContext(tenantRecordId, 0L, UUID.randomUUID(),
                                       UUID.randomUUID().toString(), CallOrigin.TEST,
                                       UserType.TEST, "Testing TestNoCachingTenantBroadcastDao", "This is a test for TestNoCachingTenantBroadcastDao",
                                       clock.getUTCNow(), clock.getUTCNow());

    }
}
