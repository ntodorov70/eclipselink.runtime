/*******************************************************************************
 * Copyright (c) 1998, 2016 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Oracle - initial API and implementation from Oracle TopLink
 ******************************************************************************/
package org.eclipse.persistence.internal.platform.database.oracle.xdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLXML;

import javax.xml.transform.dom.DOMResult;

import org.eclipse.persistence.internal.databaseaccess.BindCallCustomParameter;
import org.eclipse.persistence.internal.databaseaccess.DatabasePlatform;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.w3c.dom.Document;

public class XMLTypeBindCallCustomParameter extends BindCallCustomParameter {

    public XMLTypeBindCallCustomParameter(Object obj) {
        super(obj);
    }

    @Override
    public void set(DatabasePlatform platform, PreparedStatement statement, int index, AbstractSession session) throws SQLException {
        if (this.obj instanceof String) {
            //Bug#5200836, unwrap the connection prior to using.
            Connection con = session.getServerPlatform().unwrapConnection(statement.getConnection());
            SQLXML sqlxml = con.createSQLXML();
            sqlxml.setString((String) obj);
            this.obj = sqlxml;
        } else if (this.obj instanceof Document) {
            //Bug#5200836, unwrap the connection prior to using.
            Connection con = session.getServerPlatform().unwrapConnection(statement.getConnection());
            SQLXML sqlxml = con.createSQLXML();
            DOMResult result = sqlxml.setResult(DOMResult.class);
            result.setNode((Document) obj);
            this.obj = sqlxml;
        }
        super.set(platform, statement, index, session);
    }
}
