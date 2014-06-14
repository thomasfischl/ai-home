package com.github.thomasfischl.aihome.controller.rule;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Joiner;

public class RuleStore {

  public void loadRules(File file) {
    // TODO
  }

  public void storeRules(Collection<Rule> rules) {
    for (Rule r : rules) {
      for (Condition c : r.getConditions()) {
        
        List<String> values = new ArrayList<>(c.getValues());
        Collections.sort(values);
        
        String ruleStr = r.getName() + "." + c.getName() + " = " + Joiner.on(";").join(values);
        System.out.println(ruleStr);
      }
    }

  }

}
