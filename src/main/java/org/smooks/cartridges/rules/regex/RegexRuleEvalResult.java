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
package org.smooks.cartridges.rules.regex;

import org.smooks.cartridges.rules.BasicRuleEvalResult;

import java.util.regex.Pattern;

/**
 * Regex RuleEvalResult.
 * This class extends {@link BasicRuleEvalResult} and adds the Regex Pattern
 * and text that te regex was evaluated on.
 *
 * @author <a href="mailto:danielbevenius@gmail.com">Daniel Bevenius</a>
 */
public class RegexRuleEvalResult extends BasicRuleEvalResult
{
    /**
     * Serial unique identifier.
     */
    private static final long serialVersionUID = -3431124009222908170L;

    /**
     * The regex pattern.
     */
    final Pattern pattern;

    /**
     * The text used in the match.
     */
    private String text;

    /**
     * Creates a RuleEvalResult that indicates a successfully executed rule.
     */
    public RegexRuleEvalResult(final boolean matched, final String ruleName, final String ruleProviderName, final Pattern pattern, final String text)
    {
        super(matched, ruleName, ruleProviderName);
        this.pattern = pattern;
        this.text = text;
    }

    /**
     * @return Patten the compiled regular expression.
     */
    public Pattern getPattern()
    {
        return pattern;
    }

    /**
     * @return String the text that the  regular expression was evaluated on/against.
     */
    public String getText() {
        return text;
    }

    @Override
    public String toString()
    {
        return String.format("%s, matched=%b, providerName=%s, ruleName=%s, text=%s, pattern=%s", getClass().getSimpleName(), matched(), getRuleProviderName(), getRuleName(), text, pattern);
    }

}
