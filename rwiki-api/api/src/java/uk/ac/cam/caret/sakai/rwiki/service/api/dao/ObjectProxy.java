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

/**
 * An Object proxy inspects objects and returns a proxied version where
 * necessary.
 * 
 * @author ieb
 */
// FIXME: Service
public interface ObjectProxy
{
	Object proxyObject(Object o);
}
