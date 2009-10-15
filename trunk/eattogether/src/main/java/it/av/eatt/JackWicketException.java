/**
 * Copyright 2009 the original author or authors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package it.av.eatt;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JackWicketException extends Exception {

    private static final long serialVersionUID = -2549208951901428566L;
    private static Log log = LogFactory.getLog(JackWicketException.class);

    public JackWicketException() {
    }

    public JackWicketException(String s) {
        super(s);
        log.error(s);
    }

    public JackWicketException(String s, Throwable throwable) {
        super(s, throwable);
        log.error(s, throwable);
    }

    public JackWicketException(Throwable throwable) {
        super(throwable);
        log.error("Error on JackWicket", throwable);
    }
}