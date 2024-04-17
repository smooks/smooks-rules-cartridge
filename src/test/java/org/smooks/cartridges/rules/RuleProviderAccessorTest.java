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

import org.junit.Test;
import org.smooks.api.ExecutionContext;
import org.smooks.api.SmooksException;
import org.smooks.cartridges.rules.regex.RegexRuleEvalResult;
import org.smooks.testkit.MockApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit test for {@link RuleProviderAccessor}.
 *
 * @author <a href="mailto:danielbevenius@gmail.com">Daniel Bevenius</a>
 */
public class RuleProviderAccessorTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIfAddingNullProvider() {
        RuleProviderAccessor.add(new MockApplicationContext(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIfAddingNullApplicationContext() {
        RuleProviderAccessor.add(null, new MockProvider());
    }

    @Test
    public void add() {
        final MockApplicationContext context = new MockApplicationContext();
        final MockProvider provider = new MockProvider();

        RuleProviderAccessor.add(context, provider);

        assertNotNull(RuleProviderAccessor.getRuleProviders(context));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIfApplicationContextIsNull() {
        RuleProviderAccessor.get(null, "dummyName");
    }

    @Test(expected = SmooksException.class)
    public void shouldThrowIfTheProviderCannotBeFound() {
        RuleProviderAccessor.get(new MockApplicationContext(), "dummyName");
    }

    @Test(expected = SmooksException.class)
    public void shouldThrowIfGettingNonExistingProvider() {
        final MockProvider provider = new MockProvider();
        final MockApplicationContext context = new MockApplicationContext();
        RuleProviderAccessor.add(context, provider);

        RuleProviderAccessor.get(context, "nonExistingProviderName");
    }

    @Test
    public void parseRuleProviderName() {
        final String rule = "addressing.email";
        String ruleName = RuleProviderAccessor.parseRuleProviderName(rule);
        assertEquals("addressing", ruleName);
    }

    @Test(expected = SmooksException.class)
    public void shouldThrowIfRuleProviderNameIsInvalid() {
        RuleProviderAccessor.parseRuleProviderName("addressing");
    }

    @Test
    public void parseRuleName() {
        final String rule = "addressing.email";
        String ruleName = RuleProviderAccessor.parseRuleName(rule);
        assertEquals("email", ruleName);
    }

    @Test(expected = SmooksException.class)
    public void shouldThrowIfRuleNameIsInvalid() {
        RuleProviderAccessor.parseRuleProviderName("email");
    }


    public static class MockProvider implements RuleProvider {
        private String src;
        private String name;

        public String getName() {
            return name;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public RuleEvalResult evaluate(String ruleName, CharSequence selectedData, ExecutionContext context) throws SmooksException {
            return new RegexRuleEvalResult(true, ruleName, "MockProvider", null, selectedData.toString());
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
