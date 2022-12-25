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

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The <code>Generated</code> annotation is used to mark source code that has
 * been generated. It can also be used to differentiate user written code from
 * generated code in a single file.
 * <p>
 * The <code>value</code> element must have the name of the code generator. The
 * recommended convention is to use the fully qualified name of the code
 * generator in the value field, for example
 * <code>com.company.package.classname</code>.
 * </p>
 * <p>
 * The <code>date</code> element is used to indicate the date the source was
 * generated. The <code>date</code> element must follow the ISO 8601 standard.
 * For example, the <code>date</code> element could have the value
 * <code>2001-07-04T12:08:56.235-0700</code>, which represents 2001-07-04
 * 12:08:56 local time in the U.S. Pacific time zone.
 * </p>
 * <p>
 * The <code>comment</code> element is a place holder for any comments that the
 * code generator may want to include in the generated code.
 * </p>
 * 
 * @since 1.6, Common Annotations 1.0
 */

@Documented
@Retention(SOURCE)
@Target({ PACKAGE, TYPE, ANNOTATION_TYPE, METHOD, CONSTRUCTOR, FIELD, LOCAL_VARIABLE, PARAMETER })
public @interface Generated {
	/**
	 * The value element must have the name of the code generator. The recommended
	 * convention is to use the fully qualified name of the code generator. For
	 * example: <code>com.acme.generator.CodeGen</code>.
	 */
	String[] value();

	/**
	 * Date when the source was generated.
	 */
	String date() default "";

	/**
	 * A place holder for any comments that the code generator may want to include
	 * in the generated code.
	 */
	String comments() default "";
}
