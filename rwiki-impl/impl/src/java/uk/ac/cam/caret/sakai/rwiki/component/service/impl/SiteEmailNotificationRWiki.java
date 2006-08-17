/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006 The Sakai Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

package uk.ac.cam.caret.sakai.rwiki.component.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.component.cover.ServerConfigurationService;
import org.sakaiproject.email.api.DigestService;
import org.sakaiproject.entity.api.EntityManager;
import org.sakaiproject.entity.api.Reference;
import org.sakaiproject.entity.api.ResourceProperties;
import org.sakaiproject.event.api.Event;
import org.sakaiproject.event.api.NotificationAction;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.api.SiteService;
import org.sakaiproject.thread_local.api.ThreadLocalManager;
import org.sakaiproject.time.api.TimeService;
import org.sakaiproject.util.SiteEmailNotification;

import uk.ac.cam.caret.sakai.rwiki.service.api.RWikiObjectService;
import uk.ac.cam.caret.sakai.rwiki.service.api.RenderService;
import uk.ac.cam.caret.sakai.rwiki.service.api.model.RWikiEntity;
import uk.ac.cam.caret.sakai.rwiki.service.api.model.RWikiObject;
import uk.ac.cam.caret.sakai.rwiki.service.message.api.PreferenceService;
import uk.ac.cam.caret.sakai.rwiki.utils.DigestHtml;
import uk.ac.cam.caret.sakai.rwiki.utils.NameHelper;

/**
 * <p>
 * SiteEmailNotificationRWiki fills the notification message and headers with details from the content change that triggered the notification event.
 * </p>
 * 
 * @author Sakai Software Development Team
 * @author ieb
 */
public class SiteEmailNotificationRWiki extends SiteEmailNotification
{
	private static Log log = LogFactory.getLog(SiteEmailNotificationRWiki.class);

	private RenderService renderService = null;

	private RWikiObjectService rwikiObjectService = null;

	private PreferenceService preferenceService = null;

	private SiteService siteService;

	private SecurityService securityService;

	private EntityManager entityManager;

	private ThreadLocalManager threadLocalManager;

	private TimeService timeService;

	private DigestService digestService;

	protected Thread senderThread = null;

	protected Vector sendList = new Vector();

	/**
	 * Construct.
	 */
	public SiteEmailNotificationRWiki(RWikiObjectService rwikiObjectService, RenderService renderService,
			PreferenceService preferenceService, SiteService siteService, SecurityService securityService,
			EntityManager entityManager, ThreadLocalManager threadLocalManager, TimeService timeService, DigestService digestService)
	{
		this.renderService = renderService;
		this.rwikiObjectService = rwikiObjectService;
		this.preferenceService = preferenceService;
		this.siteService = siteService;
		this.securityService = securityService;
		this.entityManager = entityManager;
		this.threadLocalManager = threadLocalManager;
		this.timeService = timeService;
		this.digestService = digestService;
	}

	/**
	 * Construct.
	 */
	public SiteEmailNotificationRWiki(RWikiObjectService rwikiObjectService, RenderService renderService,
			PreferenceService preferenceService, SiteService siteService, SecurityService securityService,
			EntityManager entityManager, ThreadLocalManager threadLocalManager, TimeService timeService,
			DigestService digestService, String siteId)
	{
		super(siteId);
		this.renderService = renderService;
		this.rwikiObjectService = rwikiObjectService;
		this.preferenceService = preferenceService;
		this.siteService = siteService;
		this.securityService = securityService;
		this.entityManager = entityManager;
		this.threadLocalManager = threadLocalManager;
		this.timeService = timeService;
		this.digestService = digestService;

	}

	/**
	 * @inheritDoc
	 */
	public NotificationAction getClone()
	{
		SiteEmailNotificationRWiki clone = new SiteEmailNotificationRWiki(rwikiObjectService, renderService, preferenceService,
				siteService, securityService, entityManager, threadLocalManager, timeService, digestService);
		clone.set(this);

		return clone;
	}

	protected String getSiteId(String context)
	{
		if (context.startsWith("/site/"))
		{
			context = context.substring("/site/".length());
		}
		int il = context.indexOf("/");
		if (il != -1)
		{
			context = context.substring(0, il);
		}
		return context;
	}

	/**
	 * @inheritDoc
	 */
	protected String getMessage(Event event)
	{
		// get the content & properties
		Reference ref = entityManager.newReference(event.getResource());
		ResourceProperties props = ref.getProperties();

		// get the function
		// String function = event.getEvent();

		// use either the configured site, or if not configured, the site
		// (context) of the resource
		String siteId = (getSite() != null) ? getSite() : getSiteId(ref.getContext());

		// get a site title
		String title = siteId;
		try
		{
			Site site = siteService.getSite(siteId);
			title = site.getTitle();
		}
		catch (Exception ignore)
		{
		}

		// get the URL and resource name.
		// StringBuffer buf = new StringBuffer();
		String url = ServerConfigurationService.getAccessUrl() + ref.getUrl() + "html";

		String pageName = props.getProperty(RWikiEntity.RP_NAME);
		String realm = props.getProperty(RWikiEntity.RP_REALM);
		String localName = NameHelper.localizeName(pageName, realm);
		String user = props.getProperty(RWikiEntity.RP_USER);
		String moddate = new Date(Long.parseLong(props.getProperty(RWikiEntity.RP_VERSION))).toString();
		String content = "";
		try
		{
			RWikiEntity rwe = (RWikiEntity) rwikiObjectService.getEntity(ref);
			RWikiObject rwikiObject = rwe.getRWikiObject();

			String pageSpace = NameHelper.localizeSpace(pageName, realm);
			ComponentPageLinkRenderImpl cplr = new ComponentPageLinkRenderImpl(pageSpace);
			content = renderService.renderPage(rwikiObject, pageSpace, cplr);
			content = DigestHtml.digest(content);
			if (content.length() > 1000)
			{
				content = content.substring(0, 1000);
			}

		}
		catch (Exception ex)
		{

		}

		String message = "A Wiki Page has been changed in the \"" + title + "\" site at "
				+ ServerConfigurationService.getString("ui.service", "Sakai") + " (" + ServerConfigurationService.getPortalUrl()
				+ ")  " + " \n" + " \n" + "	Location: site \"" + title + "\" > Wiki  > " + localName + "\n" + " Modified at: "
				+ moddate + "\n" + " Modified by User: " + user + "\n" + " \n" + " 	Page: " + localName + " " + url + " \n" + " \n"
				+ " Content: \n" + content + "\n";

		return message;
	}

	/**
	 * @inheritDoc
	 */
	protected List getHeaders(Event e)
	{
		List rv = new ArrayList(1);
		Reference ref = entityManager.newReference(e.getResource());
		ResourceProperties props = ref.getProperties();

		String pageName = props.getProperty(RWikiEntity.RP_NAME);
		String realm = props.getProperty(RWikiEntity.RP_REALM);
		String localName = NameHelper.localizeName(pageName, realm);

		String subjectHeader = "Subject: The wiki page " + localName + " has been modified";
		// the Subject
		rv.add(subjectHeader);

		// from
		rv.add(getFrom(e));

		// to
		rv.add(getTo(e));

		return rv;
	}

	/**
	 * @inheritDoc
	 */
	protected String getTag(String newline, String title)
	{
		// tag the message
		String rv = "----------------------\n" + "This automatic notification message was sent by "
				+ ServerConfigurationService.getString("ui.service", "Sakai") + " (" + ServerConfigurationService.getPortalUrl()
				+ ") from the " + title + " site.\n"
				+ "You can modify how you receive notifications at My Workspace > Preferences.";
		/*
		 * String rv = newline + "------" + newline + rb.getString("this") + " " + ServerConfigurationService.getString("ui.service", "Sakai") + " (" + ServerConfigurationService.getPortalUrl() + ") " + rb.getString("forthe") + " " + title + " " +
		 * rb.getString("site") + newline + rb.getString("youcan") + newline;
		 */
		return rv;
	}
}
