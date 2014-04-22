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
