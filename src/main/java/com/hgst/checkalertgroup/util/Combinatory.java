package com.hgst.checkalertgroup.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Combinatory<T> {
	
	private T[] array;
	
	private boolean[] index;
	
	private int size;
	
	private Class<T> type;

	/**
	 * 
	 * @param t Array
	 * @param type Class
	 */
	public Combinatory(T[] t, Class<T> type) {
		array = t;
		this.size = t.length;
		index = new boolean[size];
		this.type = type;
	}
	
	/**
	 * 产生m个元素的组合
	 * @param m
	 * @return
	 */
	public List<T[]> combine(int m) {
		List<T[]> ret = new ArrayList<T[]>();
		init(m);
		
		// add the first one
		ret.add(generateOneCombine(m));
		
		// swap and generate
		while(!over(m)) {
			for(int i = 0; i < size - 1; i++) {
				if(index[i] && !index[i+1]) { //i:true, i+1:false
					swap(i, i+1);
					moveLeft(i);
					ret.add(generateOneCombine(m));
					break;
				}
			}
		}
		
		return ret;
	}
	
	private void swap(int i, int j) {
		boolean tmp = index[i];
		index[i] = index[j];
		index[j] = tmp;
	}
	
	private void moveLeft(int pos) {
		int count = countOfTrue(pos);
		for(int i = 0; i < count; i++) {
			index[i] = true;
		}
		for(int i = count; i < pos; i++) {
			index[i] = false;
		}
	}
	
	private int countOfTrue(int pos) {
		int count = 0;
		for(int i = 0; i < pos; i++) {
			if(index[i]) ++count;
		}
		return count;
	}

	private void init(int m) {
		for(int i = 0; i < m; i++) {
			index[i] = true;
		}
		for(int i = m; i < size; i++) {
			index[i] = false;
		}
	}
	
	@SuppressWarnings("unchecked")
	private T[] generateOneCombine(final int m) {
		T[] t = (T[]) Array.newInstance(this.type, m);
		int k = 0;
		for(int i = 0; i < size; i++) {
			if(index[i]) {
				t[k++] = array[i];
			}
		}
		return t;
	}
	
	private boolean over(int m) {
		for(int i = 0; i < size - m; i++) {
			if(index[i]) return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		Character[] c = {'a', 'b', 'c', 'd', 'e', 'f'};
		Combinatory<Character> com = new Combinatory<Character>(c, Character.class);
		List<Character[]> list = com.combine(3);
		for(Character[] ch : list) {
			System.out.println(Arrays.toString(ch));
		}
	}
}
