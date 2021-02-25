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

import org.smooks.api.ApplicationContext;
import org.smooks.api.SmooksException;
import org.smooks.assertion.AssertArgument;

import java.util.HashMap;
import java.util.Map;

/**
 * RuleProviderAccessor provides convenience methods for adding and getting
 * rule providers in a Smooks Application context.
 *
 * @author <a href="mailto:danielbevenius@gmail.com">Daniel Bevenius</a>
 */
public final class RuleProviderAccessor {
    /**
     * Sole private constructor.
     */
    private RuleProviderAccessor() {
    }

    /**
     * Adds the passed-in provider to the Smooks {@link ApplicationContext}.
     *
     * @param context  The Smooks {@link ApplicationContext}.
     * @param provider The {@link RuleProvider} that is to be added.
     */
    public static final void add(final ApplicationContext context, final RuleProvider provider) {
        AssertArgument.isNotNull(context, "context");
        AssertArgument.isNotNull(provider, "provider");

        Map<String, RuleProvider> providers = getRuleProviders(context);
        if (providers == null) {
            providers = new HashMap<>();
            context.getRegistry().registerObject(RuleProvider.class, providers);
        }

        providers.put(provider.getName(), provider);
    }

    /**
     * Gets a {@link RuleProvider} matching the passed in ruleProviderName.
     *
     * @param context          The Smooks {@link ApplicationContext}.
     * @param ruleProviderName The name of the rule provider to lookup.
     * @return {@link RuleProvider} The {@link RuleProvider} matching the passed in ruleProviderName.
     * @throws SmooksException If no providers have been previously set in the {@link ApplicationContext}, or if
     *                         the specified ruleProviderName cannot be found.
     */
    public static final RuleProvider get(final ApplicationContext context, final String ruleProviderName) {
        AssertArgument.isNotNull(context, "context");

        Map<String, RuleProvider> providers = getRuleProviders(context);
        if (providers == null || providers.isEmpty()) {
            throw new SmooksException("No RuleProviders were found. Have you configured a rules section in your Smooks configuration?");
        }

        final RuleProvider provider = providers.get(ruleProviderName);
        if (provider == null) {
            throw new SmooksException("Not provider with name '" + ruleProviderName + "' was found in the execution context. Have you configured the rules section properly?");
        }

        return provider;
    }

    /**
     * Gets the Map of RuleProviders that exist in the Smooks AppcliationContext.
     *
     * @param context The Smooks {@link ApplicationContext}.
     * @return Map<String, RuleProvider> The Map of rule providers. The String key is the name of the rule provider.
     */
    @SuppressWarnings("unchecked")
    public static Map<String, RuleProvider> getRuleProviders(final ApplicationContext context) {
        return (Map<String, RuleProvider>) context.getRegistry().lookup(RuleProvider.class);
    }

    /**
     * Parse the rule name from the passed in composite rule name.
     *
     * @param compositeRuleName The composite rule name in the form ruleProvider.ruleName.
     * @return {@code String} The rule name part of the composite rule name.
     */
    public static String parseRuleName(final String compositeRuleName) {
        final int lastIndexOfDot = compositeRuleName.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            throwInvalidCompositeRuleName(compositeRuleName);
        }
        return compositeRuleName.substring(lastIndexOfDot + 1);
    }

    /**
     * @param compositeRuleName The composite rule name in the form ruleProvider.ruleName.
     * @return {@code String} The rule provider name part of the composite rule name.
     */
    public static String parseRuleProviderName(final String compositeRuleName) {
        final int lastIndexOfDot = compositeRuleName.indexOf('.');
        if (lastIndexOfDot == -1) {
            throwInvalidCompositeRuleName(compositeRuleName);
        }
        return compositeRuleName.substring(0, lastIndexOfDot);
    }

    private static void throwInvalidCompositeRuleName(final String compositRuleName) {
        throw new SmooksException("A rule must be specified in the format <ruleProviderName>.<ruleName>." +
                " Please check you configuration and make sure that you are referencing the rule in a correct manner.");
    }

}
