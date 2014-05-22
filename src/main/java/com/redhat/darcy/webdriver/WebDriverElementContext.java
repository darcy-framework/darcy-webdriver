/*
 Copyright 2014 Red Hat, Inc. and/or its affiliates.

 This file is part of darcy-webdriver.

 This program is free software: you can redistribute it and/or modify
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

package com.redhat.darcy.webdriver;

import com.redhat.darcy.ui.ElementContext;
import com.redhat.darcy.ui.FindsByChained;
import com.redhat.darcy.ui.FindsById;
import com.redhat.darcy.ui.FindsByLinkText;
import com.redhat.darcy.ui.FindsByName;
import com.redhat.darcy.ui.FindsByNested;
import com.redhat.darcy.ui.FindsByPartialTextContent;
import com.redhat.darcy.ui.FindsByTextContent;
import com.redhat.darcy.ui.FindsByXPath;
import com.redhat.darcy.web.FindsByCssSelector;
import com.redhat.darcy.web.FindsByHtmlTag;

public interface WebDriverElementContext extends ElementContext, FindsById, FindsByName,
        FindsByLinkText, FindsByTextContent, FindsByPartialTextContent, FindsByXPath,
        FindsByCssSelector, FindsByHtmlTag, FindsByChained, FindsByNested {
}
