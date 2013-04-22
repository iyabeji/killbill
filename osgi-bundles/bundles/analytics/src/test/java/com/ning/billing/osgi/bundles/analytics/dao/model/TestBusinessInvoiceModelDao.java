/*
 * Copyright 2010-2013 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
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

package com.ning.billing.osgi.bundles.analytics.dao.model;

import java.math.BigDecimal;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ning.billing.osgi.bundles.analytics.AnalyticsTestSuiteNoDB;

public class TestBusinessInvoiceModelDao extends AnalyticsTestSuiteNoDB {

    @Test(groups = "fast")
    public void testConstructor() throws Exception {
        final BigDecimal balance = BigDecimal.ONE;
        final BigDecimal amountCharged = BigDecimal.ZERO;
        final BigDecimal originalAmountCharged = BigDecimal.ONE;
        final BigDecimal amountCredited = BigDecimal.TEN;

        final BusinessInvoiceModelDao invoiceModelDao = new BusinessInvoiceModelDao(account,
                                                                                    accountRecordId,
                                                                                    invoice,
                                                                                    amountCharged,
                                                                                    originalAmountCharged,
                                                                                    amountCredited,
                                                                                    invoiceRecordId,
                                                                                    auditLog,
                                                                                    tenantRecordId,
                                                                                    reportGroup);

        verifyBusinessModelDaoBase(invoiceModelDao, accountRecordId, tenantRecordId);
        Assert.assertEquals(invoiceModelDao.getCreatedDate(), invoice.getCreatedDate());
        Assert.assertEquals(invoiceModelDao.getInvoiceRecordId(), invoiceRecordId);
        Assert.assertEquals(invoiceModelDao.getInvoiceId(), invoice.getId());
        Assert.assertEquals(invoiceModelDao.getInvoiceNumber(), invoice.getInvoiceNumber());
        Assert.assertEquals(invoiceModelDao.getInvoiceDate(), invoice.getInvoiceDate());
        Assert.assertEquals(invoiceModelDao.getTargetDate(), invoice.getTargetDate());
        Assert.assertEquals(invoiceModelDao.getCurrency(), invoice.getCurrency().toString());
        Assert.assertEquals(invoiceModelDao.getAmountCharged(), amountCharged);
        Assert.assertEquals(invoiceModelDao.getOriginalAmountCharged(), originalAmountCharged);
        Assert.assertEquals(invoiceModelDao.getAmountCredited(), amountCredited);
        Assert.assertNull(invoiceModelDao.getBalance());
        Assert.assertNull(invoiceModelDao.getAmountPaid());
        Assert.assertNull(invoiceModelDao.getAmountRefunded());
    }
}