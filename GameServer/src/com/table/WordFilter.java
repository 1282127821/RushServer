package com.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.util.CTabFile;
import com.util.Log;

/**
 * 敏感词过滤，主要用于角色取名，聊天和帮会取名判断是否合法
 */
public final class WordFilter {
	private volatile List<String> arrt;//文字
	private volatile Node rootNode = new Node('R');
	private static final WordFilter instance = new WordFilter();
	public static WordFilter getInstance(){
		return instance;
	}
	
	public boolean load(String fileName) {
		CTabFile file = new CTabFile();
		if (!file.load(fileName)) {
			return false;
		}

		arrt = new ArrayList<String>();
		String content = file.getStringByColName(0, "DirtyContent");
		String tempStr[] = content.split(",");
		for (int i = 0, len = tempStr.length; i < len; i++) {
			
		}

		return true;
	}
	
	public void initWords() {
		try {
			rootNode = new Node('R');
			arrt.clear();
//			HashMap<String, Q_filterwordBean> map = DataManager.getInstance().q_filterwordContainer.getMap();
//			Q_filterwordBean t_filterwordBean = map.get(Global.LANGUAGE_CODE);
//			String keys = t_filterwordBean.getQ_keys();
//			if (keys == null) {
//				return;
//			}

//			String tempstr[] = keys.split(",");
//			for (int i = 0; i < tempstr.length; i++) {
//				String ss = tempstr[i];
//
//				arrt.add(ss.replace("\"", "").trim());
//			}
			
			createTree();
		} catch (Exception e) {
			Log.error("敏感词初始化失败", e);
		}
	}

	private void createTree() {
		for (String str : arrt) {
			char[] chars = str.toCharArray();
			if (chars.length > 0)
				insertNode(rootNode, chars, 0);
		}
	}

	private void insertNode(Node node, char[] cs, int index) {
		Node n = findNode(node, cs[index]);
		if (n == null) {
			n = new Node(cs[index]);
			node.nodes.put(String.valueOf(cs[index]), n);
		}

		if (index == (cs.length - 1))
			n.flag = 1;

		index++;
		if (index < cs.length)
			insertNode(n, cs, index);
	}

	/**
	 * 过滤敏感词
	 */
	public String badWordstFilterStr(String content) {
		char[] chars = content.toCharArray();
		Node node = rootNode;
		StringBuffer buffer = new StringBuffer();
		List<String> badList = new ArrayList<String>();
		int a = 0;
		while (a < chars.length) {
			node = findNode(node, chars[a]);
			if (node == null) {
				node = rootNode;
				a = a - badList.size();
				if (badList.size() > 0) {
					badList.clear();
				}
				buffer.append(chars[a]);
			} else if (node.flag == 1) {
				badList.add(String.valueOf(chars[a]));
				for (int i = 0; i < badList.size(); i++) {
					buffer.append("*");
				}
				node = rootNode;
				badList.clear();
			} else {
				badList.add(String.valueOf(chars[a]));
				if (a == chars.length - 1) {
					for (int i = 0; i < badList.size(); i++) {
						buffer.append(badList.get(i));
					}
				}
			}
			a++;
		}
		return buffer.toString();
	}

	public static void main(String[] a) {
//		WordFilter swf = new WordFilter();// BadWordsFilter.getInstance();
//		swf.initWords();
//		String qq = "";
//		for (int i = 0; i < 100; i++) {
//			qq += swf.badWordstFilterStr("想做爱操逼了没了，激情女优,稀少罕见的美少女蝴蝶屄,mm的诱人双峰→免费试看片呵呵，做去吧,你真的想做爱吗做爱真的很好吗");
//		}
//		System.out.println(qq);
//		if (log.isDebugEnabled()) {
//			log.debug("main(String[]) - " + qq); //$NON-NLS-1$
//		}
//		String qq = "你好!";
//		log.error(swf.hashNoLimitedWords(qq));
	}
	
	/**
	 * 名字是否有不允许字符
	 */
	public boolean hashNoLimitedWords(String content) {
		char[] chars = content.toCharArray();
		for (char c : chars) {
			//c是汉字
			if ((c >= 0x4e00)&&(c <= 0x9fbb)){
				continue;
			}
			//c是数字或英文
			if(Character.isLetterOrDigit(c)){
				continue;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 是否有坏字
	 */
	public boolean hashBadWords(String content) {
		char[] chars = content.toCharArray();
		Node node = rootNode;
		StringBuffer buffer = new StringBuffer();
		List<String> word = new ArrayList<String>();
		int a = 0;
		while (a < chars.length) {
			node = findNode(node, chars[a]);
			if (node == null) {
				node = rootNode;
				a = a - word.size();
				buffer.append(chars[a]);
				word.clear();
			} else if (node.flag == 1) {
				node = null;
				return true;
			} else {
				word.add(String.valueOf(chars[a]));
			}
			a++;
		}
		return false;
	}

	private Node findNode(Node node, char c) {
		return node.nodes.get(String.valueOf(c));
	}
}

class Node {
	public int flag;
	public HashMap<String, Node> nodes = new HashMap<String, Node>();

	public Node(char c) {
		this.flag = 0;
	}
}

//import java.io.InputStream;  
//import java.io.UnsupportedEncodingException;  
//import java.nio.ByteBuffer;  
//import java.util.ArrayList;  
//import java.util.Enumeration;  
//import java.util.List;  
//import java.util.Properties;  
//  
//public class test {        
//    /**  
//     * 根节点  
//     */    
//    private TreeNode rootNode = new TreeNode();    
//        
//    /**  
//     * 关键词缓存  
//     */    
//    private ByteBuffer keywordBuffer = ByteBuffer.allocate(1024);       
//        
//    /**  
//     * 关键词编码  
//     */    
//    private String charset = "utf-8";    
//    
//    /**  
//     * 创建DFA  
//     * @param keywordList  
//     * @throws UnsupportedEncodingException   
//     */    
//    public void createKeywordTree(List<String> keywordList) throws UnsupportedEncodingException{    
//        for (String keyword : keywordList) {    
//            if(keyword == null) continue;    
//            keyword = keyword.trim();    
//            byte[] bytes = keyword.getBytes(charset);    
//            TreeNode tempNode = rootNode;    
//            for (int i = 0; i < bytes.length; i++) {    
//                int index = bytes[i] & 0xff;     
//                TreeNode node = tempNode.getSubNode(index);    
//                if(node == null){    
//                    node = new TreeNode();    
//                    tempNode.setSubNode(index, node);    
//                }    
//                tempNode = node;    
//                if(i == bytes.length - 1){    
//                    tempNode.setKeywordEnd(true);      
//                }    
//            }    
//        }  
//    }    
//        
//     
//    public String searchKeyword(String text) throws UnsupportedEncodingException{    
//        return searchKeyword(text.getBytes(charset));    
//    }    
//     
//    public String searchKeyword(byte[] bytes){    
//        StringBuilder words = new StringBuilder();    
//        if(bytes == null || bytes.length == 0){    
//            return words.toString();    
//        }    
//        TreeNode tempNode = rootNode;    
//        int rollback = 0;     
//        int position = 0;   
//        while (position < bytes.length) {    
//            int index = bytes[position] & 0xFF;    
//            keywordBuffer.put(bytes[position]);   
//            tempNode = tempNode.getSubNode(index);    
//            if(tempNode == null){  
//                position = position - rollback;  
//                rollback = 0;    
//                tempNode = rootNode;        
//                keywordBuffer.clear();    
//            }    
//            else if(tempNode.isKeywordEnd()){    
//                keywordBuffer.flip();    
//                for (int i = 0; i <= rollback; i++) {  
//                        bytes[position-i] = 42;  
//                }  
//                keywordBuffer.limit(keywordBuffer.capacity());    
//                rollback = 1;    
//            }else{     
//                rollback++;   
//            }    
//            position++;    
//        }    
//        String result = null;  
//         try {  
//             result  =  new String(bytes,"utf-8");    
//              
//        } catch (Exception e) {  
//            e.printStackTrace();  
//        }  
//        return result;  
//    }    
//        
//    public void setCharset(String charset) {    
//        this.charset = charset;    
//    }   
//}  


//import java.util.ArrayList;  
//import java.util.List;  
//  
//public class TreeNode {    
//    private static final int NODE_LEN = 256;    
//        
//    /**  
//     * true 关键词的终结 ； false 继续  
//     */    
//    private boolean end = false;     
//        
//    private List<TreeNode> subNodes = new ArrayList<TreeNode>(NODE_LEN);    
//        
//    public TreeNode(){    
//        for (int i = 0; i < NODE_LEN; i++) {    
//            subNodes.add(i, null);    
//        }    
//    }    
//        
//    /**  
//     * 向指定位置添加节点树  
//     * @param index  
//     * @param node  
//     */    
//    public void setSubNode(int index, TreeNode node){    
//        subNodes.set(index, node);    
//    }    
//        
//    public TreeNode getSubNode(int index){    
//        return subNodes.get(index);    
//    }    
//        
//    
//    public boolean isKeywordEnd() {    
//        return end;    
//    }    
//    
//    public void setKeywordEnd(boolean end) {    
//        this.end = end;    
//    }    
//}    