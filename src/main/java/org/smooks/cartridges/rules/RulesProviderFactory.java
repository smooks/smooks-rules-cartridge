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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smooks.api.ApplicationContext;
import org.smooks.api.SmooksConfigException;
import org.smooks.api.SmooksException;
import org.smooks.api.delivery.ContentHandler;

import jakarta.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

/**
 * RulesProviderFactory is responsible for creating {@link RuleProvider}s
 * and installing those providers in the Smooks {@link ApplicationContext}.
 * <p/>
 *
 * @author <a href="mailto:danielbevenius@gmail.com">Daniel Bevenius</a>
 */
public final class RulesProviderFactory implements ContentHandler {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RulesProviderFactory.class);

    /**
     * The Smooks {@link ApplicationContext}.
     */
    @Inject
    private ApplicationContext applicationContext;

    /**
     * The rule name to be used.
     */
    @Inject
    private String name;

    /**
     * The {@link RuleProvider} implementation to be used.
     */
    @Inject
    @Named("provider")
    private Class<RuleProvider> provider;

    /**
     * The source of the rule. Is implementation dependent, may be a file for example.
     */
    @Inject
    private Optional<String> src;

    /**
     * Creates and installs the configured rule provider.
     *
     * @throws SmooksConfigException
     */
    @PostConstruct
    public void installRuleProvider() throws SmooksConfigException {
        LOGGER.debug(this.toString());
        if (RuleProvider.class.isAssignableFrom(provider)) {
            final RuleProvider providerImpl = createProvider(provider);
            providerImpl.setName(name);
            providerImpl.setSrc(src.orElse(null));

            RuleProviderAccessor.add(applicationContext, providerImpl);
        } else {
            throw new SmooksConfigException("Invalid rule provider configuration :'" + this + "'");
        }
    }

    RuleProvider createProvider(final Class<? extends RuleProvider> providerClass) throws SmooksException {
        try {
            return providerClass.newInstance();
        } catch (final InstantiationException | IllegalAccessException e) {
            throw new SmooksException(e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return String.format("%s [name=%s, src=%s, provider=%s]", getClass().getSimpleName(), name, src, provider);
    }

}
