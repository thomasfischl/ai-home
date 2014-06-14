package com.github.thomasfischl.aihome.brain.rule;

import java.util.ArrayList;
import java.util.List;

import com.github.thomasfischl.aihome.controller.rule.Condition;
import com.github.thomasfischl.aihome.controller.rule.Rule;

public class RuleNode {

  private List<RuleNode> children = new ArrayList<>();

  private boolean leaf = true;

  private String name;

  private String value;

  public RuleNode(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public void addChild(RuleNode node) {
    children.add(node);
    leaf = false;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  public boolean isLeaf() {
    return leaf;
  }

  public List<RuleNode> getChildren() {
    return children;
  }

  public void removeAllChilren() {
    children.clear();
  }

  public void toString(StringBuilder sb, int deep) {
    for (int i = 0; i < deep; i++) {
      sb.append("  ");
    }
    sb.append(name + " => " + value + "\n");

    for (RuleNode n : children) {
      n.toString(sb, deep + 1);
    }
  }

  public void generateRules(Rule r, List<Rule> rules) {

    Condition condition = r.getCondition(name);
    if (condition == null) {
      condition = new Condition(name, value);
      r.addCondition(condition);
    } else {
      condition.addValue(value);
    }

    if (!isLeaf() && children.get(0).isLeaf()) {
      rules.add(r);
    }

    for (RuleNode n : children) {

      if (n.isLeaf()) {
        n.generateRules(r, rules);
      } else {
        n.generateRules(r.cloneRule(), rules);
      }

    }
  }

}
