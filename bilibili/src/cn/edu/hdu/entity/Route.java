package cn.edu.hdu.entity;

import java.util.List;

/**
 * �����ʾMarkov����һ��·��
 * 
 * @author ����
 * 
 */
public class Route {

	private List<Transition> transitionList;// ·����Ǩ�����м���
	private double routeProbability; // ·������
	private int number; // �̶�������������ʱ����·����ռ����

	public List<Transition> getTransitionList() {
		return transitionList;
	}

	public void setTransitionList(List<Transition> transitionList) {
		this.transitionList = transitionList;
	}

	public double getRouteProbability() {
		return routeProbability;
	}

	public void setRouteProbability(double routeProbability) {
		this.routeProbability = routeProbability;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "Route [transitionList=" + transitionList
				+ ", routeProbability=" + routeProbability + ", number="
				+ number + "]";
	}

}
