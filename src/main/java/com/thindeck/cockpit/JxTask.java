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
package com.thindeck.cockpit;

import com.rexsl.page.Link;
import com.thindeck.api.Repo;
import com.thindeck.api.Task;
import java.util.Collection;
import java.util.LinkedList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Jaxb Task.
 *
 * @author Yegor Bugayenko (yegor@tpc2.com)
 * @version $Id$
 * @since 0.1
 */
@XmlRootElement(name = "task")
@XmlAccessorType(XmlAccessType.NONE)
final class JxTask {

    /**
     * Repo.
     */
    private final transient Repo repo;

    /**
     * Task.
     */
    private final transient Task task;

    /**
     * BaseRs.
     */
    private final transient BaseRs base;

    /**
     * Ctor.
     */
    JxTask() {
        throw new UnsupportedOperationException("#JxTask()");
    }

    /**
     * Ctor.
     * @param rpo Repo
     * @param tsk Task
     * @param res BaseRs
     */
    JxTask(final Repo rpo, final Task tsk, final BaseRs res) {
        this.repo = rpo;
        this.task = tsk;
        this.base = res;
    }

    /**
     * Its number.
     * @return Number
     */
    @XmlElement(name = "number")
    public long getNumber() {
        return this.task.number();
    }

    /**
     * Its command.
     * @return Command
     */
    @XmlElement(name = "command")
    public String getCommand() {
        return this.task.command();
    }

    /**
     * Its links.
     * @return Links
     */
    @XmlElementWrapper(name = "links")
    @XmlElement(name = "link")
    public Collection<Link> getLinks() {
        final Collection<Link> links = new LinkedList<Link>();
        links.add(
            new Link(
                "drain",
                this.base.uriInfo().getBaseUriBuilder().clone()
                    .path(TaskRs.class)
                    .build(this.repo.name(), this.task.number())
            )
        );
        return links;
    }

}
