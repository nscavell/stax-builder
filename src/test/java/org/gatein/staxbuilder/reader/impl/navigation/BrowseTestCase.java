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

package org.gatein.staxbuilder.reader.impl.navigation;

import org.gatein.staxbuilder.reader.NavigationReadEvent;
import org.gatein.staxbuilder.reader.StaxReader;
import org.gatein.staxbuilder.reader.StaxReaderBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:alain.defrance@exoplatform.com">Alain Defrance</a>
 * @author <a href="mailto:nscavell@redhat.com">Nick Scavelli</a>
 * @version $Revision$
 */
public class BrowseTestCase
{
   private StaxReader reader;
   private NavigationReadEvent navigator;

   @Before
   public void setUp() throws Exception
   {
      InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("sample.xml");
      reader = new StaxReaderBuilder().withInputStream(is).build();
      reader.readNextTag();
      navigator = reader.buildReadEvent().withNavigation();
   }

   @Test
   public void testInit() throws Exception
   {
      assertEquals("foo1", reader.currentReadEvent().getLocalName());
   }

   @Test
   public void testContent() throws Exception
   {
      assertEquals("bar1", navigator.child().getLocalName());
      assertEquals("1", navigator.getText());
      assertEquals("foo2", navigator.sibling().getLocalName());
      assertEquals("bar2", navigator.child().getLocalName());
      assertEquals("2", navigator.getText());
      assertTrue(navigator.sibling("bar3").success());
      assertEquals("foo3", navigator.child().getLocalName());
      assertEquals("4", navigator.getText());
      //TODO: The below logic doesn't make sense - taken from stax nav test (we need a way to nav back up)
//      assertTrue(navigator.sibling("foobar1").success());
//      assertEquals("3", navigator.getText());
   }

   @Test
   public void testChild() throws Exception
   {
      assertEquals("bar1", navigator.child().getLocalName());
      assertEquals("bar1", navigator.getLocalName());
   }

   @Test
   public void testChildWithName() throws Exception
   {
      assertTrue(navigator.child("foobar1").success());
      assertEquals("foobar1", navigator.getLocalName());
      assertEquals(2, navigator.getLevel());
   }

   @Test
   public void testChildOver() throws Exception
   {
      assertEquals("bar1", navigator.child().getLocalName());
      assertEquals("1", navigator.getText());
      assertFalse(navigator.child().success());
      assertTrue(navigator.sibling("foobar1").success());
      assertEquals("foobar2", navigator.sibling().getLocalName());
   }

   @Test
   public void testChildWithNameOver() throws Exception
   {
      assertTrue(navigator.child("foo2").success());
      assertFalse(navigator.child("donotexist").success());
      assertEquals("foo2", navigator.getLocalName());
      assertEquals(2, navigator.getLevel());
      assertTrue(navigator.sibling("foobar1").success());
      assertEquals("foobar2", navigator.sibling().getLocalName());
      assertNull(navigator.sibling().getText());
   }

   //TODO: Fix child EOF
/*   @Test
   public void testChildEOF() throws Exception
   {
      assertTrue(navigator.child("foobar2").success());
      assertFalse(navigator.child().success());
      assertFalse(navigator.sibling().success());
      assertEquals("foobar2", navigator.getLocalName());
   }
*/
   @Test
   public void testSibling() throws Exception
   {
      assertEquals("bar1", navigator.child().getLocalName());
      assertEquals("foo2", navigator.sibling().getLocalName());
      assertEquals("foo2", navigator.getLocalName());
      assertEquals(2, navigator.getLevel());
      assertEquals("foobar1", navigator.sibling().getLocalName());
      assertEquals("foobar1", navigator.getLocalName());
      assertEquals(2, navigator.getLevel());
   }

   @Test
   public void testSiblingWithName() throws Exception
   {
      assertEquals("bar1", navigator.child().getLocalName());
      assertEquals(2, navigator.getLevel());
      assertTrue(navigator.sibling("foobar1").success());
      assertEquals("foobar1", navigator.getLocalName());
      assertEquals(2, navigator.getLevel());
   }

   @Test
   public void testSiblingOver() throws Exception
   {
      assertTrue(navigator.child("foo2").success());
      assertTrue(navigator.child("bar3").success());
      assertTrue(navigator.child("foo3").success());
      //TODO: Should sibling really work like this ?
      //assertEquals("foobar1", navigator.sibling().getLocalName());
      //assertEquals("foobar2", navigator.sibling().getLocalName());
   }

   @Test
   public void testSiblingWithNameOver() throws Exception
   {
      assertTrue(navigator.child("foo2").success());
      assertTrue(navigator.child("bar2").success());

      //assertTrue(navigator.sibling("foobar2").success());
   }

   @Test
   public void testSiblingEOF() throws Exception
   {
      assertTrue(navigator.child("foo2").success());
//      assertTrue(navigator.child("bar2").success());
      assertTrue(navigator.sibling("foobar2").success());
      assertFalse(navigator.sibling().success());
      assertEquals("foobar2", navigator.getLocalName());
   }

   //TODO: Fix sibling EOF
/*
   @Test
   public void testSiblingWithNameEOF() throws Exception
   {
      assertTrue(navigator.child("foo2").success());
      assertEquals("foo2", navigator.getLocalName());
      assertFalse(navigator.sibling("donotexist").success());
      assertEquals("foo2", navigator.getLocalName());
      assertEquals(2, navigator.getLevel());
      assertTrue(navigator.sibling("foobar1").success());
      assertEquals("foobar1", navigator.getLocalName());
      assertEquals("3", navigator.getText());
      assertEquals(2, navigator.getLevel());
      assertEquals("foobar2", navigator.sibling().getLocalName());
   }
*/
   //TODO: Implement attributes
   /*
   public void testAttribute() throws Exception
   {

      navigator.child();
      navigator.sibling();
      navigator.child();
      assertEquals("bar2", navigator.getLocalName());
      assertEquals("b", navigator.getAttribute("a"));
      assertEquals("c", navigator.getAttribute("b"));
      assertEquals(null, navigator.getAttribute("donotexists"));
   }

   public void testAttributeInPushback() throws Exception
   {

      assertTrue(navigator.child("foo2"));
      assertTrue(navigator.child("bar2"));
      assertEquals("bar2", navigator.getLocalName());
      assertFalse(navigator.sibling("donotexist"));
      assertEquals("bar2", navigator.getLocalName());
      assertEquals(3, navigator.getLevel());
      assertEquals("b", navigator.getAttribute("a"));
      assertEquals("c", navigator.getAttribute("b"));
      assertEquals(null, navigator.getAttribute("donotexists"));
      assertTrue(navigator.sibling("foobar1"));
      assertEquals("bar", navigator.getAttribute("foo"));
   }*/
}
