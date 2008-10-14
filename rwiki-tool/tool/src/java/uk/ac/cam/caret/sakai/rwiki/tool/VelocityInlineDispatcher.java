/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2003, 2004, 2005, 2006, 2007 The Sakai Foundation.
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

package uk.ac.cam.caret.sakai.rwiki.tool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.context.Context;
import org.sakaiproject.util.FormattedText;
import org.sakaiproject.content.cover.ContentHostingService;
import org.sakaiproject.tool.cover.ToolManager;

import uk.ac.cam.caret.sakai.rwiki.service.exception.PermissionException;
import uk.ac.cam.caret.sakai.rwiki.service.exception.ReadPermissionException;
import uk.ac.cam.caret.sakai.rwiki.tool.bean.ResourceLoaderBean;
import uk.ac.cam.caret.sakai.rwiki.tool.bean.ViewBean;
import uk.ac.cam.caret.sakai.rwiki.tool.bean.helper.ResourceLoaderHelperBean;
import uk.ac.cam.caret.sakai.rwiki.tool.command.Dispatcher;
import uk.ac.cam.caret.sakai.rwiki.utils.UserDisplayHelper;

/**
 * @author ieb
 */
public class VelocityInlineDispatcher implements Dispatcher
{
	private static final String MACROS = "/WEB-INF/vm/macros.vm";
	private static final String WIKI_CONFIG = "/sakai-rwiki-tool/scripts/";
	private VelocityEngine vengine;

	private String inlineMacros;

	private String basePath;

	private VelocityUtilBean utilBean = new VelocityUtilBean();
	protected static final String LIBRARY_PATH = "/library/";

	/** The name of the context variable containing the identifier for the site's root content collection */
	protected static final String CONTEXT_SITE_COLLECTION_ID = "vppa_site_collection_id";


	public void init(ServletContext context) throws ServletException
	{
		inlineMacros = MACROS;
		try
		{
			vengine = new VelocityEngine();

			vengine.setApplicationAttribute(ServletContext.class.getName(), context);

			Properties p = new Properties();
			p.load(this.getClass().getResourceAsStream("rwikivelocity.config"));
			vengine.init(p);
			vengine.getTemplate(inlineMacros);

		}
		catch (Exception ex)
		{
			throw new ServletException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.ac.cam.caret.sakai.rwiki.tool.command.Dispatcher#dispatch(java.lang.String,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public void dispatch(String path, HttpServletRequest request,
			HttpServletResponse response) throws ServletException
	{
		VelocityContext vcontext = new VelocityContext();

		// EventCartridge ec = new EventCartridge();
		// ec.addEventHandler(new ExcludeEscapeHtmlReference());
		// ec.attachToContext(vcontext);
		String collectionId = ContentHostingService.getSiteCollection(ToolManager.getCurrentPlacement().getContext());

		vcontext.put("session", request.getSession());
		vcontext.put("request", request);
		vcontext.put("requestScope", RequestScopeSuperBean.getFromRequest(request));
		vcontext.put("util", utilBean);
		vcontext.put("collectionId", collectionId);
		vcontext.put("fckLibraryPath", LIBRARY_PATH);
		vcontext.put("fckConfigPath", WIKI_CONFIG);
		try
		{
			String filePath = path + ".vm";
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			vengine.mergeTemplate(filePath, vcontext, response.getWriter());
		}
		catch (MethodInvocationException e)
		{
			Throwable cause = e.getWrappedThrowable();
			if (cause instanceof PermissionException)
			{
				try
				{
					String filePath = "/WEB-INF/vm/permission.vm";
					response.setContentType("text/html");
					response.setCharacterEncoding("UTF-8");
					vengine.mergeTemplate(filePath, vcontext, response.getWriter());
				}
				catch (Exception ex)
				{
					throw new ServletException(ex);
				}
			}
			else
			{
				throw new ServletException(e);

			}

		}
		catch (Exception e)
		{
			throw new ServletException(e);
		}
	}

	public class VelocityUtilBean
	{

		public String escapeHtml(String val)
		{
			return FormattedText.escapeHtml(val, false);
		}

		public String formatDisplayName(String name)
		{
			if (name == null)
			{
				return "unknown";
			}
			return UserDisplayHelper.formatDisplayName(name);
		}

		public String formatDateTime(Date date, HttpServletRequest request)
		{
			if (date == null)
			{
				return "unkown-date";
			}
			ResourceLoaderBean rlb = ResourceLoaderHelperBean.getResourceLoader(request);
			DateFormat formatter = new SimpleDateFormat(rlb.getString("date_format"));
			return formatter.format(date);
		}

		public ViewBean newViewBean(RequestScopeSuperBean rssb)
		{
			ViewBean vb = new ViewBean();
			vb.setLocalSpace(rssb.getCurrentLocalSpace());
			return vb;
		}

	}

}
