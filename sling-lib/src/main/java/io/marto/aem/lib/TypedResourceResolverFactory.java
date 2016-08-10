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

import javax.annotation.Nonnull;

import org.apache.sling.api.resource.ResourceResolver;

/**
 * A Factory (OSGi Component), that provides access to a {@link TypedResourceResolver}. The {@link #execute(String, RepositoryTask)}
 * method is of particular relevance as this provides a Java 8 lambda mechanism to execute a piece of sling repository code
 * within the context of "Service User" repository session (i.e. a {@link TypedResourceResolver}).
 *
 * <h3>Example</h3>
 * <pre><code>
 * class SomeBusinesLogic {
 *   {@literal @}Reference
 *   private TypedResourceResolverFactory resolverFactory;
 *
 *   public List&lt;MyModel&gt; getMyModels() {
 *     return resolverFactory.execute("my-service",
 *       resolver -&gt; resolver.listModelChildren("/content/my-site/en", "jcr:content", MyModel.class, "my-project/components/page/my-page-model"))
 *       .collect(Collectors.toList());
 *   }
 * }
 * </code></pre>
 *
 * @author Martin Petrovsky (martin at marto.io).
 **/
public interface TypedResourceResolverFactory {

    /**
     * A convenience method to obtain a {@link TypedResourceResolver}.
     *
     * @param subService the "Sub Service" mapped to the repository user that is used to log into the repository.
     *
     * @return return the resource resolver; never null
     * @throws RepositoryLoginException if the resource resolver can't be obtained from the sub-service.
     */
    @Nonnull TypedResourceResolver getSubServiceResolver(String subService) throws RepositoryLoginException;

    /**
     * Execute some repository business logic (<code>task</code>) as a logged in "Service User". This method implements
     * the boilerplate logic of:
     * <ul>
     * <li>Logging into the repository as the user mapped by "Sub Service" (<code>subService</code>)
     * <li>Ensuring that the {@link ResourceResolver#close()} method is always called after the business logic finishes
     * executing
     * </ul>
     *
     * @param subService
     *            the "Sub Service" mapped to the repository user that is used to log into the repository to execute the
     *            business logic. The user should have the correct access to perform the business logic.
     * @param task
     *            the business logic to execute.
     * @param <T> the type of result the task returns
     * @param <E> the type of exception the task may throw if an error occurs
     *
     * @return T whatever is returned by the <code>task</code>
     * @throws E
     *             the exception thrown by the <code>task</code>
     * @throws RepositoryLoginException
     *             when we can't log in as the service user.
     */
    <T, E extends Exception> T execute(String subService, RepositoryTask<T, E> task) throws E, RepositoryLoginException;

}
