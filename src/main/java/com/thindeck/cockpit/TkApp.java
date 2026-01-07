/**
 * SPDX-FileCopyrightText: Copyright (c) 2014-2026, Thindeck.com
 * SPDX-License-Identifier: MIT
 */
package com.thindeck.cockpit;

import com.jcabi.log.VerboseProcess;
import com.jcabi.manifests.Manifests;
import com.thindeck.api.Base;
import com.thindeck.cockpit.deck.TkDeck;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Pattern;
import org.takes.Take;
import org.takes.facets.auth.PsByFlag;
import org.takes.facets.flash.TkFlash;
import org.takes.facets.fork.FkAnonymous;
import org.takes.facets.fork.FkAuthenticated;
import org.takes.facets.fork.FkFixed;
import org.takes.facets.fork.FkHitRefresh;
import org.takes.facets.fork.FkParams;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.facets.forward.TkForward;
import org.takes.tk.TkClasspath;
import org.takes.tk.TkFiles;
import org.takes.tk.TkMeasured;
import org.takes.tk.TkRedirect;
import org.takes.tk.TkVersioned;
import org.takes.tk.TkWithHeaders;
import org.takes.tk.TkWithType;
import org.takes.tk.TkWrap;

/**
 * App.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.5
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 * @checkstyle ClassFanOutComplexityCheck (500 lines)
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 */
public final class TkApp extends TkWrap {

    /**
     * Version of the system, to show in header.
     */
    private static final String VERSION = String.format(
        "%s %s %s",
        // @checkstyle MultipleStringLiterals (3 lines)
        Manifests.read("Thindeck-Version"),
        Manifests.read("Thindeck-Revision"),
        Manifests.read("Thindeck-Date")
    );

    /**
     * Ctor.
     * @param base Base
     * @throws IOException If fails
     */
    public TkApp(final Base base) throws IOException {
        super(TkApp.make(base));
    }

    /**
     * Ctor.
     * @param base Base
     * @return Take
     * @throws IOException If fails
     */
    private static Take make(final Base base) throws IOException {
        if (!"UTF-8".equals(Charset.defaultCharset().name())) {
            throw new IllegalStateException(
                String.format(
                    "default encoding is %s", Charset.defaultCharset()
                )
            );
        }
        return new TkWithHeaders(
            new TkVersioned(
                new TkMeasured(
                    new TkFlash(
                        new TkAppFallback(
                            new TkForward(
                                new TkAppAuth(
                                    TkApp.regex(base)
                                )
                            )
                        )
                    )
                )
            ),
            String.format("X-Thindeck-Version: %s", TkApp.VERSION),
            "Vary: Cookie"
        );
    }

    /**
     * Regex takes.
     * @param base Base
     * @return Take
     * @throws IOException If fails
     */
    private static Take regex(final Base base) throws IOException {
        return new TkFork(
            new FkParams(
                PsByFlag.class.getSimpleName(),
                Pattern.compile(".+"),
                new TkRedirect()
            ),
            new FkRegex("/robots.txt", ""),
            new FkRegex(
                "/xsl/[a-z\\-]+\\.xsl",
                new TkWithType(
                    TkApp.refresh("./src/main/xsl"),
                    "text/xsl"
                )
            ),
            new FkRegex(
                "/js/[a-z]+\\.js",
                new TkWithType(
                    TkApp.refresh("./src/main/js"),
                    "text/javascript"
                )
            ),
            new FkRegex(
                "/css/[a-z]+\\.css",
                new TkWithType(
                    TkApp.refresh("./src/main/scss"),
                    "text/css"
                )
            ),
            new FkAnonymous(
                new TkFork(
                    new FkRegex("/", new TkIndex(base))
                )
            ),
            new FkAuthenticated(
                new TkFork(
                    new FkRegex("/", new TkDecks(base)),
                    new FkRegex("/acc", new TkAccount(base)),
                    new FkRegex("/add", new TkAddDeck(base)),
                    new FkRegex("/d/.*", new TkDeck(base))
                )
            )
        );
    }

    /**
     * Hit refresh fork.
     * @param path Path of files
     * @return Fork
     * @throws IOException If fails
     */
    @SuppressWarnings("PMD.DoNotUseThreads")
    private static Take refresh(final String path) throws IOException {
        return new TkFork(
            new FkHitRefresh(
                new File(path),
                new Runnable() {
                    @Override
                    public void run() {
                        new VerboseProcess(
                            new ProcessBuilder(
                                "mvn",
                                "generate-resources"
                            )
                        ).stdout();
                    }
                },
                new TkFiles("./target/classes")
            ),
            new FkFixed(new TkClasspath())
        );
    }

}
