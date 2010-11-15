/*
 * JBoss, a division of Red Hat
 * Copyright 2010, Red Hat Middleware, LLC, and individual
 * contributors as indicated by the @authors tag. See the
 * copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.gatein.xml.stax.reader;

import org.gatein.xml.stax.EnumElement;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public class LargeXmlStaxReaderTest
{
   @Test
   public void largeXmlRead() throws XMLStreamException
   {
      InputStream is = getClass().getClassLoader().getResourceAsStream("pages-large.xml");
      StaxReader reader = new StaxReaderBuilder().withInputStream(is).build();
      List<PortletApplication> apps = new ArrayList<PortletApplication>();
      while (reader.hasNext())
      {
         switch (reader.read().match(Element.class, Element.UNKNOWN))
         {
            case PORTLET_APPLICATION:
               switch(reader.readNextTag().match(Element.class, Element.UNKNOWN))
               {
                  case PORTLET:
                     PortletApplication app = new PortletApplication();
                     app.appRef = reader.readNextTag().elementText();
                     app.portlet = reader.readNextTag().elementText();
                     apps.add(app);
               }
               break;
            default:
         }
      }
      Assert.assertEquals(200, apps.size());
      for (int i=0; i<apps.size(); i+=5)
      {
         PortletApplication app = apps.get(i);
         Assert.assertEquals("web", app.appRef);
         Assert.assertEquals("HomePagePortlet", app.portlet);
      }
   }

   @Test
   public void largeXmlRead_WithNestedReads() throws XMLStreamException
   {
      InputStream is = getClass().getClassLoader().getResourceAsStream("pages-large.xml");
      StaxReader reader = new StaxReaderBuilder().withInputStream(is).build();
      int titleCount = 0;
      int accessCount = 0;
      int showInforBarCount = 0;
      while (reader.hasNext())
      {
         StaxReadEvent event = reader.read().and().buildReadEvent().until(Element.PAGE).end().build();
         while (event.hasNext())
         {
            switch (reader.read().match(Element.class, Element.UNKNOWN))
            {
               case TITLE:
                  titleCount++;
                  break;
               case ACCESS_PERMISSIONS:
                  accessCount++;
                  break;
               case SHOW_INFO_BAR:
                  showInforBarCount++;
                  break;
               default:
            }
         }
      }
      Assert.assertEquals(400, titleCount); // 2 for every page
      Assert.assertEquals(400, accessCount); // 2 for every page
      Assert.assertEquals(200, showInforBarCount); // 1 for every page
   }

   private static class PortletApplication
   {
      private String appRef;
      private String portlet;
   }

   private static enum Element implements EnumElement<Element>
   {
      PAGE("page"),
      TITLE("title"),
      PORTLET_APPLICATION("portlet-application"),
      PORTLET("portlet"),
      APPLICATION_REF("application-ref"),
      PORTLET_REF("portlet-ref"),
      ACCESS_PERMISSIONS("access-permissions"),
      SHOW_INFO_BAR("show-info-bar"),
      UNKNOWN(null)
      ;

      private static final Map<String, Element> MAP;

      static {
         final Map<String, Element> map = new HashMap<String, Element>();
         for (Element element : values()) {
            final String name = element.getLocalName();
            map.put(name, element);
         }
         MAP = map;
      }

      private String name;
      Element(String name)
      {
          this.name = name;
      }

      @Override
      public Element forName(String name)
      {
         return MAP.get(name);
      }

      @Override
      public String getLocalName()
      {
         return name;
      }
   }
}
