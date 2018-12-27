package com.gongml.companyscort.utils.jsonparse.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TreeNode implements Serializable {
  private static final long serialVersionUID = 1L;
  private int id;
  private String name;
  private long nid;
  private long prid;
  private int pid;
  private boolean node;
  private String param1;
  private boolean isUsed;
  private List<TreeNode> treeNode;
  private Map<String, Object> map;

  public TreeNode() {}

  public TreeNode(String name, String param1, int id, int pid) {
    this.name = name;
    this.param1 = param1;
    this.id = id;
    this.pid = pid;
  }

  public TreeNode(String name, long nid, long prid, boolean node) {
    this.name = name;
    this.nid = nid;
    this.prid = prid;
    this.node = node;
  }

  public long getNid() {
    return nid;
  }

  public void setNid(long nid) {
    this.nid = nid;
  }

  public long getPrid() {
    return prid;
  }

  public void setPrid(long prid) {
    this.prid = prid;
  }

  public boolean isNode() {
    return node;
  }

  public void setNode(boolean node) {
    this.node = node;
  }

  public boolean isUsed() {
    return isUsed;
  }

  public void setUsed(boolean isUsed) {
    this.isUsed = isUsed;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPid() {
    return pid;
  }

  public void setPid(int pid) {
    this.pid = pid;
  }

  public String getParam1() {
    return param1;
  }

  public void setParam1(String param1) {
    this.param1 = param1;
  }

  public List<TreeNode> getTreeNode() {
    return treeNode;
  }

  public void setTreeNode(List<TreeNode> treeNode) {
    this.treeNode = treeNode;
  }

  public Map<String, Object> getMap() {
    return map;
  }

  public void setMap(Map<String, Object> map) {
    this.map = map;
  }

  public void addChild(TreeNode node) {
    if (treeNode == null) {
      treeNode = new ArrayList<TreeNode>();
    }
    treeNode.add(node);
  }

  public void addChildAll(List<TreeNode> nodes) {
    if (treeNode == null) {
      treeNode = new ArrayList<TreeNode>();
    }
    treeNode.addAll(nodes);
  }

  public void removeAll() {
    if (treeNode != null) {
      treeNode.clear();
    }
  }

  public boolean childNodeIsEmpty() {
    if (treeNode != null && treeNode.size() > 0) {
      return true;
    }
    return false;
  }
}
