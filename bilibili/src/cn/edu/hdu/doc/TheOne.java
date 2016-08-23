package cn.edu.hdu.doc;

import java.io.FileOutputStream;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import cn.edu.hdu.entity.Markov;
import cn.edu.hdu.entity.State;
import cn.edu.hdu.entity.Transition;

/**
 * ��Ŀ�������������࣬������ȡfile�ļ�������ж�markov�������Ƿ���δ����������Ǩ�ơ�
 * 
 * @author YJ
 * @version 1.0
 * */

public class TheOne {

	/**
	 * ������:������ö�ȡ���еĶ����������������������е����ɲ������������� ����ƽ�ȷֲ����еļ���ƽ�ȷֲ��������������ƶ����еļ������ƶȵķ����ȡ�
	 * */
	public static void main(String[] args) throws Exception {

		// ReadMarkov rm = new ReadMarkov();
		ReadMarkov2 rm = new ReadMarkov2();
		Markov markov = rm.readMarkov();

		// System.out.println(markov.getNumberOfStates());
		Calculate.getAllTransValues(markov);

		// ����һ��document�������ڴ洢��������
		Document dom = DocumentHelper.createDocument();
		Element root = dom.addElement("TCS");

		double[] PI = CalculateDistribution.stationaryDistribution(markov);// ����markov����ƽ�ȷֲ�

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

			System.out.println("\n��ǰʹ�����Ͳ����������ƶ�Ϊ:" + similarity);
			System.out.println("\n��ǰ���ɵĲ��������Ͳ���·���ĸ���:" + numberOfTestCases
					+ "\n\n");

		} while (similarity > 0.001);

		// WriteTestCases.writeCases(gc.getTestCases());

		for (double d : PI) {
			System.out.print(d + "  ");
		}
		// ���洢�ò���������document����д��XML�ļ�
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter writer = new XMLWriter(new FileOutputStream(
				"E:/Markov/bilibili/tcs.xml"), format);
		writer.write(dom);
		writer.close();

	}

	/**
	 * �ж�markov�����Ƿ��з��ʴ���Ϊ0��Ǩ��
	 * 
	 * @param markov
	 *            ����һ��ָ����markov������
	 * @return ����ָ��markov�������Ƿ�����˳���ԵĲ���ֵ
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
