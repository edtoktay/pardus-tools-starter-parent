/** */
package tech.pardus.rule.flow.manager.datastruture;

import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

/**
 * @author deniz.toktay
 * @param <T>
 * @since Sep 26, 2020
 */
@Getter
@Setter
public class Node<T> implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4818163094426646212L;

  private Node<T> parent;

  private Node<T> prevSibling;

  private Node<T> nextSibling;

  private Node<T> child;

  private T data;

  private boolean executable = false;

  private int level;

  private Node(T data) {
    this.data = data;
  }

  /**
   * @param data
   * @param level
   */
  public Node(T data, int level) {
    this.data = data;
    this.level = level;
  }

  /**
   * @param data
   * @param executable
   */
  public Node(T data, boolean executable) {
    this.data = data;
    this.executable = executable;
  }

  /**
   * @param data
   * @param parent
   */
  public Node(T data, Node<T> parent) {
    this.data = data;
    this.parent = parent;
  }

  /**
   * @param parent
   */
  public void setParent(Node<T> parent) {
    if (Objects.nonNull(parent)) {
      parent.addChild(this);
    }
    this.parent = parent;
  }

  /**
   * @param data
   */
  public void addChild(T data) {
    var newChild = new Node<T>(data);
    newChild.setLevel(this.getLevel() + 1);
    newChild.parent = this;
    this.child = newChild;
  }

  /**
   * @param child
   */
  public void addChild(Node<T> child) {
    child.parent = this;
    child.setLevel(this.getLevel() + 1);
    this.child = child;
  }

  /**
   * @param data
   */
  public void addSibling(T data) {
    var sibling = new Node<T>(data);
    sibling.setLevel(this.getLevel());
    sibling.setPrevSibling(this);
    this.nextSibling = sibling;
  }

  /**
   * @param sibling
   */
  public void addSibling(Node<T> sibling) {
    sibling.setPrevSibling(this);
    sibling.setLevel(this.getLevel());
    this.nextSibling = sibling;
  }

  /**
   * @return true if the node is root
   */
  public boolean isRoot() {
    return this.parent == null;
  }

  /**
   * @return true if the node has root
   */
  public boolean isLeaf() {
    return Objects.isNull(child) && Objects.isNull(nextSibling);
  }

  /**
   * @return true if that level is the last level of tree
   */
  public boolean isLastLevel() {
    return Objects.isNull(child);
  }
}
