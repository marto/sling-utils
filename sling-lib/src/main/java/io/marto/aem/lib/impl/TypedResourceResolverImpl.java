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

import static io.marto.aem.lib.ResourceUtils.adapt;
import static io.marto.aem.lib.impl.Streams.streamOf;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import io.marto.aem.lib.TypedResourceResolver;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public class TypedResourceResolverImpl implements TypedResourceResolver {

    private final @Delegate ResourceResolver delegate;

    @Override
    public @Nullable <T> T findModel(String path, Class<T> klass, String slingResourceType) {
        if (isBlank(path)) {
            throw new IllegalArgumentException();
        }

        return adapt(getResource(path), klass, slingResourceType);
    }

    @Override
    public @Nonnull <T> List<T> listModelChildren(String parentPath, String subPath, Class<T> klass, String slingResourceType) {
        return streamModelChildren(parentPath, subPath, klass, slingResourceType)
            .collect(Collectors.toList());
    }

    @Override
    public @Nonnull <T> Stream<T> streamModelChildren(String parentPath, String subPath, Class<T> klass, String slingResourceType) {
        if (isBlank(parentPath) || isBlank(subPath) || klass == null || isBlank(slingResourceType)) {
            throw new IllegalArgumentException();
        }

        final Resource resource = getResource(parentPath);
        if (resource == null) {
            return Stream.empty();
        }

        return streamOf(resource.getChildren())
            .map(child -> adapt(child.getChild(subPath), klass, slingResourceType))
            .filter(Objects::nonNull);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
