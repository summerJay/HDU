package cn.edu.hdu.assign;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hdu.entity.Markov;
import cn.edu.hdu.entity.Route;
import cn.edu.hdu.entity.State;
import cn.edu.hdu.entity.Transition;

/**
 * ����������װ�ռ�Markov��·���ȵ���ع���
 * 
 * @author ����
 * 
 */
public class CollectRoute {

	private List<Route> routeList;// ���Markov���ϵ�·������
	private List<Transition> transitionList;// �����ϵ���ջ���� �ռ�ÿһ��·��
	private double routeProbability = 1.0;
	private int tcNumber;

	public void collect(Markov markov) {
		routeList = new ArrayList<Route>();
		transitionList = new ArrayList<Transition>();
		tcNumber = markov.getTcNumber();
		State initialState = markov.getInitialState();
		dfs(initialState);
		markov.setRouteList(routeList);
	}

	/**
	 * ��Markov������һ������������������ռ�����·��
	 * 
	 * @param initialState
	 *            ��ʼ״̬
	 */
	private void dfs(State initialState) {
		List<Transition> outTransitions = initialState.getOutTransitions();
		if (outTransitions != null && outTransitions.size() != 0) {
			for (Transition transition : outTransitions) {
				// if (transition.isVisited() == false) {
				// transition.setVisited(true);
				transitionList.add(transition);
				routeProbability = routeProbability
						* transition.getProbability();
				State nextState = transition.getNextState();
				if (nextState.getLabel().equals("final")) {
					List<Transition> oneRoute = new ArrayList<Transition>();
					oneRoute.addAll(transitionList);
					Route route = new Route();
					route.setTransitionList(oneRoute);
					route.setRouteProbability(routeProbability);

					route.setNumber((int) Math.round(tcNumber
							* routeProbability));

					// route.setNumber((int) (tcNumber * routeProbability));
					routeList.add(route);
				}
				dfs(nextState);
				transitionList.remove(transitionList.size() - 1);
				routeProbability = routeProbability
						/ transition.getProbability();
				// }
			}
		}
	}

}
