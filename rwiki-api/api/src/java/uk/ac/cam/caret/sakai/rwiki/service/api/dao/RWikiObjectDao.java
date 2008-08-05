/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright 2006 Sakai Foundation
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

import java.util.List;

import uk.ac.cam.caret.sakai.rwiki.service.api.model.RWikiObject;

//FIXME: Service

public interface RWikiObjectDao
{
	/**
	 * Get a list of all objects. <b> Note this must not cause all objects to be
	 * loaded, but rather iterate through the list on demand</b>
	 * 
	 * @return
	 */
	List getAll();

	/**
	 * Update the object. Should not update explicity lazy loaded objects.
	 * 
	 * @param rwo
	 */
	void updateObject(RWikiObject rwo);
}
