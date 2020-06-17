/*-
 * ========================LICENSE_START=================================
 * smooks-rules-cartridge
 * %%
 * Copyright (C) 2020 Smooks
 * %%
 * Licensed under the terms of the Apache License Version 2.0, or
 * the GNU Lesser General Public License version 3.0 or later.
 * 
 * SPDX-License-Identifier: Apache-2.0 OR LGPL-3.0-or-later
 * 
 * ======================================================================
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
 * 
 * ======================================================================
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * =========================LICENSE_END==================================
 */
package org.smooks.cartridges.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.smooks.Smooks;
import org.smooks.SmooksException;
import org.smooks.cartridges.rules.regex.RegexRuleEvalResult;
import org.smooks.container.ExecutionContext;
import org.smooks.payload.StringResult;
import org.smooks.payload.StringSource;
import org.xml.sax.SAXException;

/**
 * Unit test for RuleProviderFactory.
 * <p/>
 *
 * @author <a href="mailto:danielbevenius@gmail.com">Daniel Bevenius</a>
 */
public class RulesProviderFactoryTest
{
    @Test
    public void extendedConfig() throws IOException, SAXException
    {
        final Smooks smooks = new Smooks("/smooks-configs/extended/1.0/smooks-rules-config.xml");
        final StringSource source = new StringSource("<order></order>");
        final StringResult result = new StringResult();

        smooks.filterSource(source, result);

        final Map<String, RuleProvider> ruleProviders = RuleProviderAccessor.getRuleProviders(smooks.getApplicationContext());

        assertNotNull("Not rules providers were created!", ruleProviders);
        assertEquals(1, ruleProviders.size());
        assertNotNull(RuleProviderAccessor.get(smooks.getApplicationContext(), "custom"));
    }

    @Test
    @Ignore
    public void createProvider()
    {
        final RuleProvider provider = new RulesProviderFactory().createProvider(MockProvider.class);
        assertNotNull(provider);
        assertTrue(provider instanceof MockProvider);
        assertEquals("MockProvider", provider.getName());
    }

    public static class MockProvider implements RuleProvider
    {
        public String getName()
        {
            return getClass().getSimpleName();
        }

        public String getSrc()
        {
            return null;
        }

        public void setSrc(String src)
        {
        }

        public RuleEvalResult evaluate(String ruleName, CharSequence selectedData, ExecutionContext context) throws SmooksException
        {
            return new RegexRuleEvalResult(true, ruleName, "MockProvider", null, selectedData.toString());
        }

        public void setName(String name)
        {
        }
    }

}
