package main.basic;

public class TraversalTree {
	//保存树的第level层的所有节点值
	public static String res = "";

	/**
	 * 要求：请实现函数treeLevel(TNode tree, int level)，level为整数，表示树的层数。
	 * 		 要求能返回树tree的第level层的所有节点值，并且输出顺序为从左到右。例如：treeLevel(tree, 3)，则返回G-H-C-F
	 * 方法：使用递归，访问结点的子结点时将level减一，level==1就表示第level层的结点。
	 */
	public static String treeLevel(TNode tree, int level) {
		//如果根结点为空，则返回null
		if (tree == null)
			return null;
		//递归的退出条件，level == 1表示第level层的结点。
		if (level == 1)
			res += tree.value + "-";
		else {
			//访问左子树
			treeLevel(tree.left, level - 1);
			//访问右子树
			treeLevel(tree.right, level - 1);
		}

		//返回结果前去掉末尾的"-"
		return res.substring(0, res.length() - 1);
	}

	//二叉树的字符串表示
	static String tree = "ABG##H##DC##F##";
	//二叉树字符串的下标
	static int i = 0;
	/**
	 * 根据二叉树字符串返回根节点
	 * @return
	 */
	private static TNode createTree() {
		TNode temp = null;

		if (tree.charAt(i) == ' ')
			i++;
		if (tree.charAt(i) == '#') {
			i++;
			return null;
		}
		if (tree.charAt(i) != ' ') {
			temp = new TNode();
			temp.value = "" + tree.charAt(i++);
			temp.left = createTree();
			temp.right = createTree();
		}

		return temp;
	}

	public static void main(String[] args) {
		TNode a = createTree();
		System.out.println(treeLevel(a, 3));
	}
}
