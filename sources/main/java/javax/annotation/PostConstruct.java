/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2005-2018 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The <code>PostConstruct</code> annotation is used on a method that needs to
 * be executed after dependency injection is done to perform any initialization.
 * This method must be invoked before the class is put into service. This
 * annotation must be supported on all classes that support dependency
 * injection. The method annotated with <code>PostConstruct</code> must be
 * invoked even if the class does not request any resources to be injected. Only
 * one method in a given class can be annotated with this annotation. The method
 * on which the <code>PostConstruct</code> annotation is applied must fulfill
 * all of the following criteria:
 * <ul>
 * <li>The method must not have any parameters except in the case of
 * interceptors in which case it takes an <code>InvocationContext</code> object
 * as defined by the Interceptors specification.</li>
 * <li>The method defined on an interceptor class or superclass of an
 * interceptor class must have one of the following signatures:
 * <p>
 * void &#060;METHOD&#062;(InvocationContext)
 * <p>
 * Object &#060;METHOD&#062;(InvocationContext) throws Exception
 * <p>
 * <i>Note: A PostConstruct interceptor method must not throw application
 * exceptions, but it may be declared to throw checked exceptions including the
 * java.lang.Exception if the same interceptor method interposes on business or
 * timeout methods in addition to lifecycle events. If a PostConstruct
 * interceptor method returns a value, it is ignored by the container.</i></li>
 * <li>The method defined on a non-interceptor class must have the following
 * signature:
 * <p>
 * void &#060;METHOD&#062;()</li>
 * <li>The method on which the <code>PostConstruct</code> annotation is applied
 * may be public, protected, package private or private.</li>
 * <li>The method must not be static except for the application client.</li>
 * <li>The method should not be final.</li>
 * <li>If the method throws an unchecked exception the class must not be put
 * into service except in the case where the exception is handled by an
 * interceptor.</li>
 * </ul>
 *
 * @see javax.annotation.PreDestroy
 * @see javax.annotation.Resource
 * @since 1.6, Common Annotations 1.0
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface PostConstruct {
}
