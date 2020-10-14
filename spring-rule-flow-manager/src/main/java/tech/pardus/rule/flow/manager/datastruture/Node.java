/**
 *
 */
package tech.pardus.rule.flow.manager.datastruture;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

/**
 * @author deniz.toktay
 * @since Sep 26, 2020
 */
@Getter
@Setter
public class Node<T> {

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

	public Node(T data, int level) {
		this.data = data;
		this.level = level;
	}

	public Node(T data, boolean executable) {
		this.data = data;
		this.executable = executable;
	}

	public Node(T data, Node<T> parent) {
		this.data = data;
		this.parent = parent;
	}

	public void setParent(Node<T> parent) {
		if (Objects.nonNull(parent)) {
			parent.addChild(this);
		}
		this.parent = parent;
	}

	public void addChild(T data) {
		var child = new Node<T>(data);
		child.setLevel(this.getLevel() + 1);
		child.parent = this;
		this.child = child;
	}

	public void addChild(Node<T> child) {
		child.parent = this;
		child.setLevel(this.getLevel() + 1);
		this.child = child;
	}

	public void addSibling(T data) {
		var sibling = new Node<T>(data);
		sibling.setLevel(this.getLevel());
		sibling.setPrevSibling(this);
		this.nextSibling = sibling;
	}

	public void addSibling(Node<T> sibling) {
		sibling.setPrevSibling(this);
		sibling.setLevel(this.getLevel());
		this.nextSibling = sibling;
	}

	public boolean isRoot() {
		return this.parent == null;
	}

	public boolean isLeaf() {
		return Objects.isNull(child) && Objects.isNull(nextSibling);
	}

	public boolean isLastLevel() {
		return Objects.isNull(child);
	}

}
