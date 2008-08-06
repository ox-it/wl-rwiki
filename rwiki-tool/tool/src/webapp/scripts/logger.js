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

var logdiv = null;
var logdivID = "logdiv";
var logInfo = false;
function log(message) {
	if ( logInfo ) {
		if ( logdiv == null ) {
			logdiv = document.getElementById(logdivID);
		}
		if ( logdiv != null ) {
			var ts = new Date();
			logdiv.innerHTML+=ts+":"+message+"<br>";
		}
	}
}
function clearLog() {
	if ( logdiv == null ) {
		logdiv = document.getElementById("logdiv");
	}
	if ( logdiv != null ) {
		logdiv.innerHTML="<hr>";
	}
}

