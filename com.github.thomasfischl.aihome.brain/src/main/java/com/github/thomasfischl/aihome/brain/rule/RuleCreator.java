package com.github.thomasfischl.aihome.brain.rule;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.thomasfischl.aihome.communication.sensor.SensorDataType;
import com.github.thomasfischl.aihome.controller.rule.Rule;
import com.github.thomasfischl.aihome.controller.rule.RuleStore;

public class RuleCreator {

  private RuleNode root = new RuleNode(null, null, null);

  public RuleCreator() {
  }

  public void addTree(RuleNode node) {
    root.addChild(node);
  }

  public void merge() {
    merge(root);
    System.out.println(this);
    for (int i = 0; i < 5; i++) {
      merge(root);
      flat(root);
    }
    System.out.println(this);
  }

  public void merge(RuleNode node) {
    if (node.isLeaf()) {
      return;
    }

    Map<String, RuleNode> newChildren = new HashMap<>();
    for (RuleNode child : node.getChildren()) {
      String key = child.getName() + "_" + child.getValue();

      if (!newChildren.containsKey(key)) {
        newChildren.put(key, child);
      } else {
        RuleNode newChild = newChildren.get(key);
        for (RuleNode n : child.getChildren()) {
          newChild.addChild(n);
        }
      }
    }

    node.removeAllChilren();
    for (RuleNode n : newChildren.values()) {
      node.addChild(n);
      merge(n);
    }
  }

  public void flat(RuleNode node) {
    if (node.isLeaf()) {
      return;
    }

    if (node.getChildren().get(0).isLeaf()) {
      SensorDataType type = node.getChildren().get(0).getType();
      Set<String> values = new HashSet<>(type.getPossibleValues());

      for (RuleNode n : node.getChildren()) {
        values.remove(n.getValue());
      }

      if (values.isEmpty()) {
        System.out.println("Flatten rule nodes with type '" + type + "'");
        node.removeAllChilren();
      }

    } else {
      for (RuleNode n : node.getChildren()) {
        flat(n);
      }
    }
  }

  public void generateRules() {
    int idx = 0;
    Set<Rule> rules = new HashSet<>();

    Rule r = new Rule("rule");
    for (RuleNode n : root.getChildren()) {
      n.generateRules(r, rules);
    }

    for (Rule rule : rules) {
      rule.setName(rule.getName() + (idx++));
    }

    RuleStore store = new RuleStore();
    store.storeRules(rules);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("ROOT\n");
    for (RuleNode n : root.getChildren()) {
      n.toString(sb, 1);
    }
    return sb.toString();
  }
}
