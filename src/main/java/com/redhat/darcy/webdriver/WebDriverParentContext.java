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

import com.redhat.darcy.ui.api.ParentContext;
import com.redhat.darcy.ui.internal.FindsById;
import com.redhat.darcy.ui.internal.FindsByName;
import com.redhat.darcy.ui.internal.FindsByTitle;
import com.redhat.darcy.ui.internal.FindsByView;
import com.redhat.darcy.web.api.Alert;
import com.redhat.darcy.web.internal.FindsByUrl;

public interface WebDriverParentContext extends ParentContext, FindsById, FindsByName, FindsByView,
        FindsByTitle, FindsByUrl {
    Alert alert();
}
