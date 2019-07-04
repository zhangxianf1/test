package test.basic;

import static org.junit.Assert.*;

import java.util.Stack;

import org.junit.Test;

import main.basic.TNode;
import main.basic.TraversalTree;

public class TestTraversalTree {
	/**
	 * 访问树的第level层的所有结点，测试结果是否和预期一致
	 */
	@Test
	public void test1() {
		//二叉树的字符串表示：根据先序遍历来表示，#表示子树为空
		String tree = "ABG##H##DC##F##";
		//根据字符串返回树的根节点
		TNode a = createTree(tree);

		//字符串res在方法外部,是类的成员，调用方法后需要初始化res
		assertEquals("G-H-C-F", TraversalTree.treeLevel(a, 3));
		TraversalTree.res = "";
		assertEquals("B-D", TraversalTree.treeLevel(a, 2));
		TraversalTree.res = "";
		assertEquals("A", TraversalTree.treeLevel(a, 1));
		TraversalTree.res = "";

	}

	//二叉树字符串的下标
	static int i = 0;

	/**
	 * 根据二叉树字符串返回根节点
	 * @return 返回二叉树的根节点
	 */
	private static TNode createTree(String tree) {
		//返回的二叉树结点
		TNode temp = null;
		//如果字符串是空，直接遍历下一个
		if (tree.charAt(i) == ' ')
			i++;
		//如果是#，表示空结点，返回null
		if (tree.charAt(i) == '#') {
			i++;
			return null;
		}
		//如果是一个字母，就生成二叉树结点，递归创建它的子结点
		if (tree.charAt(i) != ' ') {
			temp = new TNode();
			temp.value = "" + tree.charAt(i++);
			temp.left = createTree(tree);
			temp.right = createTree(tree);
		}

		return temp;
	}
}
