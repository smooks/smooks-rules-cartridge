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

import org.smooks.api.ExecutionContext;
import org.smooks.api.SmooksException;

/**
 * RuleProvider declares the contract which must be followed to make
 * different types of rule/evaluation technologies work with Smooks.
 * </p>
 *
 * @author <a href="mailto:danielbevenius@gmail.com">Daniel Bevenius</a>
 *
 */
public interface RuleProvider
{
    /**
     * Gets this providers name.
     * @return String This providers name.
     */
    String getName();

    /**
     * The name of this rule provider.
     * @param name The rule providers name.
     */
    void setName(final String name);

    /**
     * Gets the source for this rule provider
     * @return String The src for this rule provider
     */
    String getSrc();

    /**
     * Sets the src for this rule provider.
     * @param src The source which defines the rules.
     */
    void setSrc(final String src);

    /**
     * Evalutate the rule.
     *
     * @param ruleName The ruleName to be used in this evaluation.
     * @param selectedData The data that this evalute method will evaluate upon.
     * @param context The Smooks Excecution context.
     * @return {@code RuleEvalResult} Object representing an evaluation result.
     *
     * @throws SmooksException
     */
    RuleEvalResult evaluate(final String ruleName, final CharSequence selectedData, final ExecutionContext context) throws SmooksException;

}
