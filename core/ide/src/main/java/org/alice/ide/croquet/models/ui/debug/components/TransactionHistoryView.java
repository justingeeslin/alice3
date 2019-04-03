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

package org.alice.ide.croquet.models.ui.debug.components;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.ide.croquet.models.ui.debug.TransactionHistoryComposite;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.history.event.ChangeEvent;
import org.lgna.croquet.history.event.ActivityEvent;
import org.lgna.croquet.history.event.Listener;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.ScrollPane;

import javax.swing.JTree;
import javax.swing.SwingUtilities;

/**
 * @author Dennis Cosgrove
 */
public class TransactionHistoryView extends BorderPanel {

  private final Listener historyListener = new Listener() {
    private void reload() {
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          treeModel.reload();
          collapseDesiredRows();
          tree.scrollRowToVisible(tree.getRowCount() - 1);
          scrollPane.getAwtComponent().getHorizontalScrollBar().setValue(0);
        }
      });
    }

    @Override
    public void changed(ActivityEvent e) {
      if (e instanceof ChangeEvent) {
        this.reload();
      }
      tree.repaint();
    }
  };

  private final ScrollPane scrollPane = new ScrollPane();
  private final JTree tree = new JTree();
  private UserActivity rootActivity;
  private boolean isCollapsingDesired = true;
  private TransactionHistoryTreeModel treeModel;

  public TransactionHistoryView(TransactionHistoryComposite composite) {
    super(composite);
    this.scrollPane.getAwtComponent().setViewportView(this.tree);
    this.tree.setRootVisible(false);
    this.tree.setCellRenderer(new TransactionHistoryCellRenderer());
    this.addCenterComponent(this.scrollPane);
  }

  private void collapseDesiredRows() {
    int childCount = treeModel.getChildCount(treeModel.getRoot());
    for (int i = 0; i < tree.getRowCount(); i++) {
      if (isCollapsingDesired() && (i < (childCount - 1))) {
        tree.collapseRow(i);
      } else {
        tree.expandRow(i);
      }
    }
  }

  public boolean isCollapsingDesired() {
    return this.isCollapsingDesired;
  }

  public void setCollapsingDesired(boolean isCollapsingDesired) {
    this.isCollapsingDesired = isCollapsingDesired;
    if (this.treeModel != null) {
      this.treeModel.reload();
    }
  }

  public void setRootActivity(UserActivity rootActivity) {
    if (rootActivity == null) {
      Logger.outln("No root activity provided to view.");
      return;
    }

    this.removeTransactionListener();
    this.rootActivity = rootActivity;

    if (this.isShowing()) {
      this.addTransactionListener();
    }
  }

  @Override
  protected void handleDisplayable() {
    super.handleDisplayable();
    this.addTransactionListener();
  }

  @Override
  protected void handleUndisplayable() {
    this.removeTransactionListener();
    super.handleUndisplayable();
  }

  private void addTransactionListener() {
    if (this.rootActivity != null) {
      this.treeModel = new TransactionHistoryTreeModel(rootActivity);
      this.tree.setModel(this.treeModel);
      this.revalidateAndRepaint();
      this.treeModel.reload();
      this.rootActivity.addListener(this.historyListener);
    }
  }

  private void removeTransactionListener() {
    if ((this.rootActivity != null) && (this.rootActivity.isListening(this.historyListener))) {
      this.rootActivity.removeListener(this.historyListener);
    }
  }
}
