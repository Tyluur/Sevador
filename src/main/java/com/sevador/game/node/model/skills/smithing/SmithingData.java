package com.sevador.game.node.model.skills.smithing;

import java.util.HashMap;
import java.util.Map;

import com.sevador.game.node.Item;

/**
 * @author <b>Tyluur</b><tyluur@zandium.org> - <code>Wrote the class</code>
 */
public class SmithingData {
	public enum ForgingBar {

		ADAMANT(2361, 70, new Item[] { new Item(1211, 1), new Item(1357, 1),
				new Item(1430, 1), new Item(1145, 1), new Item(9380, 1),
				new Item(1287, 1), new Item(823, 1), new Item(4823, 1),
				new Item(-1, 1), new Item(-1, 1), new Item(-1, 1),
				new Item(43, 1), new Item(1331, 1), new Item(9429, 1),
				new Item(1301, 1), new Item(867, 1), new Item(1161, 1),
				new Item(1183, 1), new Item(-1, 1), new Item(-1, 1),
				new Item(1345, 1), new Item(1371, 1), new Item(1111, 1),
				new Item(1199, 1), new Item(3100, 1), new Item(1317, 1),
				new Item(1091, 1), new Item(1073, 1), new Item(1123, 1),
				new Item(1271, 1) }, new double[] { 62.5, 125, 187.5, 312.5 },
				new int[] { 66, 210, 267 }),

		BRONZE(2349, 0, new Item[] { new Item(1205, 1), new Item(1351, 1),
				new Item(1422, 1), new Item(1139, 1), new Item(877, 1),
				new Item(1277, 1), new Item(819, 1), new Item(4819, 1),
				new Item(1794, 1), new Item(-1, 1), new Item(-1, 1),
				new Item(39, 1), new Item(1321, 1), new Item(9420, 1),
				new Item(1291, 1), new Item(864, 1), new Item(1155, 1),
				new Item(1173, 1), new Item(-1, 1), new Item(-1, 1),
				new Item(1337, 1), new Item(1375, 1), new Item(1103, 1),
				new Item(1189, 1), new Item(3095, 1), new Item(1307, 1),
				new Item(1087, 1), new Item(1075, 1), new Item(1117, 1),
				new Item(1265, 1) }, new double[] { 12.5, 25, 37.5, 62.5 },
				new int[] { 66, 82, 210, 267 }),

		IRON(2351, 15, new Item[] { new Item(1203, 1), new Item(1349, 1),
				new Item(1420, 1), new Item(1137, 1), new Item(9377, 1),
				new Item(1279, 1), new Item(820, 1), new Item(4820, 1),
				new Item(-1, 1), new Item(7225, 1), new Item(-1, 1),
				new Item(40, 1), new Item(1323, 1), new Item(9423, 1),
				new Item(1293, 1), new Item(863, 1), new Item(1153, 1),
				new Item(1175, 1), new Item(4540, 1), new Item(-1, 1),
				new Item(1335, 1), new Item(1363, 1), new Item(1101, 1),
				new Item(1191, 1), new Item(3096, 1), new Item(1309, 1),
				new Item(1081, 1), new Item(1067, 1), new Item(1115, 1),
				new Item(1267, 1) }, new double[] { 25, 50, 75, 125 },
				new int[] { 66, 90, 162, 210, 267 }),

		MITHRIL(2359, 50, new Item[] { new Item(1209, 1), new Item(1355, 1),
				new Item(1428, 1), new Item(1143, 1), new Item(9379, 1),
				new Item(1285, 1), new Item(822, 1), new Item(4822, 1),
				new Item(-1, 1), new Item(-1, 1), new Item(-1, 1),
				new Item(42, 1), new Item(1329, 1), new Item(9427, 1),
				new Item(1299, 1), new Item(866, 1), new Item(1159, 1),
				new Item(1181, 1), new Item(-1, 1), new Item(9416, 1),
				new Item(1343, 1), new Item(1369, 1), new Item(1109, 1),
				new Item(1197, 1), new Item(3099, 1), new Item(1315, 1),
				new Item(1085, 1), new Item(1071, 1), new Item(1121, 1),
				new Item(1273, 1) }, new double[] { 50, 100, 150, 250 },
				new int[] { 66, 170, 210, 267 }),

		RUNE(2363, 85, new Item[] { new Item(1213, 1), new Item(1359, 1),
				new Item(1432, 1), new Item(1147, 1), new Item(9381, 1),
				new Item(1289, 1), new Item(824, 1), new Item(4824, 1),
				new Item(-1, 1), new Item(-1, 1), new Item(-1, 1),
				new Item(44, 1), new Item(1333, 1), new Item(9431, 1),
				new Item(1303, 1), new Item(868, 1), new Item(1163, 1),
				new Item(1185, 1), new Item(-1, 1), new Item(-1, 1),
				new Item(1347, 1), new Item(1373, 1), new Item(1113, 1),
				new Item(1201, 1), new Item(3101, 1), new Item(1319, 1),
				new Item(1093, 1), new Item(1079, 1), new Item(1127, 1),
				new Item(1275, 1) }, new double[] { 75, 150, 225, 375 },
				new int[] { 66, 210, 267 }),

		STEEL(2353, 30, new Item[] { new Item(1207, 1), new Item(1353, 1),
				new Item(1424, 1), new Item(1141, 1), new Item(9378, 1),
				new Item(1281, 1), new Item(821, 1), new Item(1539, 1),
				new Item(-1, 1), new Item(-1, 1), new Item(2370, 1),
				new Item(41, 1), new Item(1325, 1), new Item(9425, 1),
				new Item(1295, 1), new Item(865, 1), new Item(1157, 1),
				new Item(1177, 1), new Item(4544, 1), new Item(-1, 1),
				new Item(1339, 1), new Item(1365, 1), new Item(1105, 1),
				new Item(1193, 1), new Item(3097, 1), new Item(1311, 1),
				new Item(1083, 1), new Item(1069, 1), new Item(1119, 1),
				new Item(1269, 1) }, new double[] { 37.5, 75, 112.5, 187.5 },
				new int[] { 66, 98, 162, 210, 267 });

		private static Map<Integer, ForgingBar> bars = new HashMap<Integer, ForgingBar>();

		static {
			for (ForgingBar bar : ForgingBar.values()) {
				bars.put(bar.getBarId(), bar);
			}
		}

		public static ForgingBar forId(int id) {
			return bars.get(id);
		}

		private int barId;
		private int[] componentChilds;
		private double[] experience;
		private Item items[];
		private int level;

		private ForgingBar(int barId, int level, Item[] items,
				double[] experience, int[] componentChilds) {
			this.barId = barId;
			this.level = level;
			this.items = items;
			this.componentChilds = componentChilds;
			this.experience = experience;
		}

		public int getBarId() {
			return barId;
		}

		public int[] getComponentChilds() {
			return componentChilds;
		}

		public double[] getExperience() {
			return experience;
		}

		public Item[] getItems() {
			return items;
		}

		public int getLevel() {
			return level;
		}
	}
}
