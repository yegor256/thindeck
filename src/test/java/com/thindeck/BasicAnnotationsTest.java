/**
 * Copyright (c) 2014, Thindeck.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the thindeck.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.thindeck;

import com.thindeck.dynamo.DyBase;
import com.thindeck.dynamo.DyRepo;
import com.thindeck.dynamo.DyRepos;
import com.thindeck.dynamo.DyTask;
import com.thindeck.dynamo.DyTasks;
import com.thindeck.dynamo.DyUser;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import lombok.EqualsAndHashCode;
import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test suite for checking if classes have some required basic annotations .
 * @author Aleksey Popov
 */
public class BasicAnnotationsTest {

    @Test
    public void checkForEqualsAndHashCode() throws Exception {
        MatcherAssert.assertThat(
            Arrays.asList(
                DyTask.class,DyTasks.class, DyBase.class,
                DyRepo.class, DyRepos.class, DyUser.class

            ),
            Matchers.everyItem(
                isAnnotatedWith(EqualsAndHashCode.class)
            )
        );
    }

    private Matcher<Class<?>> isAnnotatedWith(final Class<? extends Annotation> annotation) {
        return new CustomTypeSafeMatcher<Class<?>>("annotated type") {
            @Override
            protected boolean matchesSafely(final Class<?> aClass) {
                return aClass.isAnnotationPresent(annotation);
            }
        };
    }
}
