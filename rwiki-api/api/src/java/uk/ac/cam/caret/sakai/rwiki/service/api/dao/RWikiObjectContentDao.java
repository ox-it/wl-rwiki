/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright 2003, 2004, 2005, 2006 Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 *
 *       http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 **********************************************************************************/

package uk.ac.cam.caret.sakai.rwiki.service.api.dao;

import uk.ac.cam.caret.sakai.rwiki.service.api.model.RWikiObject;
import uk.ac.cam.caret.sakai.rwiki.service.api.model.RWikiObjectContent;

// FIXME: Service

public interface RWikiObjectContentDao
{
	/**
	 * get the content object for the RWikiObject
	 * 
	 * @param parent
	 *        the RWikiObject
	 * @return
	 */
	RWikiObjectContent getContentObject(RWikiObject parent);

	/**
	 * Create a new content object and associate it with the RWikiObject
	 * 
	 * @param parent
	 * @return
	 */
	RWikiObjectContent createContentObject(RWikiObject parent);

	/**
	 * Update the content object
	 * 
	 * @param content
	 */
	void update(RWikiObjectContent content);

}
