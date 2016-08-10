/*
Copyright (c) 2015-2017 "Martin Petrovsky"

This file is part of aem-utils (marto.io).

This is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package io.marto.aem.lib.impl;

import static java.lang.String.format;
import static java.util.Collections.singletonMap;
import static org.apache.sling.api.resource.ResourceResolverFactory.SUBSERVICE;

import javax.annotation.Nonnull;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolverFactory;

import io.marto.aem.lib.RepositoryLoginException;
import io.marto.aem.lib.RepositoryTask;
import io.marto.aem.lib.TypedResourceResolver;
import io.marto.aem.lib.TypedResourceResolverFactory;

@Component
@Service
public class TypedResourceResolverFactoryImpl implements TypedResourceResolverFactory {

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    public @Nonnull TypedResourceResolver getSubServiceResolver(String subService) throws RepositoryLoginException {
        try {
            return new TypedResourceResolverImpl(resolverFactory.getServiceResourceResolver(singletonMap(SUBSERVICE, (Object) subService)));
        } catch (LoginException e) {
            throw new RepositoryLoginException(format("Failed to access repository as '%s': %s", subService, e.getMessage()),e);
        }
    }

    @Override
    public <T, E extends Exception> T execute(String srvc, RepositoryTask<T, E> action) throws E, RepositoryLoginException {
        TypedResourceResolver resolver = null;
        try {
            resolver = getSubServiceResolver(srvc);
            return action.run(new TypedResourceResolverImpl(resolver));
        } finally {
            if (resolver != null && resolver.isLive()) {
                resolver.close();
            }
        }
    }
}
