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
package io.marto.aem.lib;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.LinkedList;
import java.util.Queue;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.marto.aem.lib.impl.TypedResourceResolverImpl;

//import com.day.cq.wcm.api.Page;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Various utility methods for {@link Resource sling resources (Resource)}.
 *
 * @author Martin Petrovsky (martin at marto.io).
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceUtils {

    /**
     * A null safe utility method to get a child resource and adapt it to a <code>type</code>
     *
     * @param resource  the parent resource
     * @param relPath   the relative path of the child resource
     * @param type      the type the child should be adapted to
     * @return  null if the child can't be found or can't be adapted to type
     */
    public static <T> T child(Resource resource, String relPath, Class<T> type) {
        final Resource child = child(resource, relPath);
        return child == null ? null : child.adaptTo(type);
    }

    /**
     * A null safe utility method to get a child resource.
     *
     * @param resource  the parent resource
     * @param relPath   the relative path of the child resource
     * @return  null if the child can't be found or can't be adapted to type
     */
    public static Resource child(Resource resource, String relPath) {
        if (resource == null || isBlank(relPath)) {
            return null;
        }
        return resource.getChild(relPath);
    }

    /**
     * A helper method to adapt a resource to a specific type only if the resource is of the certain
     * <code>slingResourceType</code>. This is null safe from the point of view that the passed
     * <code>resource</code> can be null.
     *
     * @param resource
     *            the resource to adaptTo
     * @param klass
     *            the class to adaptTo
     * @param slingResourceType
     *            the sling resource type the resource must be otherwise, null is returned
     * @return The adapter target or null if the resource is null, or isn't not of <code>slingResourceType</code> or
     *         can't be adapted to <code>klass</code>
     */
    public static @CheckForNull <T> T adapt(@Nullable final Resource resource, Class<T> klass, String slingResourceType) {
        if (klass == null || isBlank(slingResourceType)) {
            throw new NullPointerException();
        }
        if (resource != null) {
            if (resource.isResourceType(slingResourceType)) {
                T ret = resource.adaptTo(klass);
                if (ret == null) {
                    LOGGER.debug("Failed to adapt path={} to class={} (type={})", resource.getPath(), klass.getSimpleName(), slingResourceType);
                }
                return ret;
            } else {
                LOGGER.debug(
                    "Failed to adapt path={} to model class={} because resourceType={} is not of expected type={}",
                    resource.getPath(), klass.getSimpleName(), resource.getResourceType(), slingResourceType);
            }
        }
        return null;
    }

// TODO Move to a PageUtils helper class
//    /**
//     * A Depth First Search to find a resource of <code>type</code> on a page.
//     *
//     * @param page to search
//     * @param type  the type of resource to find
//     * @return the 1st child resource of type <code>type</code>, <code>null</code> if none found
//     */
//    public static Resource find1stChildResource(Page page, String type) {
//        if (page == null) {
//            return null;
//        }
//        return find1stChildResource(page.getContentResource(), type);
//    }

    /**
     * A Depth First Search to find a resource of <code>type</code>. You should never call this on a page. Call it on page.getConentResource().
     *
     * @param resource the root of the search tree (i.e. jcr:content resource)
     * @param type  the type of resource to find
     * @return the 1st child resource of type <code>type</code>, <code>null</code> if none found
     */
    public static Resource find1stChildResource(Resource resource, String type) {
        if (resource == null) {
            return null;
        }
        final ResourceResolver resolver = resource.getResourceResolver();
        final Queue<Resource> stack = new LinkedList<>();
        stack.add(resource);
        while (!stack.isEmpty()) {
            Resource res = stack.poll();
            if (resolver.isResourceType(res, type)) {
                return res;
            } else {
                for (Resource child : res.getChildren()) {
                    if (child != null) {
                        stack.add(child);
                    }
                }
            }
        }
        return null;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(TypedResourceResolverImpl.class);
}
