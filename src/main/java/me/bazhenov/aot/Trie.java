package me.bazhenov.aot;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class Trie<T extends Serializable> implements Serializable {

	private final TrieNode<T> root = new TrieNode<T>();

	public void add(String word, T value) {
		set(word, value, true);
	}

	public void replace(String word, T value) {
		set(word, value, false);
	}

	private void set(String word, T value, boolean forbidReplace) {
		TrieNode<T> iNode = root;
		for (int i = 0; i < word.length(); i++) {
			Character c = word.charAt(i);
			iNode = iNode.getOrCreateChild(c);
		}
		if (forbidReplace && iNode.hasValue()) {
			throw new IllegalArgumentException("Value already present in trie");
		}
		iNode.setValue(value);
	}

	public T search(String word) {
		TrieNode<T> iNode = root;
		for (int i = 0; i < word.length() && iNode != null; i++) {
			Character c = word.charAt(i);
			iNode = iNode.getChild(c);
		}
		return iNode == null ? null : iNode.getValue();
	}
}

class TrieNode<T extends Serializable> implements Serializable {

	private T value;
	private final Map<Character, TrieNode<T>> children = new TreeMap<Character, TrieNode<T>>();

	TrieNode() {
		this(null);
	}

	TrieNode(T value) {
		this.value = value;
	}

	boolean hasValue() {
		return value != null;
	}

	T getValue() {
		return value;
	}

	void setValue(T value) {
		this.value = value;
	}

	TrieNode<T> getOrCreateChild(Character c) {
		TrieNode<T> node = children.get(c);
		if (node == null) {
			node = new TrieNode<T>();
			children.put(c, node);
		}
		return node;
	}

	public TrieNode<T> getChild(Character c) {
		return children.get(c);
	}
}
