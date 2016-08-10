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

/**
 * Used by {@link TypedResourceResolverFactory#execute(String, RepositoryTask)} to execute some business logic.
 *
 * @param <R> the type of result the task returns
 * @param <E> the type of exception the task may throw if an error occurs
 *
 * @see TypedResourceResolverFactory TypedResourceResolverFactory on how / where to use this
 */
public interface RepositoryTask<R, E extends Exception> {

    /**
     * @param resolver the sling resource resolver
     * @return the result of the execute task. This may be {@link Void}.
     * @throws E an error when things go wrong
     */
    R run(TypedResourceResolver resolver) throws E;

}
