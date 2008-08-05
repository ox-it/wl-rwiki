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

package uk.ac.cam.caret.sakai.rwiki.service.message.api.dao;

import java.util.List;

import uk.ac.cam.caret.sakai.rwiki.service.message.api.model.PagePresence;

/**
 * @author ieb
 */
public interface PagePresenceDao
{
	PagePresence createPagePresence(String pageName, String pageSpace,
			String sessionid, String user);

	List findBySpace(String pageSpace);

	List findByPage(String pageSpace, String pageName);

	List findByUser(String user);

	PagePresence findBySession(String sessionid);

	/**
	 * @param pp
	 */
	void update(Object o);

	/**
	 * @param pageSpace
	 * @param pageName
	 * @return
	 */
	List findBySpaceOnly(String pageSpace, String pageName);
}
