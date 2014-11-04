package com.thindeck.dynamo;

import com.jcabi.xml.XML;
import com.thindeck.api.Memo;
import org.xembly.Directive;

import java.io.IOException;

/**
 * Dynamo implementation of {@link com.thindeck.api.Memo}.
 *
 * @author Rodrigo G. de A. (rodrigo.gdea@gmail.com)
 * @version $Id$
 */
public class DyMemo implements Memo {

    /**
     * The XML String memo representation
     */
    private String memo;

    public DyMemo(final String memo) {
        this.memo = memo;
    }

    @Override
    public XML read() throws IOException {
        throw new UnsupportedOperationException("#read");
    }

    @Override
    public void update(Iterable<Directive> dirs) throws IOException {
        throw new UnsupportedOperationException("#update");
    }
}
