/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package edu.cmu.cs.dennisc.javax.swing.components;

import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dennis Cosgrove
 */
public class JList<E> extends javax.swing.JList {
  private List<ActionListener> m_actionListeners = new LinkedList<ActionListener>();

  public JList() {
    initialize();
  }

  public JList(ListModel model) {
    super(model);
    initialize();
  }

  private void initialize() {
    addMouseListener(new MouseListener() {
      @Override
      public void mousePressed(MouseEvent e) {
        fireActionListeners(e.getWhen(), e.getModifiers(), true);
      }

      @Override
      public void mouseReleased(MouseEvent e) {
      }

      @Override
      public void mouseClicked(MouseEvent e) {
      }

      @Override
      public void mouseEntered(MouseEvent e) {
      }

      @Override
      public void mouseExited(MouseEvent e) {
      }
    });
    addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
          //pass
        } else {
          int modifiers = 0; //todo
          fireActionListeners(System.currentTimeMillis(), modifiers, false);
        }
      }
    });
  }

  public void addActionListener(ActionListener l) {
    m_actionListeners.add(l);
  }

  public void removeActionListener(ActionListener l) {
    m_actionListeners.remove(l);
  }

  private int m_selectedIndexPrev = -1;
  private boolean m_isActionEventPrev = false;

  private void fireActionListeners(long when, int modifiers, boolean isActionEvent) {
    int selectedIndex = getSelectedIndex();
    if ((selectedIndex != m_selectedIndexPrev) || (m_isActionEventPrev == isActionEvent)) {
      String command;
      Object value = getSelectedValue();
      if (value != null) {
        command = value.toString();
      } else {
        command = null;
      }
      ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command, when, modifiers);
      for (ActionListener l : m_actionListeners) {
        l.actionPerformed(e);
      }
      m_selectedIndexPrev = selectedIndex;
    } else {
      m_isActionEventPrev = isActionEvent;
    }
  }

  public E getSelectedTypedValue() {
    return (E) getSelectedValue();
  }
}
