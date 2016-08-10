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

import java.util.List;
import java.util.stream.Stream;

import org.apache.sling.api.resource.ResourceResolver;

/**
 * An wrapper of a {@link ResourceResolver} that decorates it with methods mainly dealing with Sling Models (thus most
 * methods return a "typed" sling model).
 *
 * <h3>Conventions</h3>
 * <p>
 * As a general rule/conventions a get or a list... method will never return null, but instead throw an exception if it
 * can't find a resource or can't adapt it to a model, while a find... method may return null.
 * </p>
 *
 * @author Martin Petrovsky (martin at marto.io).
 */
public interface TypedResourceResolver extends ResourceResolver {

    /**
     * A simple null safe wrapper around {@link ResourceResolver#getResource(String) getResource(path)} that adapts a
     * resource if the resource if of sling:resourceType.
     *
     * @param path the absolute path to the resource object to be loaded.
     * @param klass the class to {@link org.apache.sling.api.adapter.Adaptable#adaptTo(Class) Adaptable#adaptTo(klass)} the resource to
     * @param <T> the <code>klass</code> type of the model
     * @param slingResourceType the sling:resourceType the resource at the given path must match before it's adapted to T
     *
     * @return the model object, null if the resource can't be located at the given path or is of wrong type.
     */
    <T> T findModel(String path, Class<T> klass, String slingResourceType);

    /**
     * This is synonymous to ls (list dir contents) to list a set of models of <code>Class T</code> located at
     * at <code>base</code> path location.
     *
     * @param base  the base location of the resources to list
     * @param subPath the relative path to the base to find the resource (eg: "jcr:content")
     * @param klass the class to {@link org.apache.sling.api.adapter.Adaptable#adaptTo(Class) adaptTo(klass)} the resource to
     * @param <T> the <code>klass</code> type of the model
     * @param slingResourceType the sling:resourceType the resource at the given path and subPath must match before it's adapted to T
     *
     * @return an unmodifiable {@link List} of <code>T</code> found, or an empty list if none found
     */
    <T> List<T> listModelChildren(String base, String subPath, Class<T> klass, String slingResourceType);


    /**
     * This is synonymous to `ls` (list dir contents) to list a set of models of <code>Class T</code> located at
     * at <code>parentPath</code> path location where the model sits under <code>subPath</code>.
     *
     * @param parentPath  the parentPath location of the resources to list
     * @param subPath the relative path to the parentPath to find the resource (eg: "jcr:content")
     * @param klass the class to {@link org.apache.sling.api.adapter.Adaptable#adaptTo(Class) adaptTo(klass)} the resource to
     * @param <T> the <code>klass</code> type of the model
     * @param slingResourceType the sling:resourceType the resource at the given path and subPath must match before it's adapted to T
     *
     * @return an unmodifiable {@link List} of <code>T</code> found, or an empty list if none found
     */
    <T> Stream<T> streamModelChildren(String parentPath, String subPath, Class<T> klass, String slingResourceType);

}
