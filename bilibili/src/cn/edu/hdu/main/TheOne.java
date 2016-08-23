package cn.edu.hdu.main;

import java.io.FileOutputStream;
import java.util.Scanner;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import cn.edu.hdu.assign.BestAssign;
import cn.edu.hdu.assign.CollectRoute;
import cn.edu.hdu.assign.SearchConditions;
import cn.edu.hdu.entity.Markov;
import cn.edu.hdu.entity.Route;
import cn.edu.hdu.entity.State;
import cn.edu.hdu.entity.Transition;
import cn.edu.hdu.random.Calculate;
import cn.edu.hdu.random.CalculateDistribution;
import cn.edu.hdu.random.CalculateSimilarity;
import cn.edu.hdu.random.GenerateCases;
import cn.edu.hdu.random.ReadMarkov2;

/**
 * 项目的主函数所在类，包括获取file文件对象和判断markov链对象是否还有未被遍历过的迁移。
 * 
 * @author YJ
 * @version 1.0
 * */

public class TheOne {

	/**
	 * 主函数:负责调用读取类中的读方法，产生测试用例类中的生成测试用例方法， 计算平稳分布类中的计算平稳分布方法，计算相似度类中的计算相似度的方法等。
	 * */
	public static void main(String[] args) throws Exception {

		// ReadMarkov rm = new ReadMarkov();
		ReadMarkov2 rm = new ReadMarkov2();
		Markov markov = rm.readMarkov();

		Scanner s = new Scanner(System.in);
		System.out.println("请选择测试用例生成模式：");
		System.out.println("			1.根据模型相似度随机生成");
		System.out.println("			2.自定义测试用例个数生成");
		int model = s.nextInt();
		if (model == 2) {
			int min = getMinTCNum(markov, s);
			System.out.println("请输入你想要生成的测试用例个数,并且不低于满足当前充分性指标的最低测试用例个数" + min
					+ "：");
			int N;
			while ((N = s.nextInt()) < min) {
				System.out.println("当前输入的测试用例个数不满足要求，请重新输入：");
			}
			markov.setTcNumber(N);
		}

		s.close();

		// System.out.println(markov.getNumberOfStates());
		Calculate.getAllTransValues(markov);

		// 创建一个document对象，用于存储测试用例
		Document dom = DocumentHelper.createDocument();
		Element root = dom.addElement("TCS");
		// 计算markov链的平稳分布
		double[] PI = CalculateDistribution.stationaryDistribution(markov);

		if (model == 2) {
			new CollectRoute().collect(markov);
			new BestAssign().assign(markov, root);
			System.out.println("利用平稳分布计算出的最佳模型相似度："
					+ CalculateSimilarity.statistic(markov, PI));
		} else if (model == 1) {

			double similarity = 999991;
			boolean sufficiency = false;
			GenerateCases gc = new GenerateCases();
			boolean flag = true;

			do {
				int numberOfTestCases = gc.generate(markov, root);
				// System.out.println(numberOfTestCases);
				if (flag) {

					sufficiency = isSufficient(markov);
				}

				if (!sufficiency) {
					continue;
				}
				flag = false;
				// similarity = CalculateSimilarity.statistic_1(markov);
				similarity = CalculateSimilarity.statistic(markov, PI);

				System.out.println("\n当前使用链和测试链的相似度为:" + similarity);
				System.out.println("\n当前生成的测试用例和测试路径的个数:" + numberOfTestCases
						+ "\n\n");

			} while (similarity > 0.001);

			// WriteTestCases.writeCases(gc.getTestCases());

			for (double d : PI) {
				System.out.print(d + "  ");
			}
		}

		// 将存储好测试用例的document对象写入XML文件
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(new FileOutputStream(
				"E:/Markov/bilibili/tcs.xml"), format);
		writer.write(dom);
		writer.close();

	}

	private static int getMinTCNum(Markov markov, Scanner s) throws Exception {
		// 按照pc公式计算最小测试用例数目
		System.out.println("请输入软件可靠性指标p和置信度c：");
		System.out.print("p = ");
		double p = s.nextDouble();
		System.out.print("c = ");
		double c = s.nextDouble();
		int min_pc = (int) Math.ceil(Math.log10(1 - c) / Math.log10(1 - p));

		// 按照固定最小概率路径个数为一来计算最小测试用例数目
		new CollectRoute().collect(markov);
		double prob = 1;
		for (Route r : markov.getRouteList()) {
			if (r.getRouteProbability() < prob) {
				prob = r.getRouteProbability();
			}
		}
		int min_routePro = (int) Math.round(1.0 / prob);

		// 按照DO-178B MCDC准则计算最小测试用例数目(条件数+1)
		int min_mcdc = SearchConditions.findConditionNum() + 1;

		int temp = Math.max(min_pc, min_routePro);

		return Math.max(temp, min_mcdc);

	}

	/**
	 * 判断markov链中是否有访问次数为0的迁移
	 * 
	 * @param markov
	 *            接受一个指定的markov链对象
	 * @return 返回指定markov链对象是否满足此充分性的布尔值
	 * */
	private static boolean isSufficient(Markov markov) {

		for (State state : markov.getStates()) {

			for (Transition outTransition : state.getOutTransitions()) {

				if (outTransition.getAccessTimes() == 0) {
					return false;
				}
			}
		}
		return true;
	}

}
