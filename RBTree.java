import java.io.BufferedReader;
import java.io.FileReader;

public class RBTree<T extends Comparable<T>> {
	private RBTNode<T> ROOT; // a T type variable to store the root
	private static final boolean RED = false;
	private static final boolean BLACK = true;

	public class RBTNode<T extends Comparable<T>> {
		T ID, count;
		boolean color;
		RBTNode<T> parent, left, right;

		// constructor of RBTNode
		public RBTNode(T ID, T count, boolean color, RBTNode<T> parent, RBTNode<T> left, RBTNode<T> right) {
			this.ID = ID;
			this.count = count;
			this.color = color;
			this.parent = parent;
			this.left = left;
			this.right = right;
		}
	}

	// constructor of RBTree
	public RBTree() {
		ROOT = null;
	}

	private RBTNode<T> parentOf(RBTNode<T> node) {
		return node != null ? node.parent : null;
	}

	private boolean colorOf(RBTNode<T> node) {
		return node != null ? node.color : BLACK;
	}

	private boolean isRed(RBTNode<T> node) {
		return ((node != null) && (node.color == RED)) ? true : false;
	}

	private boolean isBlack(RBTNode<T> node) {
		return !isRed(node);
	}

	private void setBlack(RBTNode<T> node) {
		if (node != null)
			node.color = BLACK;
	}

	private void setRed(RBTNode<T> node) {
		if (node != null)
			node.color = RED;
	}

	private void setParent(RBTNode<T> node, RBTNode<T> parent) {
		if (node != null)
			node.parent = parent;
	}

	private void setColor(RBTNode<T> node, boolean color) {
		if (node != null)
			node.color = color;
	}

	// recursive search for a node with a given ID in a RBT
	private RBTNode<T> recursiveSearch(RBTNode<T> x, T ID) {
		if (x == null)
			return x;
		int cmp = ID.compareTo(x.ID);
		if (cmp < 0)
			return recursiveSearch(x.left, ID);
		else if (cmp > 0)
			return recursiveSearch(x.right, ID);
		else
			return x;
	}

	public RBTNode<T> recursiveSearch(T ID) {
		return recursiveSearch(ROOT, ID);
	}

	// iterative search for a node with a given ID in a RBT
	private RBTNode<T> iterativeSearch(RBTNode<T> x, T ID) {
		while (x != null) {
			int cmp = ID.compareTo(x.ID);
			if (cmp < 0)
				x = x.left;
			else if (cmp > 0)
				x = x.right;
			else
				return x;
		}
		return x;
	}

	public RBTNode<T> iterativeSearch(T ID) {
		return iterativeSearch(ROOT, ID);
	}

	// find minumum ID
	private RBTNode<T> minimum(RBTNode<T> tree) {
		if (tree == null)
			return null;
		while (tree.left != null)
			tree = tree.left;
		return tree;
	}

	public T minimum() {
		RBTNode<T> p = minimum(ROOT);
		if (p != null)
			return p.ID;
		return null;
	}

	// find maximum ID
	private RBTNode<T> maximum(RBTNode<T> tree) {
		if (tree == null)
			return null;
		while (tree.right != null)
			tree = tree.right;
		return tree;
	}

	public T maximum() {
		RBTNode<T> p = maximum(ROOT);
		if (p != null)
			return p.ID;
		return null;
	}

	// Use for Next(theID)
	public RBTNode<T> successor(RBTNode<T> x) {
		// 1.If node x has a right subtree, then the successor of x is just the
		// leftmost node in x's right subtree.
		if (x.right != null)
			return minimum(x.right);

		// 2.x is a left child, so its successor is its parent.
		// 3.the successor of x is the ancestor whose left child is also an
		// ancestor
		RBTNode<T> y = x.parent;
		while ((y != null) && (x == y.right)) {
			x = y;
			y = y.parent;
		}
		return y;
	}

	// Use for Previous(theID)
	public RBTNode<T> predecessor(RBTNode<T> x) {
		// 1.If node x has a left subtree, then the predecessor of x is just the
		// rightmost node in x's left subtree.
		if (x.left != null)
			return maximum(x.left);

		// 2. x is a right child, so its predecessor is its parent.
		// 3. the predecessor of x is the ancestor whose right child is also an
		// ancestor
		RBTNode<T> y = x.parent;
		while ((y != null) && (x == y.left)) {
			x = y;
			y = y.parent;
		}

		return y;
	}

	// left rotate
	private void leftRotate(RBTNode<T> x) {
		// set y
		RBTNode<T> y = x.right;
		// turn y's left subtree into x's right subtree
		x.right = y.left;
		if (y.left != null)
			y.left.parent = x;

		// link x's parent to y's
		y.parent = x.parent;

		if (x.parent == null) {
			this.ROOT = y;
		} else {
			if (x.parent.left == x)
				x.parent.left = y;
			else
				x.parent.right = y;
		}

		// put x on y's left
		y.left = x;
		x.parent = y;
	}

	// right rotate
	private void rightRotate(RBTNode<T> y) {
		// set x
		RBTNode<T> x = y.left;

		// turn x's right subtree into y's left subtree
		y.left = x.right;
		if (x.right != null)
			x.right.parent = y;

		// link y's parent to x's
		x.parent = y.parent;

		if (y.parent == null) {
			this.ROOT = x;
		} else {
			if (y == y.parent.right)
				y.parent.right = x;
			else
				y.parent.left = x;
		}

		// put y on x's right
		x.right = y;
		y.parent = x;
	}

	private void insertFixUp(RBTNode<T> node) {
		RBTNode<T> parent, gparent;

		// if parent node exists and color is red
		while (((parent = parentOf(node)) != null) && isRed(parent)) {
			gparent = parentOf(parent);

			// if parent node is left child of gparent
			if (parent == gparent.left) {
				RBTNode<T> uncle = gparent.right;

				// Case1: uncle node is red
				if ((uncle != null) && isRed(uncle)) {
					setBlack(uncle);
					setBlack(parent);
					setRed(gparent);
					node = gparent;
					continue;
				}

				// Case2: uncle node is black, and current node is right child
				if (parent.right == node) {
					RBTNode<T> tmp;
					leftRotate(parent);
					tmp = parent;
					parent = node;
					node = tmp;
				}

				// Case3: uncle node is black, and current node is left child
				setBlack(parent);
				setRed(gparent);
				rightRotate(gparent);
			} else {
				// if parent node is right child of gparent
				RBTNode<T> uncle = gparent.left;

				// Case1: uncle node is red
				if ((uncle != null) && isRed(uncle)) {
					setBlack(uncle);
					setBlack(parent);
					setRed(gparent);
					node = gparent;
					continue;
				}

				// Case2: uncle node is black, and current node is left child
				if (parent.left == node) {
					RBTNode<T> tmp;
					rightRotate(parent);
					tmp = parent;
					parent = node;
					node = tmp;
				}

				// Case3: uncle node is black, and current node is right child
				setBlack(parent);
				setRed(gparent);
				leftRotate(gparent);
			}
		}

		// set root's color to black
		setBlack(this.ROOT);
	}

	private void insert(RBTNode<T> node) {
		int cmp;
		RBTNode<T> y = null;
		RBTNode<T> x = this.ROOT;

		// insert the node into a BST
		while (x != null) {
			y = x;
			cmp = node.ID.compareTo(x.ID);
			if (cmp < 0)
				x = x.left;
			else
				x = x.right;
		}

		node.parent = y;
		if (y != null) {
			cmp = node.ID.compareTo(y.ID);
			if (cmp < 0)
				y.left = node;
			else
				y.right = node;
		} else {
			this.ROOT = node;
		}

		// set node's color to red
		node.color = RED;
		insertFixUp(node);
	}

	public void insert(T ID, T count) {
		RBTNode<T> node = new RBTNode<T>(ID, count, BLACK, null, null, null);
		if (node != null)
			insert(node);
	}

	private void deleteFixUp(RBTNode<T> node, RBTNode<T> parent) {
		// w is x's sibling
		RBTNode<T> w;
		while ((node == null || isBlack(node)) && (node != this.ROOT)) {
			if (parent.left == node) {

				w = parent.right;
				// Case1: w is red
				if (isRed(w)) {
					setBlack(w);
					setRed(parent);
					leftRotate(parent);
					w = parent.right;
				}

				// Case2: w is black and the children of w are both black
				if ((w.left == null || isBlack(w.left)) && (w.right == null || isBlack(w.right))) {
					setRed(w);
					node = parent;
					parent = parentOf(node);
				} else {

					// Case3: w is black and left child of w is red, right child
					// of w is black
					if (w.right == null || isBlack(w.right)) {
						setBlack(w.left);
						setRed(w);
						rightRotate(w);
						w = parent.right;
					}
					// Case4: w is black and right child of w is red
					setColor(w, colorOf(parent));
					setBlack(parent);
					setBlack(w.right);
					leftRotate(parent);
					node = this.ROOT;
					break;
				}
			} else {

				w = parent.left;

				// Case1: w is red
				if (isRed(w)) {
					setBlack(w);
					setRed(parent);
					rightRotate(parent);
					w = parent.left;
				}

				// Case2: w is black and the children of w are both black
				if ((w.left == null || isBlack(w.left)) && (w.right == null || isBlack(w.right))) {
					setRed(w);
					node = parent;
					parent = parentOf(node);
				} else {

					// Case3: w is black and left child of w is red, right child
					// of w is black
					if (w.left == null || isBlack(w.left)) {
						setBlack(w.right);
						setRed(w);
						leftRotate(w);
						w = parent.left;
					}

					// Case4: w is black and right child of w is red
					setColor(w, colorOf(parent));
					setBlack(parent);
					setBlack(w.left);
					rightRotate(parent);
					node = this.ROOT;
					break;
				}
			}
		}

		if (node != null)
			setBlack(node);
	}

	private void delete(RBTNode<T> node) {
		RBTNode<T> child, parent;
		boolean color;

		// The delete node have both two children.
		if ((node.left != null) && (node.right != null)) {
			RBTNode<T> replace = node;
			replace = replace.right;
			while (replace.left != null)
				replace = replace.left;

			// The delete node is not root.
			if (parentOf(node) != null) {
				if (parentOf(node).left == node)
					parentOf(node).left = replace;
				else
					parentOf(node).right = replace;
			} else {
				// The delete node is root, refresh the root.
				this.ROOT = replace;
			}

			// replace node does not have a left child, because it is a
			// successor
			child = replace.right;
			parent = parentOf(replace);
			color = colorOf(replace);

			if (parent == node) {
				parent = replace;
			} else {
				// child is not null
				if (child != null)
					setParent(child, parent);
				parent.left = child;
				replace.right = node.right;
				setParent(node.right, replace);
			}

			replace.parent = node.parent;
			replace.color = node.color;
			replace.left = node.left;
			node.left.parent = replace;

			if (color == BLACK)
				deleteFixUp(child, parent);

			node = null;
			return;
		}

		if (node.left != null) {
			child = node.left;
		} else {
			child = node.right;
		}

		parent = node.parent;
		color = node.color;

		if (child != null)
			child.parent = parent;

		if (parent != null) {
			if (parent.left == node)
				parent.left = child;
			else
				parent.right = child;
		} else {
			this.ROOT = child;
		}

		if (color == BLACK)
			deleteFixUp(child, parent);
		node = null;
	}

	public void delete(T ID) {
		RBTNode<T> node;
		if ((node = iterativeSearch(ROOT, ID)) != null)
			delete(node);
	}

	// clear the whole RBT
	private void clear(RBTNode<T> tree) {
		if (tree == null)
			return;
		if (tree.left != null)
			clear(tree.left);
		if (tree.right != null)
			clear(tree.right);
		tree = null;
	}

	public void clear() {
		clear(ROOT);
		ROOT = null;
	}

	// 1.Increase(theID, m)
	public void Increase(RBTree<Integer> tree, int theID, int m) {
		RBTree<Integer>.RBTNode<Integer> x = tree.iterativeSearch(theID);
		if (x == null) {
			tree.insert(theID, m);
			System.out.println(m);
		} else {
			x.count += m;
			System.out.println(x.count);
		}
	}

	// 2.Reduce(theID, m)
	public void Reduce(RBTree<Integer> tree, int theID, int m) {
		RBTree<Integer>.RBTNode<Integer> x = tree.iterativeSearch(theID);
		if (x == null) {
			System.out.println("0");
		} else {
			x.count -= m;
			if (x.count <= 0) {
				tree.delete(theID);
				System.out.println("0");
			} else
				System.out.println(x.count);
		}
	}

	// 3.Count(theID)
	public void Count(RBTree<Integer> tree, int theID) {
		RBTree<Integer>.RBTNode<Integer> x = tree.iterativeSearch(theID);
		if (x == null) {
			System.out.println("0");
		} else
			System.out.println(x.count);
	}

	// 4.InRange(ID1, ID2)
	public void InRange(RBTree<Integer> tree, int ID1, int ID2) {
		RBTree<Integer>.RBTNode<Integer> x = tree.iterativeSearch(ID1);
		RBTree<Integer>.RBTNode<Integer> y = null;
		int total = 0;
		if (x != null) {
			while (x.ID <= ID2) {
				y = tree.successor(x);
				total += x.count;
				if (y != null) {
					x = y;
				} else
					break;
			}
		} else {
			x = tree.ROOT;
			while (x != null) {
				y = x;
				if (ID1 < x.ID)
					x = x.left;
				else
					x = x.right;
			}
			while ((y != null) && (ID1 > y.ID)) {
				x = y;
				y = y.parent;
			}
			x = y;
			while (x.ID <= ID2) {
				y = tree.successor(x);
				total += x.count;
				if (y != null) {
					x = y;
				} else
					break;
			}
		}
		System.out.println(total);
	}

	// 5.Next(theID)
	public void Next(RBTree<Integer> tree, int theID) {
		RBTree<Integer>.RBTNode<Integer> x = tree.iterativeSearch(theID);
		RBTree<Integer>.RBTNode<Integer> y = null;
		if (x != null) {
			y = tree.successor(x);
		} else {
			x = tree.ROOT;
			while (x != null) {
				y = x;
				if (theID < x.ID)
					x = x.left;
				else
					x = x.right;
			}
			while ((y != null) && (theID > y.ID)) {
				x = y;
				y = y.parent;
			}
		}
		if (y != null) {
			System.out.println(y.ID + " " + y.count);
		} else
			System.out.println("0 0");
	}

	// 6.Previous(theID)
	public void Previous(RBTree<Integer> tree, int theID) {
		RBTree<Integer>.RBTNode<Integer> x = tree.iterativeSearch(theID);
		RBTree<Integer>.RBTNode<Integer> y = null;
		if (x != null) {
			y = tree.predecessor(x);
		} else {
			x = tree.ROOT;
			while (x != null) {
				y = x;
				if (theID < x.ID)
					x = x.left;
				else
					x = x.right;
			}
			while ((y != null) && (theID < y.ID)) {
				x = y;
				y = y.parent;
			}
		}
		if (y != null) {
			System.out.println(y.ID + " " + y.count);
		} else
			System.out.println("0 0");
	}

	// 7.Initialization
	public void Initialization(RBTree<Integer> tree, String filename) {
		FileReader fr = null;
		BufferedReader br = null;
		String line;
		int l = 0;
		try {
			fr = new FileReader(filename);
			br = new BufferedReader(fr);
			while ((line = br.readLine()) != null) {
				l++;
				if (l == 1)
					continue;
				String[] initial = line.split(" ");
				tree.insert(Integer.parseInt(initial[0]), Integer.parseInt(initial[1]));
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {
			try {
				br.close();
				fr.close();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
	}
}
