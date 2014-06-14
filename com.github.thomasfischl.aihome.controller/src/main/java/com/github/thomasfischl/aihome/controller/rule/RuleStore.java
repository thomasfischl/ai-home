package com.github.thomasfischl.aihome.controller.rule;

import java.io.File;
import java.util.Collection;

import com.google.common.base.Joiner;

public class RuleStore {

  public void loadRules(File file) {
    // TODO
  }

  public void storeRules(Collection<Rule> rules) {
    for (Rule r : rules) {
      for (Condition c : r.getConditions()) {
        String ruleStr = r.getName() + "." + c.getName() + " = " + Joiner.on(";").join(c.getValues());
        System.out.println(ruleStr);
      }
    }

  }

}
