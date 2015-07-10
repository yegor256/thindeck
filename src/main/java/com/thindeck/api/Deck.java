/**
 * Copyright (c) 2014-2015, Thindeck.com
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
package com.thindeck.api;

import com.jcabi.aspects.Immutable;
import com.jcabi.xml.XML;
import com.jcabi.xml.XSD;
import com.jcabi.xml.XSDDocument;
import java.io.IOException;
import javax.validation.constraints.NotNull;
import org.xembly.Directive;

/**
 * Deck.
 *
 * <p>Deck is a configurable deployment of sources
 * to Docker containers. Deck should be configured through
 * {@link #update(java.lang.Iterable)}.
 *
 * <p>Deck is an XML document with data about the current
 * state of the deck. A memo can contain, for example, the
 * list of Docker containers that are running the deck at
 * the moment.
 *
 * <p>Full description of what information a memo should (and can)
 * include you can get from its XSD schema.
 *
 * <p>Deck should not guarantee any thread-safety. It is assumed
 * that the client calls {@link #read()} and {@link #update(Iterable)}
 * methods consequently. If two threads will update in parallel,
 * the result may be unpredictable.
 *
 * @author Yegor Bugayenko (yegor@teamed.io)
 * @version $Id$
 * @since 0.1
 */
@Immutable
public interface Deck {

    /**
     * Schema.
     */
    XSD SCHEMA = XSDDocument.make(
        Deck.class.getResourceAsStream("deck.xsd")
    );

    /**
     * Name, unique.
     *
     * <p>Name of the deck is unique in the entire system, which consists
     * of user name and a name of the deck, for example "yegor256/test".
     *
     * @return Unique name of the deck
     * @throws IOException If fails
     */
    @NotNull(message = "deck name can't be null")
    String name() throws IOException;

    /**
     * Read XML.
     *
     * <p>The method must guarantee that the XML document it
     * returns complies to the XSD schema.
     *
     * @return XML
     * @throws IOException If fails
     */
    @NotNull(message = "XML can't be null")
    XML read() throws IOException;

    /**
     * Update.
     *
     * <p>The method must throw a runtime exception if,
     * after applying the changes to the XML, it violates the XSD schema.
     *
     * @param dirs Directives
     * @throws IOException If fails
     */
    void update(Iterable<Directive> dirs) throws IOException;

    /**
     * Get events.
     * @return Events
     */
    Events events();

}
