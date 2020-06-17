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
package org.smooks.cartridges.rules.mvel;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smooks.SmooksException;
import org.smooks.cartridges.rules.RuleEvalResult;
import org.smooks.cartridges.rules.RuleProvider;
import org.smooks.cdr.SmooksConfigurationException;
import org.smooks.container.ExecutionContext;
import org.smooks.expression.ExpressionEvaluator;
import org.smooks.expression.MVELExpressionEvaluator;
import org.smooks.resource.URIResourceLocator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <a href="http://mvel.codehaus.org/">MVEL</a> Rule Provider.
 * <p/>
 * Rules must be specified in Comma Separated Value files (CSV).  These can be edited
 * using a Spreadsheet application (Excel or OpenOffice).
 *
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class MVELProvider implements RuleProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(MVELProvider.class);
    private String name;
    private String src;
    private Map<String, ExpressionEvaluator> rules = new HashMap<String, ExpressionEvaluator>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
        loadRules();
    }

    public RuleEvalResult evaluate(String ruleName, CharSequence selectedData, ExecutionContext context) throws SmooksException {
        ExpressionEvaluator evaluator = rules.get(ruleName);

        if (evaluator == null) {
            throw new SmooksException("Unknown rule name '" + ruleName + "' on MVEL RuleProvider '" + name + "'.");
        }

        try {
            return new MVELRuleEvalResult(evaluator.eval(context.getBeanContext().getBeanMap()), ruleName, name, selectedData.toString());
        } catch(Throwable t) {
            return new MVELRuleEvalResult(t, ruleName, name, selectedData.toString());
        }
    }

    @SuppressWarnings("unchecked")
	private void loadRules() {
        if (src == null) {
            throw new SmooksException("ruleFile not specified.");
        }

        InputStream ruleStream;

        // Get the input stream...
        try {
            ruleStream = new URIResourceLocator().getResource(src);
        }
        catch (final IOException e) {
            throw new SmooksException("Failed to open rule file '" + src + "'.", e);
        }

        CSVReader csvLineReader = new CSVReader(new InputStreamReader(ruleStream));
        List<String[]> entries;
        try {
            entries = csvLineReader.readAll();
        } catch (IOException | CsvException e) {
            throw new SmooksConfigurationException("Error reading MVEL rule file (CSV format) '" + src + "'.", e);
        } finally {
            try {
                ruleStream.close();
            } catch (IOException e) {
                LOGGER.debug("Error closing MVEL rule file '" + src + "'.", e);
            }
        }

        for (String[] ruleLine : entries) {
            if(ruleLine.length == 2 && ruleLine[0].trim().charAt(0) != '#') {
                String ruleName = ruleLine[0].trim();
                String ruleExpression = ruleLine[1];

                if(rules.containsKey(ruleName)) {
                    LOGGER.debug("Duplicate rule definition '" + ruleName + "' in MVEL rule file '" + ruleName + "'.  Ignoring duplicate.");
                    continue;
                }

                rules.put(ruleName, new MVELExpressionEvaluator().setExpression(ruleExpression));
            }
        }
    }
}
