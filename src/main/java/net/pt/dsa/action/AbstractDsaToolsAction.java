package net.pt.dsa.action;

import net.pt.dsa.DsaTools;

import javax.swing.*;

/**
 * Created by ptaucher on 10.01.2019<br/>
 */
public abstract class AbstractDsaToolsAction extends AbstractAction {
  protected final DsaTools tools;

  protected AbstractDsaToolsAction(DsaTools tools) {
    this.tools = tools;
  }
}
