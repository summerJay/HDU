package cn.edu.hdu.assign;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import cn.edu.hdu.entity.Markov;
import cn.edu.hdu.entity.Route;
import cn.edu.hdu.entity.Stimulate;
import cn.edu.hdu.entity.Transition;
import cn.edu.hdu.random.CalculateSimilarity;
import cn.edu.hdu.random.RandomCase;

public class BestAssign {

	public void assign(Markov markov, Element root) {
		int actualTcNumber = 0;
		List<Route> routeList = markov.getRouteList();
		for (Route route : routeList) {
			actualTcNumber += route.getNumber();
			List<Transition> transitionList = route.getTransitionList();
			List<Stimulate> stimulateList = transform(transitionList,
					route.getNumber());
			for (int i = 0; i < route.getNumber(); i++) {
				System.out.print("����������");
				RandomCase.getCase(stimulateList, root);
			}
		}
		System.out.println("\nʵ�����ɵ��ܲ�����������Ϊ��" + actualTcNumber);

		printBaseTestSequence(routeList);

		System.out.println("\n����ŷ�Ͼ������������ģ�����ƶȣ�"
				+ CalculateSimilarity.statistic_1(markov));
	}

	/**
	 * ��ӡ���е�·����������
	 * 
	 * @param routeList
	 */
	private void printBaseTestSequence(List<Route> routeList) {
		System.out.println("\nMarkov���Ļ����������м���������" + routeList.size() + "����");
		for (Route route : routeList) {
			String testSequence = "";
			List<Transition> transitionList = route.getTransitionList();
			for (Transition transition : transitionList) {
				testSequence += transition.getName() + "����";
			}
			testSequence = testSequence.substring(0, testSequence.length() - 2);
			System.out.println("			�������У�" + testSequence + "	 ·������:"
					+ route.getRouteProbability() + "	������������"
					+ route.getNumber() + "��");
		}

	}

	/**
	 * ��Ǩ�Ƽ���ת���ɼ�������
	 * 
	 * @param transitionList
	 * @param routeNumber
	 * @return
	 */
	private List<Stimulate> transform(List<Transition> transitionList,
			int routeNumber) {
		List<Stimulate> stimulateList = new ArrayList<Stimulate>();
		for (Transition transition : transitionList) {
			stimulateList.add(transition.getStimulate());
			transition
					.setAccessTimes(transition.getAccessTimes() + routeNumber);
		}
		return stimulateList;
	}
}
