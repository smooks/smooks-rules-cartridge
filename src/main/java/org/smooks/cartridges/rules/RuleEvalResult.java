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

/**
 * RuleEvalResult is the returned result from a {@link RuleProvider#evaluate(String, CharSequence, org.smooks.container.ExecutionContext)}
 * invocation.
 * <p/>
 *
 * Concrete RuleProviders may implement their own custom result that are more specific to the technology
 * used.
 *
 * @author <a href="mailto:danielbevenius@gmail.com">Daniel Bevenius</a>
 *
 */
public interface RuleEvalResult
{
    /**
     * The outcome of the rule evaluation.
     *
     * @return {@code true} if successful or false if it failed.
     */
    boolean matched();

    /**
     * Gets the name of the Rule that this class is a result of.
     *
     * @return String The name of the rule that created this result.
     */
    String getRuleName();

    /**
     * The name of the provider that produced this rule result.
     *
     * @return String The name of the Rule provider that produced this result.
     */
    String getRuleProviderName();

    /**
     * Get any provider level exceptions that may have
     * occured during the rule evaluation.
     * @return A provider level exception that occured during
     * rule evaluation, or null if no evaluation exception occured.
     */
    Throwable getEvalException();
}
